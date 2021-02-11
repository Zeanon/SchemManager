package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
		if (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(path))) {
			final @NotNull File file = path.toFile();
			return file.exists() && !file.isDirectory() ? Collections.singletonList(file) : Collections.emptyList();
		}
		return Objects.notNull(ConfigUtils.getStringList("File Extensions"))
					  .stream()
					  .map(extension -> new File(path + "." + extension))
					  .filter(tempFile -> tempFile.exists() && !tempFile.isDirectory())
					  .collect(Collectors.toList());
	}

	public @NotNull String deleteEmptyParent(final @NotNull File file) {
		if (file.getAbsoluteFile().getParentFile().delete()) { //NOSONAR
			return InternalFileUtils.deleteEmptyParent(file.getAbsoluteFile().getParentFile());
		}
		return file.getName();
	}
}