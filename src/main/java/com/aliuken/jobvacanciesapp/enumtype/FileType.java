package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileNameUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public enum FileType implements Serializable, DirectoryStream.Filter<Path> {
	COMPANY_LOGO("jpg", "jpeg", "png"),
	ENTITY_QUERY("pdf"),
	USER_CURRICULUM("pdf", "doc", "docx"),
	ZIP("zip");

	private final @NonNull List<String> allowedLowerCaseFileExtensions;

	@Getter
	private final @NonNull String allowedLowerCaseFileExtensionsString;

	private FileType(final @NonNull String @NonNull ... allowedFileExtensionsVarargs) {
		if(allowedFileExtensionsVarargs.length == 0) {
			throw new IllegalArgumentException("FileType allowedFileExtensions must not be empty");
		}

		final List<String> allowedLowerCaseFileExtensions = new ArrayList<>();
		for(final String allowedFileExtension : allowedFileExtensionsVarargs) {
			final String allowedLowerCaseFileExtension = allowedFileExtension.toLowerCase();
			allowedLowerCaseFileExtensions.add(allowedLowerCaseFileExtension);
		}

		this.allowedLowerCaseFileExtensions = allowedLowerCaseFileExtensions;
		this.allowedLowerCaseFileExtensionsString = StringUtils.getStringJoinedWithDelimiters(", ", null, null, allowedLowerCaseFileExtensions);
	}

	@Override
	public boolean accept(final @NonNull Path path) throws IOException {
		if(Files.isDirectory(path)) {
			return true;
		} else if(Files.isRegularFile(path)) {
			final String fileName = path.getFileName().toString();
			final String lowerCaseFileExtension = FileNameUtils.getLowerCaseFileExtension(fileName);
			final boolean isAllowedFileExtension = this.isAllowedFileExtension(lowerCaseFileExtension);
			return isAllowedFileExtension;
		} else {
			return false;
		}
	}

	/**
	 * Method to check if the given extension is an allowed extension without throwing exceptions
	 */
	public boolean isAllowedFileExtension(final String lowerCaseFileExtension) {
		final boolean result = (lowerCaseFileExtension != null && allowedLowerCaseFileExtensions.contains(lowerCaseFileExtension));
		return result;
	}

	/**
	 * Method to check if the given extension is an allowed extension by throwing exceptions
	 */
	public void checkAllowedFileExtension(final String lowerCaseFileExtension) {
		if(lowerCaseFileExtension == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The file does not have an extension. The allowed file extensions are ", allowedLowerCaseFileExtensionsString, " and zip."));
		}

		if(!allowedLowerCaseFileExtensions.contains(lowerCaseFileExtension)) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("File extension ", lowerCaseFileExtension, " not allowed. The allowed file extensions are ", allowedLowerCaseFileExtensionsString, " and zip."));
		}
	}

	/**
	 * Recursive method to get the allowed files in a folder path
	 */
	public @NonNull List<String> getFolderAllowedFilesRecursive(final @NonNull Path originFolderPath, final @NonNull String destinationFolderPathString) throws IOException {
		final List<String> finalFileNameList = new ArrayList<>();
		try(final DirectoryStream<Path> pathDirectoryStream = Files.newDirectoryStream(originFolderPath, this)) {
			for(final Path path : pathDirectoryStream) {
				if(Files.isDirectory(path)) {
					final List<String> folderAllowedFiles = this.getFolderAllowedFilesRecursive(path, destinationFolderPathString);
					finalFileNameList.addAll(folderAllowedFiles);
				} else {
					final String fileName = path.getFileName().toString();
					final String finalFileName = FileNameUtils.getFinalFileName(fileName, this);
					final Path destinationFilePath = Path.of(destinationFolderPathString, finalFileName);
					Files.move(path, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
					finalFileNameList.add(finalFileName);
				}
			}
		}
		return finalFileNameList;
	}
}
