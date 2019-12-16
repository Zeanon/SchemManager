package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
@SuppressWarnings("unused")
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
	public static List<File> getExistingFiles(final @NotNull Path path) {
		@NotNull List<File> tempFiles = new GapList<>();
		if (Objects.notNull(ConfigUtils.getStringList("File Extensions"))
				   .stream()
				   .anyMatch(BaseFileUtils.getExtension(path)::equalsIgnoreCase)) {
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
		final @NotNull List<File> files = new GapList<>();
		for (final @NotNull File file : tempFiles) {
			if (file.exists() && !file.isDirectory()) {
				files.add(file);
			}
		}
		return files;
	}

	@NotNull
	public static String deleteEmptyParent(final @NotNull File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) { //NOSONAR
			return InternalFileUtils.deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}