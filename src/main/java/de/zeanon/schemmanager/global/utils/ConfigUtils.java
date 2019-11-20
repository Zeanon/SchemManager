package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtils {

	/**
	 * get an int from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	public static byte getByte(@NotNull final String key) {
		try {
			return Objects.notNull(Utils.getConfig()).getByteUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (byte) getDefaultValue(key);
		}
	}

	/**
	 * get a boolean from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	public static boolean getBoolean(@NotNull final String key) {
		try {
			return Objects.notNull(Utils.getConfig()).getBooleanUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
				}
			}.runTaskAsynchronously(SchemManager.getInstance());
			return (boolean) getDefaultValue(key);
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
	public static List<String> getStringList(@NotNull final String key) {
		try {
			return Objects.notNull(Utils.getConfig()).getStringListUseArray(key);
		} catch (ObjectNullException e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Update.updateConfig();
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
	 *
	 * @return the default value.
	 */
	@Nullable
	private static Object getDefaultValue(@NotNull final String key) {
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