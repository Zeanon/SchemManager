package de.zeanon.schemmanager.worldeditmode.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.Utils;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditModeSchemUtils {

	private static File schemFolder;
	private static Path schemFolderPath;

	public static Path getSchemPath() {
		if (!Utils.getWeConfig().hasChanged()) {
			return schemFolderPath;
		} else {
			try {
				initSchemPath();
				return schemFolderPath;
			} catch (FileNotFoundException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	public static void initSchemPath() throws FileNotFoundException {
		Path tempPath = Paths.get(Utils.getWeConfig().getString("saving.dir"));
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
		if (!Utils.getWeConfig().hasChanged()) {
			return schemFolder;
		} else {
			try {
				initSchemPath();
				return schemFolder;
			} catch (FileNotFoundException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}
}