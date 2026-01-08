package com.aliuken.jobvacanciesapp.util.persistence.file;

import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomMultipartFile implements MultipartFile {

	@Getter
	private final @NonNull String name;

	@Getter
	private final String originalFilename;

	@Getter
	private final String contentType;

	private final byte[] content;

	public CustomMultipartFile(final @NonNull String name, final String originalFilename, final String contentType, final byte[] content) {
		this.name = name;
		this.originalFilename = originalFilename;
		this.contentType = contentType;
		if(content != null) {
			this.content = content;
		} else {
			this.content = new byte[0];
		}
	}

	@Override
	public boolean isEmpty() {
		final boolean result = LogicalUtils.isNullOrEmpty(content);
		return result;
	}

	@Override
	public long getSize() {
		final int length = content.length;
		return length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return content;
	}

	@Override
	public @NonNull InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(content);
	}

	@Override
	public void transferTo(final @NonNull File destination) throws IOException, IllegalStateException {
		final Path path = Paths.get(destination.getPath());
		Files.write(path, content);
	}
}