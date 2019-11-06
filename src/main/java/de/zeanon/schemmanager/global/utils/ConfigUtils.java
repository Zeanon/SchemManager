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
	public static byte getByte(final String key) {
		try {
			return Utils.getConfig().getByte(key);
		} catch (IllegalStateException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig(true);
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (byte) getDefaultValue(key);
		}
	}

	/**
	 * get a boolean from the config.
	 *
	 * @param key the Config key.
	 * @return value.
	 */
	public static boolean getBoolean(final String key) {
		try {
			return Utils.getConfig().getBoolean(key);
		} catch (IllegalStateException e) {
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
	public static List<String> getStringList(final String key) {
		try {
			return Utils.getConfig().getStringList(key);
		} catch (IllegalStateException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig(true);
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			//noinspection unchecked
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
}