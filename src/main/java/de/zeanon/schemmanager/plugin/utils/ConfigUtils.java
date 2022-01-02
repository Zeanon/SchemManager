package de.zeanon.schemmanager.plugin.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.storagemanagercore.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.storagemanagercore.internal.base.settings.Reload;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.thunderfilemanager.ThunderFileManager;
import de.zeanon.thunderfilemanager.internal.base.exceptions.ThunderException;
import de.zeanon.thunderfilemanager.internal.files.config.ThunderConfig;
import de.zeanon.thunderfilemanager.internal.utility.parser.ThunderFileParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.logging.Level;
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
		} catch (final @NotNull UncheckedIOException | FileParseException e) {
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while loading Configs", e);
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be loaded.");
			cause = e;
		}

		if (cause != null) {
			throw new UncheckedIOException(new IOException(cause.getMessage(), cause.getCause()));
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
	public @NotNull List<String> getStringList(final @NotNull String... key) {
		//noinspection unchecked
		return ConfigUtils.get(List.class, key);
	}

	public boolean getBoolean(final @NotNull String... key) {
		return Objects.toBoolean(ConfigUtils.get(Boolean.class, key));
	}

	public int getInt(final @NotNull String... key) {
		return Objects.toInt(ConfigUtils.get(Integer.class, key));
	}

	public @NotNull <T> T get(final @NotNull Class<T> type, final @NotNull String... key) {
		final @Nullable T result = Objects.toDef(ConfigUtils.getConfig().getUseArray(key), type);
		if (result != null) {
			return result;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				Update.updateConfig();
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
		return ConfigUtils.getDefaultValue(type, key);
	}

	/**
	 * Get the default values of the different Config keys.
	 *
	 * @param key the Config key.
	 *
	 * @return the default value.
	 */
	public @NotNull <T> T getDefaultValue(final @NotNull Class<T> type, final @NotNull String... key) {
		try (final @NotNull InputStream resourceStream = BaseFileUtils.createNewInputStreamFromResource("resources/config.tf")) {
			return Objects.notNull(Objects.toDef(ThunderFileParser.readDataAsFileData(resourceStream,
																					  ConfigUtils.getConfig().collectionsProvider(),
																					  ConfigUtils.getConfig().getCommentSetting(),
																					  ConfigUtils.getConfig().getBufferSize()).getUseArray(key), type), "Could not read from the default config.");
		} catch (final @NotNull ThunderException | IOException e) {
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while getting default value for config", e);
		}
		return Objects.notNull(Objects.toDef(new Object(), type));
	}
}