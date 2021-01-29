package de.zeanon.schemmanager.plugin.utils;

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

	@Getter
	private final @NotNull String PLUGIN_FOLDER_PATH;

	static {
		final @NotNull String slash = SchemManager.getInstance().getDataFolder().getAbsolutePath().contains("\\") ? "\\\\" : "/";
		final @NotNull String[] parts = SchemManager.getInstance().getDataFolder().getAbsolutePath().split(slash);
		final @NotNull StringBuilder pathBuilder = new StringBuilder(parts[0] + slash);
		for (int i = 1; i < parts.length - 1; i++) {
			pathBuilder.append(parts[i]).append(slash);
		}
		PLUGIN_FOLDER_PATH = pathBuilder.toString();
	}

	public @NotNull List<File> getExistingFiles(final @NotNull Path path) {
		final @NotNull List<File> tempFiles = new GapList<>();
		if (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(path))) {
			final @NotNull File file = path.toFile();
			if (file.exists() && !file.isDirectory()) {
				tempFiles.add(file);
			}
			return tempFiles;
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

	public @NotNull String deleteEmptyParent(final @NotNull File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) { //NOSONAR
			return InternalFileUtils.deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}