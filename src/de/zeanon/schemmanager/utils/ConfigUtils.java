package de.zeanon.schemmanager.utils;

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
	 * @param key the yaml key.
	 * @return value.
	 */
	public static int getInt(final String key) {
		if (SchemManager.config.hasKey(key)) {
			return SchemManager.config.getInt(key);
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
	 * get a boolean from the config.
	 *
	 * @param key the yaml key.
	 * @return value.
	 */
	public static boolean getBoolean(final String key) {
		if (SchemManager.config.hasKey(key)) {
			return SchemManager.config.getBoolean(key);
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
	 * @param key the yaml key.
	 * @return value.
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getStringList(final String key) {
		if (SchemManager.config.hasKey(key)) {
			return SchemManager.config.getStringList(key);
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

	/**
	 * Get the default values of the different Config keys.
	 *
	 * @param key the Config key.
	 * @return the default value.
	 */
	private static Object getDefaultValue(final String key) {
		switch (key) {
			case "Space Lists":
				return true;
			case "Delete empty Folders":
				return true;
			case "Listmax":
				return 10;
			case "Save Function Override":
				return true;
			case "Stoplag Override":
				return true;
			case "Automatic Reload":
				return true;
			case "File Extensions":
				return Arrays.asList("schem", "schematic");
			case "Plugin Version":
				return SchemManager.getInstance().getDescription().getVersion();
			default:
				return null;
		}
	}
}