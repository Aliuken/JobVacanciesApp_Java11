package com.aliuken.jobvacanciesapp.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.aliuken.jobvacanciesapp.model.dto.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
	private static final String EXTENSION_PATTERN = "(?<!^)\\.([^.]*$)";

	public static String getFileExtension(String filename) {
		if (filename == null || filename.isEmpty()) {
			return null;
		}

		final Pattern pattern = Pattern.compile(EXTENSION_PATTERN);
		final Matcher matcher = pattern.matcher(filename);

		final String extension;
		if (matcher.find()) {
			extension = matcher.group(1);
		} else {
			extension = null;
		}

		return extension;
	}

	/**
	 * Metodo que guarda un archivo a traves de un formulario HTML al disco duro.
	 */
	public static String saveFile(final MultipartFile multipartFile, final String path, final FileType fileType) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return null;
		}

		if(fileType == null) {
			throw new IllegalArgumentException("FileType must not be null");
		}

		final String originalFilename = multipartFile.getOriginalFilename();
		final List<String> allowedExtensions = fileType.getAllowedExtensions();

		final String finalFilename = FileUtils.getFinalFilename(originalFilename, allowedExtensions);
		final Path finalFilePath = FileUtils.getFinalFilePath(path, finalFilename);

		try {
			multipartFile.transferTo(finalFilePath);
		} catch (IOException e) {
			if(log.isErrorEnabled()) {
				log.error(StringUtils.getStringJoined("Error while trying to save the file ", finalFilePath.toAbsolutePath().toString(), ": ", e.getMessage()));
			}
			return null;
		}
		
		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("File ", finalFilePath.toAbsolutePath().toString(), " was saved"));
		}

		return finalFilename;
	}

	private static String getFinalFilename(String originalFilename, final List<String> allowedExtensions) {
		String fileExtension = FileUtils.getFileExtension(originalFilename);
		if (fileExtension == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("File extension ", fileExtension, " not allowed. The allowed file extensions are ", allowedExtensions.toString(), "."));
		}

		fileExtension = fileExtension.toLowerCase();
		if(!allowedExtensions.contains(fileExtension)) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("File extension ", fileExtension, " not allowed. The allowed file extensions are ", allowedExtensions.toString(), "."));
		}

		originalFilename = originalFilename.substring(0, originalFilename.length() - (fileExtension.length() + 1));
		originalFilename = originalFilename.replace(" ", "-");

		final String randomAlphaNumeric = StringUtils.getRandomAlphaNumeric(12);
		final String finalFilename = StringUtils.getStringJoined(originalFilename, "-", randomAlphaNumeric, ".", fileExtension);

		return finalFilename;
	}

	private static Path getFinalFilePath(final String path, final String finalFilename) {
		final String finalFilePathname = StringUtils.getStringJoined(path, finalFilename);
		Path finalFilePath = Paths.get(finalFilePathname);

		return finalFilePath;
	}

}