package de.zeanon.schemmanager.utils.updateutils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUtils {

	private static final int BUFFER_SIZE = 8192;

	public static synchronized boolean writeToFile(final File file, final BufferedInputStream inputStream) {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
				return true;
			} else {
				final byte[] data = new byte[BUFFER_SIZE];
				int count;
				while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
					outputStream.write(data, 0, count);
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}