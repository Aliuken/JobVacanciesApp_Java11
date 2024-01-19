package com.aliuken.jobvacanciesapp.enumtype;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.FileNameUtils;

public enum FileType implements Serializable, DirectoryStream.Filter<Path> {
	CURRICULUM("pdf", "doc", "docx"),
	JOB_COMPANY_LOGO("jpg", "jpeg", "png"),
	ZIP("zip");

	@NotEmpty
	private final List<String> allowedLowerCaseFileExtensions;

	@NotNull
	private final String allowedLowerCaseFileExtensionsString;

	private FileType(final String... allowedFileExtensionsVararg) {
		if(LogicalUtils.isNullOrEmpty(allowedFileExtensionsVararg)) {
			throw new IllegalArgumentException("FileType allowedFileExtensions must not be null nor empty");
		}

		final List<String> allowedLowerCaseFileExtensions = new ArrayList<>();
		for(String allowedFileExtension : allowedFileExtensionsVararg) {
			if(allowedFileExtension != null) {
				allowedFileExtension = allowedFileExtension.toLowerCase();
				allowedLowerCaseFileExtensions.add(allowedFileExtension);
			}
		}

		this.allowedLowerCaseFileExtensions = allowedLowerCaseFileExtensions;
		this.allowedLowerCaseFileExtensionsString = StringUtils.getStringJoinedWithDelimiters(", ", null, null, allowedLowerCaseFileExtensions);
	}

	public String getAllowedLowerCaseFileExtensionsString() {
		return allowedLowerCaseFileExtensionsString;
	}

	@Override
	public boolean accept(final Path path) throws IOException {
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
		final boolean result = (lowerCaseFileExtension != null && allowedLowerCaseFileExtensions != null && allowedLowerCaseFileExtensions.contains(lowerCaseFileExtension));
		return result;
	}

	/**
	 * Method to check if the given extension is an allowed extension by throwing exceptions
	 */
	public void checkAllowedFileExtension(final String lowerCaseFileExtension) {
		if(lowerCaseFileExtension == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The file does not have an extension. The allowed file extensions are ", allowedLowerCaseFileExtensionsString, " and zip."));
		}

		if(allowedLowerCaseFileExtensions == null || !allowedLowerCaseFileExtensions.contains(lowerCaseFileExtension)) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("File extension ", lowerCaseFileExtension, " not allowed. The allowed file extensions are ", allowedLowerCaseFileExtensionsString, " and zip."));
		}
	}

	/**
	 * Recursive method to get the allowed files in a folder path
	 */
	public List<String> getFolderAllowedFilesRecursive(final Path originFolderPath, final String destinationFolderPathString) throws IOException {
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
