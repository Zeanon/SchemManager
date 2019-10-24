package de.zeanon.schemmanager.utils;

import de.zeanon.schemmanager.SchemManager;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	private static final String pluginFolderPath;

	static {
		String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
		String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
		StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
		for (int i = 1; i < parts.length - 1; i++) {
			pathBuilder.append(parts[i]).append(slash);
		}
		pluginFolderPath = pathBuilder.toString();
	}

	public static String getPluginFolderPath() {
		return pluginFolderPath;
	}

	/**
	 * @param folder the folder to look into
	 * @param deep   deepSearch
	 * @return the files of the folder that are directories
	 */
	public static ArrayList<File> getFolders(final File folder, final Boolean deep) {
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


	public static ArrayList<File> getExistingFiles(final Path path) {
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


	public static String removeExtension(final String path) {
		return path.replaceFirst("[.][^.]+$", "");
	}

	public static String getExtension(final String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public static String deleteEmptyParent(final File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) {
			return deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}