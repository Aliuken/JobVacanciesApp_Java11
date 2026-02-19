package com.aliuken.jobvacanciesapp.util.persistence.file;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.FileType;
import com.aliuken.jobvacanciesapp.enumtype.RandomCharactersEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.security.RandomUtils;
import org.jspecify.annotations.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameUtils {
	private static final String EXTENSION_PATTERN = "^(.+)\\.([^.]+$)";

	private FileNameUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	/**
	 * Method to get the final name of a given folder name
	 */
	public static @NonNull String getFinalFolderName(@NonNull String folderName) {
		final String randomAlphanumeric = RandomUtils.getRandomString(RandomCharactersEnum.ALPHANUMERIC_CHARACTERS, 12);

		folderName = FileNameUtils.getPathNameWithoutSpaces(folderName);
		folderName = StringUtils.getStringJoined(folderName, Constants.HYPHEN, randomAlphanumeric);
		return folderName;
	}

	/**
	 * Method to get the final name of a given file name (if it has an allowed extension)
	 */
	public static @NonNull String getFinalFileName(String fileName, final @NonNull FileType fileType) {
		final String lowerCaseFileExtension = FileNameUtils.getLowerCaseFileExtension(fileName);
		fileType.checkAllowedFileExtension(lowerCaseFileExtension);
		final String randomAlphanumeric = RandomUtils.getRandomString(RandomCharactersEnum.ALPHANUMERIC_CHARACTERS, 12);

		fileName = fileName.substring(0, fileName.length() - (lowerCaseFileExtension.length() + 1));
		fileName = FileNameUtils.getPathNameWithoutSpaces(fileName);
		fileName = StringUtils.getStringJoined(fileName, Constants.HYPHEN, randomAlphanumeric, Constants.DOT, lowerCaseFileExtension);
		return fileName;
	}

	/**
	 * Method to replace spaces with hyphens in a file/folder name
	 */
	public static @NonNull String getPathNameWithoutSpaces(@NonNull String pathName) {
		pathName = pathName.replace(Constants.SPACE, Constants.HYPHEN);
		while(pathName.contains("--")) {
			pathName = pathName.replace("--", Constants.HYPHEN);
		}
		return pathName;
	}

	/**
	 * Method to get the extension of a given file name
	 */
	public static String getLowerCaseFileExtension(final String fileName) {
		if(LogicalUtils.isNullOrEmptyString(fileName)) {
			return null;
		}

		final Pattern pattern = Pattern.compile(FileNameUtils.EXTENSION_PATTERN);
		final Matcher matcher = pattern.matcher(fileName);

		String extension;
		if(matcher.find()) {
			extension = matcher.group(2);
		} else {
			return null;
		}

		if(extension != null) {
			extension = extension.toLowerCase();
		}

		return extension;
	}
}