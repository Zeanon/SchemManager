package de.zeanon.schemmanager.fawemode.utils;

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
public class FastAsyncWorldEditModeSchemUtils {

	private @Nullable
	File schemFolder;
	private @Nullable
	Path schemFolderPath;

	public @Nullable
	Path getSchemPath() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return FastAsyncWorldEditModeSchemUtils.schemFolderPath;
		} else {
			try {
				FastAsyncWorldEditModeSchemUtils.initSchemPath();
				return FastAsyncWorldEditModeSchemUtils.schemFolderPath;
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
			FastAsyncWorldEditModeSchemUtils.schemFolderPath = tempPath.normalize();
		} else {
			FastAsyncWorldEditModeSchemUtils.schemFolderPath = Objects.notNull(SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit"))
																	  .getDataFolder().toPath().resolve(tempPath).normalize();
		}
		FastAsyncWorldEditModeSchemUtils.schemFolder = FastAsyncWorldEditModeSchemUtils.schemFolderPath.toFile();
		if (!FastAsyncWorldEditModeSchemUtils.schemFolder.exists() && !FastAsyncWorldEditModeSchemUtils.schemFolder.mkdirs()) {
			throw new FileNotFoundException();
		}
	}

	public @Nullable
	File getSchemFolder() {
		if (!Objects.notNull(Utils.getWeConfig()).hasChanged()) {
			return FastAsyncWorldEditModeSchemUtils.schemFolder;
		} else {
			try {
				FastAsyncWorldEditModeSchemUtils.initSchemPath();
				return FastAsyncWorldEditModeSchemUtils.schemFolder;
			} catch (@NotNull FileNotFoundException | ObjectNullException e) {
				System.err.println("Could not initialize Schematic folder");
				e.printStackTrace();
				throw new RuntimeIOException();
			}
		}
	}
}