package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.InitMode;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SchemUtils {

	private @Nullable
	File schemFolder;
	private @Nullable
	Path schemFolderPath;

	public @Nullable
	Path getSchemPath() {
		if (!Objects.notNull(InitMode.getWeConfig()).hasChanged()) {
			return SchemUtils.schemFolderPath;
		} else {
			try {
				SchemUtils.initSchemPath();
				return SchemUtils.schemFolderPath;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}

	public void initSchemPath() throws FileNotFoundException {
		final @NotNull Path tempPath = Paths.get(Objects.notNull(InitMode.getWeConfig().getStringUseArray("saving", "dir")));
		InitMode.getWeConfig().clearData();
		if (tempPath.isAbsolute()) {
			SchemUtils.schemFolderPath = tempPath.normalize();
		} else {
			SchemUtils.schemFolderPath = Objects.notNull(SchemManager.getPluginManager().getPlugin(InitMode.getWorldEditPluginName()))
												.getDataFolder().toPath().resolve(tempPath).normalize();
		}
		SchemUtils.schemFolder = SchemUtils.schemFolderPath.toFile();
		if (!SchemUtils.schemFolder.exists() && !SchemUtils.schemFolder.mkdirs()) {
			throw new FileNotFoundException();
		}
	}


	public @Nullable File getSchemFolder() {
		if (!Objects.notNull(InitMode.getWeConfig()).hasChanged()) {
			return SchemUtils.schemFolder;
		} else {
			try {
				SchemUtils.initSchemPath();
				return SchemUtils.schemFolder;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}
}