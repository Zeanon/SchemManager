package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.InitMode;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class ConfigUtils {

	/**
	 * get an int from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	public int getInt(final @NotNull String key) {
		try {
			return Objects.notNull(InitMode.getConfig()).getByteUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (int) ConfigUtils.getDefaultValue(key);
		}
	}

	/**
	 * get a boolean from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	public boolean getBoolean(final @NotNull String key) {
		try {
			return Objects.notNull(InitMode.getConfig()).getBooleanUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (boolean) ConfigUtils.getDefaultValue(key);
		}
	}

	/**
	 * get a StringList from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	@Nullable
	public List<String> getStringList(final @NotNull String key) {
		try {
			return Objects.notNull(InitMode.getConfig()).getListUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			//noinspection unchecked
			return (List<String>) ConfigUtils.getDefaultValue(key);
		}
	}

	/**
	 * Get the default values of the different Config keys.
	 *
	 * @param key the Config key.
	 *
	 * @return the default value.
	 */
	private @NotNull Object getDefaultValue(final @NotNull String key) {
		switch (key) {
			case "Space Lists":
			case "Delete empty Folders":
			case "Save Function Override":
			case "Automatic Reload":
			case "Stoplag Override":
				return true;
			case "Listmax":
				return 10;
			case "File Extensions":
				return Arrays.asList("schem", "schematic");
			case "Plugin Version":
				return SchemManager.getInstance().getDescription().getVersion();
			default:
				return new Object();
		}
	}
}