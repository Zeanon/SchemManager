package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.storagemanagercore.internal.base.settings.Reload;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.thunderfilemanager.ThunderFileManager;
import de.zeanon.thunderfilemanager.internal.base.exceptions.ThunderException;
import de.zeanon.thunderfilemanager.internal.files.config.ThunderConfig;
import de.zeanon.thunderfilemanager.internal.utility.parser.ThunderFileParser;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class ConfigUtils {

	@Getter(onMethod_ = {@NotNull})
	private ThunderConfig config;


	public void loadConfigs() {
		@Nullable Throwable cause = null;
		try {
			ConfigUtils.config = ThunderFileManager.thunderConfig(SchemManager.getInstance().getDataFolder(), "config")
												   .fromResource("resources/config.tf")
												   .reloadSetting(Reload.INTELLIGENT)
												   .commentSetting(Comment.PRESERVE)
												   .concurrentData(true)
												   .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' loaded.");
		} catch (final @NotNull RuntimeIOException | FileParseException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be loaded.");
			e.printStackTrace();
			cause = e;
		}

		if (cause != null) {
			throw new RuntimeIOException(cause);
		}
	}

	public void initConfigs() {
		if (!ConfigUtils.getConfig().hasKeyUseArray("Plugin Version")
			|| !Objects.notNull(ConfigUtils.getConfig().getStringUseArray("Plugin Version"))
					   .equals(SchemManager.getInstance().getDescription().getVersion())) {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Updating Configs...");
			Update.checkConfigUpdate();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are updated successfully.");
		}
	}

	/**
	 * get a StringList from the config.
	 *
	 * @param key the Config key.
	 *
	 * @return value.
	 */
	public @NotNull List<String> getStringList(final @NotNull String key) {
		return ConfigUtils.get(key);
	}

	public boolean getBoolean(final @NotNull String... key) {
		return ConfigUtils.get(key);
	}

	public int getInt(final @NotNull String... key) {
		return ConfigUtils.get(key);
	}

	public @NotNull <V> V get(final @NotNull String... key) {
		final @Nullable V result = ConfigUtils.getConfig().getUseArray(key);
		if (result != null) {
			return result;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				Update.updateConfig();
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
		return ConfigUtils.getDefaultValue(key);
	}

	/**
	 * Get the default values of the different Config keys.
	 *
	 * @param key the Config key.
	 *
	 * @return the default value.
	 */
	public @NotNull <V> V getDefaultValue(final @NotNull String... key) {
		try {
			return Objects.notNull(Objects.toDef(ThunderFileParser.readDataAsFileData(BaseFileUtils.createNewInputStreamFromResource("resources/config.tf"),
																					  ConfigUtils.getConfig().collectionsProvider(),
																					  ConfigUtils.getConfig().getCommentSetting(),
																					  ConfigUtils.getConfig().getBufferSize()).getUseArray(key)));
		} catch (ThunderException e) {
			e.printStackTrace();
		}
		//noinspection unchecked
		return (V) new Object();
	}
}