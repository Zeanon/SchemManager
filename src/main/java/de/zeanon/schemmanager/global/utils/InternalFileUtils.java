package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storage.internal.utils.SMFileUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InternalFileUtils {

	@Getter
	private static final String pluginFolderPath;

	static {
		String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
		String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
		StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
		for (byte i = 1; i < parts.length - 1; i++) {
			pathBuilder.append(parts[i]).append(slash);
		}
		pluginFolderPath = pathBuilder.toString();
	}

	public static List<File> getExistingFiles(final Path path) {
		ArrayList<File> tempFiles = new ArrayList<>();
		if (ConfigUtils.getStringList("File Extensions")
					   .stream()
					   .anyMatch(SMFileUtils.getExtension(path)::equalsIgnoreCase)) {
			File file = path.toFile();
			if (file.exists() && !file.isDirectory()) {
				tempFiles.add(file);
				return tempFiles;
			} else {
				return tempFiles;
			}
		}
		ConfigUtils.getStringList("File Extensions")
				   .iterator()
				   .forEachRemaining(extension -> tempFiles.add(new File(path + "." + extension)));
		ArrayList<File> files = new ArrayList<>();
		for (File file : tempFiles) {
			if (file.exists() && !file.isDirectory()) {
				files.add(file);
			}
		}
		return files;
	}

	public static String deleteEmptyParent(final File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) {
			return deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}