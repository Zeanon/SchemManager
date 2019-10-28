package de.zeanon.schemmanager.worldeditversion.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditVersionSchemUtils {

	private static WorldEditPlugin we;
	private static File schemFolder;
	private static Path schemFolderPath;

	public static void initWorldEditPlugin() {
		we = (WorldEditPlugin) SchemManager.getPluginManager().getPlugin("WorldEdit");
	}

	public static WorldEditPlugin getWorldEditPlugin() {
		return we;
	}

	public static Path getSchemPath() {
		if (!SchemManager.getWeConfig().hasChanged()) {
			return schemFolderPath;
		} else {
			try {
				initSchemPath();
				return schemFolderPath;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static void initSchemPath() throws FileNotFoundException {
		Path tempPath = Paths.get(SchemManager.getWeConfig().getString("saving.dir"));
		SchemManager.getWeConfig().clearData();
		if (tempPath.isAbsolute()) {
			schemFolderPath = tempPath.normalize();
			schemFolder = schemFolderPath.toFile();
			if (!schemFolder.exists()) {
				if (!schemFolder.mkdirs()) {
					throw new FileNotFoundException();
				}
			}
		} else {
			schemFolderPath = Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder().toPath().resolve(tempPath).normalize();
			schemFolder = schemFolderPath.toFile();
			if (!schemFolder.exists()) {
				if (!schemFolder.mkdirs()) {
					throw new FileNotFoundException();
				}
			}
		}
	}

	public static File getSchemFolder() {
		return schemFolder;
	}
}