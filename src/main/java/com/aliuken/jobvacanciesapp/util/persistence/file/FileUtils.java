package com.aliuken.jobvacanciesapp.util.persistence.file;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.FileType;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.service.AuthUserEntityQueryService;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.AuthUserQueryReport;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerServletUtils;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileUtils {
	private static final String FILE_RESOURCE_LOCATION_PREFIX = "file:";
	public static final String PDF_EXTENSION = ".pdf";
	public static final String ZIP_EXTENSION = ".zip";

	private FileUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	/**
	 * Method to get the resource location of a file path
	 */
	public static String getFileResourceLocation(final String filePathString) {
		final String resourceLocation = StringUtils.getStringJoined(FILE_RESOURCE_LOCATION_PREFIX, filePathString);
		return resourceLocation;
	}

	/**
	 * Method to store and download a query pdf file
	 */
	public static <T extends AbstractEntity> byte[] storeAndDownloadPdf(
			final PredefinedFilterDTO predefinedFilterDTO, final @NonNull TableSearchDTO tableSearchDTO,
			final @NonNull PageEntityEnum pageEntity, final Page<T> entityPage, final String destinationFolderPathString,
			final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);

		final String destinationFolderPathWithAuthUserId;
		if(sessionAuthUser != null) {
			final Long sessionAuthUserId = sessionAuthUser.getId();
			if(sessionAuthUserId != null) {
				final String sessionAuthUserIdString = sessionAuthUserId.toString();
				destinationFolderPathWithAuthUserId = StringUtils.getStringJoined(destinationFolderPathString, sessionAuthUserIdString, "/");
			} else {
				destinationFolderPathWithAuthUserId = null;
			}
		} else {
			destinationFolderPathWithAuthUserId = null;
		}

		byte[] pdfByteArray;
		if(destinationFolderPathWithAuthUserId != null) {
			final AuthUserEntityQueryService authUserEntityQueryService = BeanFactoryUtils.getBean(AuthUserEntityQueryService.class);

			final AuthUserEntityQuery authUserEntityQuery;
			try {
				final String requestUrl = ControllerServletUtils.getUrlFromHttpServletRequest(httpServletRequest);
				final String queryUrl = requestUrl.replaceFirst("/exportToPdf", Constants.EMPTY_STRING);
				authUserEntityQuery = new AuthUserEntityQuery(sessionAuthUser, predefinedFilterDTO, tableSearchDTO, pageEntity, queryUrl);
				authUserEntityQueryService.saveAndFlush(authUserEntityQuery);
			} catch(final Exception exception) {
				if(log.isErrorEnabled()) {
					final String stackTrace = ThrowableUtils.getStackTrace(exception);
					log.error(StringUtils.getStringJoined("An exception happened when trying to get the search criteria for exporting a query to pdf. Exception: ", stackTrace));
				}
				throw exception;
			}

			try {
				// Calculate the pdf content
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				AuthUserQueryReport.generatePdfDocument(byteArrayOutputStream, authUserEntityQuery, entityPage);
				pdfByteArray = byteArrayOutputStream.toByteArray();

				if(LogicalUtils.isNotNullNorEmpty(pdfByteArray)) {
					final String originalResultFileName = authUserEntityQuery.getOriginalResultFileName();

					// Store the pdf content in a file in the server file system
					final CustomMultipartFile customMultipartFile = new CustomMultipartFile("exportToPdfButton", originalResultFileName, null, pdfByteArray);
					List<String> finalResultFileNameList = uploadAndOptionallyUnzipFile(customMultipartFile, destinationFolderPathWithAuthUserId, FileType.ENTITY_QUERY);

					// Store the final pdf file name in the database
					final String finalResultFileName = (LogicalUtils.isNotNullNorEmpty(finalResultFileNameList)) ? finalResultFileNameList.get(0) : null;
					authUserEntityQuery.setFinalResultFileName(finalResultFileName);
					authUserEntityQueryService.saveAndFlush(authUserEntityQuery);

					// Write the pdf content in the httpServletResponse to download it
					try(final ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
						servletOutputStream.write(pdfByteArray);
						servletOutputStream.flush();
					}
				}
			} catch(final Exception exception) {
				if(log.isErrorEnabled()) {
					final String stackTrace = ThrowableUtils.getStackTrace(exception);
					log.error(StringUtils.getStringJoined("An exception happened when trying to store and download the pdf for the following search criteria: ", authUserEntityQuery.toString(), ". Exception: ", stackTrace));
				}
				pdfByteArray = new byte[0];
			}
		} else {
			pdfByteArray = new byte[0];
		}

		return pdfByteArray;
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
		Objects.requireNonNull(originalFileName, "originalFileName cannot be null");

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
		Objects.requireNonNull(originalFileName, "originalFileName cannot be null");

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
			if(zipFile.isEncrypted()) {
//				zipFile.setPassword(originFilePassword);
				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\": The zip file must not be encrypted with a password"));
				}
				throw new IllegalArgumentException(StringUtils.getStringJoined("Error when trying to unzip the file \"", originFilePathString, "\": The zip file must not be encrypted with a password"));
			}
			Path destinationFolderPath = Path.of(destinationFolderPathString);
			destinationFolderPath = Files.createDirectories(destinationFolderPath);
			zipFile.extractAll(destinationFolderPathString);
		} catch (final IOException exception) {
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