package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InternalFileUtils {

	@NotNull
	@Getter
	private static final String pluginFolderPath; //NOSONAR

	static {
		String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
		String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
		StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
		for (byte i = 1; i < parts.length - 1; i++) {
			pathBuilder.append(parts[i]).append(slash);
		}
		pluginFolderPath = pathBuilder.toString();
	}

	@NotNull
	public static List<File> getExistingFiles(@NotNull final Path path) {
		ArrayList<File> tempFiles = new ArrayList<>();
		if (Objects.notNull(ConfigUtils.getStringList("File Extensions"))
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
		Objects.notNull(ConfigUtils.getStringList("File Extensions"))
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

	@NotNull
	public static String deleteEmptyParent(@NotNull final File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) { //NOSONAR
			return deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}