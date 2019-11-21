package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
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
	private static final String PLUGIN_FOLDER_PATH;

	static {
		@NotNull String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
		@NotNull String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
		@NotNull StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
		for (byte i = 1; i < parts.length - 1; i++) {
			pathBuilder.append(parts[i]).append(slash);
		}
		PLUGIN_FOLDER_PATH = pathBuilder.toString();
	}

	@NotNull
	public static List<File> getExistingFiles(@NotNull final Path path) {
		@NotNull ArrayList<File> tempFiles = new ArrayList<>();
		if (Objects.notNull(ConfigUtils.getStringList("File Extensions"))
				   .stream()
				   .anyMatch(SMFileUtils.getExtension(path)::equalsIgnoreCase)) {
			@NotNull File file = path.toFile();
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
		@NotNull ArrayList<File> files = new ArrayList<>();
		for (@NotNull File file : tempFiles) {
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