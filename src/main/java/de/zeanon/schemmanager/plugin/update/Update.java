package de.zeanon.schemmanager.plugin.update;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import de.zeanon.storagemanagercore.internal.utility.basic.Pair;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"), instance);
		} else {
			DefaultUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"), instance);
		}
	}

	public void updatePlugin(final @NotNull Player p, final @NotNull JavaPlugin instance) {
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"), instance);
		} else {
			DefaultUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"), instance);
		}
	}

	public void checkConfigUpdate() {
		try {
			if (!Objects.notNull(ConfigUtils.getConfig().getStringUseArray("Plugin Version"))
						.equals(SchemManager.getInstance().getDescription().getVersion())
				|| !ConfigUtils.getConfig().hasKeyUseArray("File Extensions")
				|| !ConfigUtils.getConfig().hasKeyUseArray("Listmax")
				|| !ConfigUtils.getConfig().hasKeyUseArray("Space Lists")
				|| !ConfigUtils.getConfig().hasKeyUseArray("Delete empty Folders")
				|| !ConfigUtils.getConfig().hasKeyUseArray("Save Function Override")
				|| !ConfigUtils.getConfig().hasKeyUseArray("Automatic Reload")) {

				Update.updateConfig();
			}
		} catch (final @NotNull ObjectNullException e) {
			Update.updateConfig();
		}
	}

	public void updateConfig() {
		try {
			final @NotNull List<String> fileExtensions = ConfigUtils.getConfig().hasKeyUseArray("File Extensions")
														 ? Objects.notNull(ConfigUtils.getConfig().getListUseArray("File Extensions"))
														 : Arrays.asList("schem", "schematic");
			final int listmax = ConfigUtils.getConfig().hasKeyUseArray("Listmax")
								? ConfigUtils.getConfig().getIntUseArray("Listmax")
								: 10;
			final boolean spaceLists = !ConfigUtils.getConfig().hasKeyUseArray("Space Lists")
									   || ConfigUtils.getConfig().getBooleanUseArray("Space Lists");
			final boolean deleteEmptyFolders = !ConfigUtils.getConfig().hasKeyUseArray("Delete empty Folders")
											   || ConfigUtils.getConfig().getBooleanUseArray("Delete empty Folders");
			final boolean saveOverride = !ConfigUtils.getConfig().hasKeyUseArray("Save Function Override")
										 || ConfigUtils.getConfig().getBooleanUseArray("Save Function Override");
			final boolean autoReload = !ConfigUtils.getConfig().hasKeyUseArray("Automatic Reload")
									   || ConfigUtils.getConfig().getBooleanUseArray("Automatic Reload");

			ConfigUtils.getConfig().setDataFromResource("resources/config.tf");

			//noinspection unchecked
			ConfigUtils.getConfig().setAllUseArray(new Pair<>(new String[]{"Plugin Version"}, SchemManager.getInstance().getDescription().getVersion()),
												   new Pair<>(new String[]{"File Extensions"}, fileExtensions),
												   new Pair<>(new String[]{"Listmax"}, listmax),
												   new Pair<>(new String[]{"Space Lists"}, spaceLists),
												   new Pair<>(new String[]{"Delete empty Folders"}, deleteEmptyFolders),
												   new Pair<>(new String[]{"Save Function Override"}, saveOverride),
												   new Pair<>(new String[]{"Automatic Reload"}, autoReload));

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' updated.");
		} catch (final @NotNull RuntimeIOException e) {
			throw new RuntimeIOException("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be updated.", e);
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
			HttpURLConnection urlConnect = (HttpURLConnection) new URL(Update.RELEASE_URL).openConnection();
			urlConnect.setInstanceFollowRedirects(false);
			urlConnect.getResponseCode();
			return urlConnect.getHeaderField("Location").replaceFirst(".*/", "");
		} catch (final @NotNull IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}