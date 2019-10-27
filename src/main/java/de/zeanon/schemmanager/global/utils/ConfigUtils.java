package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtils {

	/**
	 * get an int from the config.
	 *
	 * @param key the Config key.
	 * @return value.
	 */
	public static int getInt(final String key) {
		if (SchemManager.getLocalConfig().hasKey(key)) {
			return SchemManager.getLocalConfig().getInt(key);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig(true);
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (int) getDefaultValue(key);
		}
	}

	/**
	 * Get the default values of the different Config keys.
	 *
	 * @param key the Config key.
	 * @return the default value.
	 */
	private static Object getDefaultValue(final String key) {
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
				return null;
		}
	}

	/**
	 * get a boolean from the config.
	 *
	 * @param key the Config key.
	 * @return value.
	 */
	public static boolean getBoolean(final String key) {
		if (SchemManager.getLocalConfig().hasKey(key)) {
			return SchemManager.getLocalConfig().getBoolean(key);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig(true);
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (boolean) getDefaultValue(key);
		}
	}

	/**
	 * get a StringList from the config.
	 *
	 * @param key the Config key.
	 * @return value.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getStringList(final String key) {
		if (SchemManager.getLocalConfig().hasKey(key)) {
			return SchemManager.getLocalConfig().getStringList(key);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig(true);
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (List<String>) getDefaultValue(key);
		}
	}
}