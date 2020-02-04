package de.zeanon.schemmanager.worldeditmode.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.Utils;
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
public class WorldEditModeSchemUtils {

	private @Nullable
	File schemFolder;
	private @Nullable
	Path schemFolderPath;

	public @Nullable
	Path getSchemPath() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return WorldEditModeSchemUtils.schemFolderPath;
		} else {
			try {
				WorldEditModeSchemUtils.initSchemPath();
				return WorldEditModeSchemUtils.schemFolderPath;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}

	public void initSchemPath() throws FileNotFoundException {
		final @NotNull Path tempPath = Paths.get(Objects.notNull(Utils.getWeConfig().getStringUseArray("saving", "dir")));
		Utils.getWeConfig().clearData();
		if (tempPath.isAbsolute()) {
			WorldEditModeSchemUtils.schemFolderPath = tempPath.normalize();
		} else {
			WorldEditModeSchemUtils.schemFolderPath = Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit"))
															 .getDataFolder().toPath().resolve(tempPath).normalize();
		}
		WorldEditModeSchemUtils.schemFolder = WorldEditModeSchemUtils.schemFolderPath.toFile();
		if (!WorldEditModeSchemUtils.schemFolder.exists() && !WorldEditModeSchemUtils.schemFolder.mkdirs()) {
			throw new FileNotFoundException();
		}
	}

	public @Nullable
	File getSchemFolder() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return WorldEditModeSchemUtils.schemFolder;
		} else {
			try {
				WorldEditModeSchemUtils.initSchemPath();
				return WorldEditModeSchemUtils.schemFolder;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}
}