package de.zeanon.schemmanager.plugin.update;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.interfaces.DataMap;
import de.zeanon.storagemanagercore.internal.base.settings.Comment;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.thunderfilemanager.internal.base.cache.filedata.ThunderFileData;
import de.zeanon.thunderfilemanager.internal.base.exceptions.ThunderException;
import de.zeanon.thunderfilemanager.internal.utility.parser.ThunderFileParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Update {

	final String DOWNLOAD_URL = Update.RELEASE_URL + "/download/SchemManager.jar";
	private final String RELEASE_URL = "https://github.com/Zeanon/SchemManager/releases/latest";

	public void updatePlugin(final @NotNull JavaPlugin instance) {
		final @NotNull Updater updater;
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			updater = new PlugManEnabledUpdater();
		} else {
			updater = new PlugManDisabledUpdater();
		}

		updater.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"), instance);
	}

	public void updatePlugin(final @NotNull Player p, final @NotNull JavaPlugin instance) {
		final @NotNull Updater updater;
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			updater = new PlugManEnabledUpdater();
		} else {
			updater = new PlugManDisabledUpdater();
		}

		updater.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"), instance);
	}

	public void checkConfigUpdate() {
		try {
			if (!Objects.notNull(ConfigUtils.getConfig().getStringUseArray("Plugin Version"))
						.equals(SchemManager.getInstance().getDescription().getVersion())) {
				Update.updateConfig();
				return;
			}

			for (final @NotNull String[] entry : Objects.notNull(Objects.notNull(ConfigUtils.getDefaultFileData()).getKeysUseArray())) {
				if (!ConfigUtils.getConfig().hasKeyUseArray(entry)) {
					Update.updateConfig();
					return;
				}
			}
		} catch (final @NotNull ObjectNullException e) {
			Update.updateConfig();
		}
	}

	public void updateConfig() {
		try {
			//noinspection rawtypes
			final @NotNull ThunderFileData<DataMap, ?, List> data = ThunderFileParser.readDataAsFileData(ConfigUtils.getConfig().file(), //NOSONAR
																										 ConfigUtils.getConfig().collectionsProvider(),
																										 Comment.SKIP,
																										 ConfigUtils.getConfig().getBufferSize()); //NOSONAR

			data.insertUseArray(new String[]{"Plugin Version"}, SchemManager.getInstance().getDescription().getVersion());

			ConfigUtils.getConfig().setDataFromResource("resources/config.tf");

			for (final @NotNull String[] key : Objects.notNull(data.getKeysUseArray())) {
				ConfigUtils.getConfig().setUseArray(key, data.getUseArray(key));
			}

			SchemManager.getChatLogger().info(">> [Configs] >> 'config.tf' updated.");
		} catch (final UncheckedIOException | ThunderException e) {
			SchemManager.getChatLogger().log(Level.SEVERE, ">> [Configs] >> 'config.tf' could not be updated.", e);
		}
	}

	public void updateAvailable(final @NotNull Player p) {
		if ((p.hasPermission("schemmanager.update")) && Update.checkForUpdate()) {
			GlobalMessageUtils.sendCommandMessage("",
												  ChatColor.RED + ""
												  + ChatColor.BOLD + "There is a new Update available for SchemManager, click here to update.",
												  ChatColor.DARK_GREEN + ""
												  + ChatColor.UNDERLINE + ""
												  + ChatColor.ITALIC + ""
												  + ChatColor.BOLD + "!!UPDATE BABY!!",
												  "/sm update",
												  p);
		}
	}

	public boolean checkForUpdate() {
		return !("v" + SchemManager.getInstance().getDescription().getVersion()).equalsIgnoreCase(Update.getGithubVersionTag());
	}

	private String getGithubVersionTag() {
		try {
			final HttpURLConnection urlConnect = (HttpURLConnection) new URL(Update.RELEASE_URL).openConnection();
			urlConnect.setInstanceFollowRedirects(false);
			urlConnect.getResponseCode();
			return urlConnect.getHeaderField("Location").replaceFirst(".*/", "");
		} catch (final @NotNull IOException e) {
			SchemManager.getChatLogger().log(Level.SEVERE, "Error while getting newest version tag from Github", e);
			return null;
		}
	}
}