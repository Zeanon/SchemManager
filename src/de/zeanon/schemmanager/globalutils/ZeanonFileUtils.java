package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ZeanonFileUtils {

    private static String pluginFolderPath;

    /**
     * initiates the class
     */
    public static void initiate() {
        String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
        String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
        StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
        for (int i = 1; i < parts.length - 1; i++) {
            pathBuilder.append(parts[i]).append(slash);
        }
        pluginFolderPath = pathBuilder.toString();
    }

    static String getPluginFolderPath() {
        return pluginFolderPath;
    }

    /**
     * @param folder thefolder to look into
     * @param deep   deepSearch
     * @return the files of the folder that are directorys
     */
    public static ArrayList<File> getFolders(File folder, Boolean deep) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                files.add(file);
                if (deep) {
                    files.addAll(getFolders(file, true));
                }
            }
        }
        return files;
    }


    public static ArrayList<File> getExistingFiles(Path path) {
        ArrayList<File> tempFiles = new ArrayList<>();
        if (ConfigUtils.getStringList("File Extensions").stream().anyMatch(getExtension(path.toString())::equalsIgnoreCase)) {
            File file = path.toFile();
            if (file.exists() && !file.isDirectory()) {
                return new ArrayList<>(Collections.singletonList(file));
            }
        }
        ConfigUtils.getStringList("File Extensions").iterator().forEachRemaining(extension -> tempFiles.add(new File(path + "." + extension)));
        ArrayList<File> files = new ArrayList<>();
        for (File file : tempFiles) {
            if (file.exists() && !file.isDirectory()) {
                files.add(file);
            }
        }
        return files;
    }


    public static String removeExtension(String path) {
        return path.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(String path) {
        return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
    }

    public static String deleteEmptyParent(File file) {
        if (file.getParentFile().delete()) {
            return deleteEmptyParent(file.getParentFile());
        }
        return file.getName();
    }
}
