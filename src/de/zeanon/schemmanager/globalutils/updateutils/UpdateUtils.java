package de.zeanon.schemmanager.globalutils.updateutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdateUtils {

    @SuppressWarnings("Duplicates")
    public static boolean writeToFile(File file, BufferedInputStream inputStream) {
        try {
            FileOutputStream outputStream = null;
            try {
                if (!file.exists()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.ATOMIC_MOVE);
                } else {
                    outputStream = new FileOutputStream(file);
                    final byte[] data = new byte[5120];
                    int count;
                    while ((count = inputStream.read(data, 0, 5120)) != -1) {
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