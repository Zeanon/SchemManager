package de.zeanon.schemmanager.utils.updateutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUtils {

    private static final int BUFFER_SIZE = 8192;

    @SuppressWarnings("Duplicates")
    public static boolean writeToFile(final File file, final BufferedInputStream inputStream) {
        try {
            FileOutputStream outputStream = null;
            try {
                if (!file.exists()) {
                    Files.copy(inputStream, file.toPath());
                } else {
                    outputStream = new FileOutputStream(file);
                    final byte[] data = new byte[BUFFER_SIZE];
                    int count;
                    while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}