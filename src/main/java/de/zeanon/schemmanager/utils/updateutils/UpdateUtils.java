package de.zeanon.schemmanager.utils.updateutils;

import java.io.*;
import java.nio.file.Files;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UpdateUtils {

	static synchronized boolean writeToFile(final File file, final BufferedInputStream inputStream) {
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
				return true;
			} else {
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
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