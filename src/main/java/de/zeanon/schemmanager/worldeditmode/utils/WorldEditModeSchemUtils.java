package de.zeanon.schemmanager.worldeditmode.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.Utils;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.utility.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class WorldEditModeSchemUtils {

	private static File schemFolder;
	private static Path schemFolderPath;

	public static Path getSchemPath() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return schemFolderPath;
		} else {
			try {
				initSchemPath();
				return schemFolderPath;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}

	public static void initSchemPath() throws FileNotFoundException {
		final @NotNull Path tempPath = Paths.get(Objects.notNull(Utils.getWeConfig().getStringUseArray("saving", "dir")));
		Utils.getWeConfig().clearData();
		if (tempPath.isAbsolute()) {
			schemFolderPath = tempPath.normalize();
			schemFolder = schemFolderPath.toFile();
			if (!schemFolder.exists() && !schemFolder.mkdirs()) {
				throw new FileNotFoundException();
			}
		} else {
			schemFolderPath = Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit"))
									 .getDataFolder().toPath().resolve(tempPath).normalize();
			schemFolder = schemFolderPath.toFile();
			if (!schemFolder.exists() && !schemFolder.mkdirs()) {
				throw new FileNotFoundException();
			}
		}
	}

	public static File getSchemFolder() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return schemFolder;
		} else {
			try {
				initSchemPath();
				return schemFolder;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}
}