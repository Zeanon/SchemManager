package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.init.RunningMode;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SchemUtils {

	private @Nullable
	File schemFolder;
	private @Nullable
	Path schemFolderPath;

	public @NotNull Path getSchemPath() {
		return SchemUtils.getSchemFolder().toPath();
	}

	public @NotNull File getSchemFolder() {
		return Objects.notNull(RunningMode.getWorldEditPlugin()).getWorldEdit().getWorkingDirectoryFile(Objects.notNull(RunningMode.getWorldEditPlugin()).getWorldEdit().getConfiguration().saveDir);
	}
}