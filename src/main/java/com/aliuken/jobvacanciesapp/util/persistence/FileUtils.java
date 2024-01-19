package com.aliuken.jobvacanciesapp.util.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliuken.jobvacanciesapp.enumtype.FileType;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;

@Slf4j
public class FileUtils {
	private static final String ZIP_EXTENSION = ".zip";

	private FileUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", FileUtils.class.getName()));
	}

	/**
	 * Method to delete a given file path recursively
	 */
	public static void deletePathRecursively(final Path filePath) {
		if(filePath == null) {
			if(log.isWarnEnabled()) {
				log.warn(StringUtils.getStringJoined("Error when trying to delete a file: The file must not be null"));
			}
			return;
		}

		try {
			final boolean deleteRecursivelyResult = FileSystemUtils.deleteRecursively(filePath);
			if(deleteRecursivelyResult) {
				if(log.isDebugEnabled()) {
					final String filePathString = filePath.toAbsolutePath().toString();
					log.debug(StringUtils.getStringJoined("The file \"", filePathString, "\" was deleted"));
				}
			} else {
				if(log.isWarnEnabled()) {
					final String filePathString = filePath.toAbsolutePath().toString();
					log.warn(StringUtils.getStringJoined("Error when trying to delete the file \"", filePathString, "\": The file already didn't exist"));
				}
			}
		} catch(final IOException exception) {
			if(log.isErrorEnabled()) {
				final String filePathString = filePath.toAbsolutePath().toString();
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("Error when trying to delete the file \"", filePathString, "\". Exception: ", stackTrace));
			}
		}
	}

	/**
	 * Method to upload and unzip (if it is a zip file) a MultipartFile (from a HTML form) in the hard drive
	 */
	public static List<String> uploadAndOptionallyUnzipFile(final MultipartFile multipartFile, final String destinationFolderPathString, final FileType fileType) throws IOException {
		if(multipartFile == null) {
			return null;
		}

		if(multipartFile.isEmpty()) {
			throw new IllegalArgumentException("Error when trying to upload a file: The file must not be empty");
		}

		if(destinationFolderPathString == null) {
			throw new IllegalArgumentException("Error when trying to upload a file: The destination folder must not be null");
		}

		if(fileType == null) {
			throw new IllegalArgumentException("Error when trying to upload a file: The file type must not be null");
		}

		final String originalFileName = multipartFile.getOriginalFilename();

		final List<String> finalFileNameList;
		if(originalFileName.endsWith(ZIP_EXTENSION)) {
			finalFileNameList = uploadAndUnzipFile(multipartFile, destinationFolderPathString, fileType);
		} else {
			final String finalFileName = uploadFile(multipartFile, destinationFolderPathString, fileType);
			finalFileNameList = Arrays.asList(finalFileName);
		}
		return finalFileNameList;
	}

	/**
	 * Method to upload a MultipartFile (from a HTML form) in the hard drive
	 */
	private static String uploadFile(final MultipartFile multipartFile, final String destinationFolderPathString, final FileType fileType) throws IOException {
		final Path finalFilePath = FileUtils.uploadFileInternal(multipartFile, destinationFolderPathString, fileType);
		final String finalFileName = finalFilePath.getFileName().toString();

		return finalFileName;
	}

	/**
	 * Method to upload and unzip a MultipartFile (from a HTML form) in the hard drive
	 */
	private static List<String> uploadAndUnzipFile(final MultipartFile multipartFile, final String destinationFolderPathString, final FileType fileType) throws IOException {
		final Path finalFilePath = FileUtils.uploadFileInternal(multipartFile, destinationFolderPathString, FileType.ZIP);
		final String finalFilePathString = finalFilePath.toAbsolutePath().toString();

		final String finalFolderName = FileNameUtils.getFinalFolderName("unzipped");

		final Path unzippedDestinationFolderPath = Path.of(destinationFolderPathString, finalFolderName);
		final String unzippedDestinationFolderPathString = unzippedDestinationFolderPath.toAbsolutePath().toString();

		FileUtils.unzipFile(finalFilePathString, unzippedDestinationFolderPathString);

		FileUtils.deletePathRecursively(finalFilePath);

		final List<String> finalFileNameList = fileType.getFolderAllowedFilesRecursive(unzippedDestinationFolderPath, destinationFolderPathString);

		FileUtils.deletePathRecursively(unzippedDestinationFolderPath);

		return finalFileNameList;
	}

	/**
	 * Method to upload a file in a given path (if its extension is allowed)
	 */
	private static Path uploadFileInternal(final MultipartFile multipartFile, final String destinationFolderPathString, final FileType fileType) throws IOException {
		final String originalFileName = multipartFile.getOriginalFilename();
		final String finalFileName = FileNameUtils.getFinalFileName(originalFileName, fileType);
		final Path finalFilePath = Path.of(destinationFolderPathString, finalFileName);

		try {
			Path destinationFolderPath = Path.of(destinationFolderPathString);
			destinationFolderPath = Files.createDirectories(destinationFolderPath);
			multipartFile.transferTo(finalFilePath);
		} catch(final IOException exception) {
			final String finalFilePathString = finalFilePath.toAbsolutePath().toString();
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("Error while trying to upload the file \"", finalFilePathString, "\". Exception: ", stackTrace));
			}
			throw new IOException(StringUtils.getStringJoined("Error when trying to upload the file \"", finalFilePathString, "\""), exception);
		}

		if(log.isDebugEnabled()) {
			final String finalFilePathString = finalFilePath.toAbsolutePath().toString();
			log.debug(StringUtils.getStringJoined("The file \"", finalFilePathString, "\" was uploaded"));
		}

		return finalFilePath;
	}

	/**
	 * Method to unzip a file (given its path) in the hard drive
	 */
	private static void unzipFile(final String originFilePathString, final String destinationFolderPathString) throws IOException {
		try(final ZipFile zipFile = new ZipFile(originFilePathString)) {
			if (zipFile.isEncrypted()) {
//				zipFile.setPassword(originFilePassword);
				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\": The zip file must not be encrypted with a password"));
				}
				throw new IllegalArgumentException(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\": The zip file must not be encrypted with a password"));
			}
			Path destinationFolderPath = Path.of(destinationFolderPathString);
			destinationFolderPath = Files.createDirectories(destinationFolderPath);
			zipFile.extractAll(destinationFolderPathString);
		} catch (IOException exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\". Exception: ", stackTrace));
			}
			throw new IOException(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\""), exception);
		}

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("The file \"", originFilePathString, "\" was unzipped"));
		}
	}

}