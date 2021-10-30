package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.init.RunningMode;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class SchemUtils {

	public @NotNull Path getSchemPath() {
		return SchemUtils.getSchemFolder().toPath();
	}

	public @NotNull File getSchemFolder() {
		return RunningMode.getWorldEditPlugin().getWorldEdit().getWorkingDirectoryFile(Objects.notNull(RunningMode.getWorldEditPlugin()).getWorldEdit().getConfiguration().saveDir);
	}
}