package de.zeanon.schemmanager.plugin.update;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.InitMode;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
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
			if (!Objects.notNull(InitMode.getConfig().getStringUseArray("Plugin Version"))
						.equals(SchemManager.getInstance().getDescription().getVersion())
				|| !InitMode.getConfig().hasKeyUseArray("File Extensions")
				|| !InitMode.getConfig().hasKeyUseArray("Listmax")
				|| !InitMode.getConfig().hasKeyUseArray("Space Lists")
				|| !InitMode.getConfig().hasKeyUseArray("Delete empty Folders")
				|| !InitMode.getConfig().hasKeyUseArray("Save Function Override")
				|| !InitMode.getConfig().hasKeyUseArray("Stoplag Override")
				|| !InitMode.getConfig().hasKeyUseArray("Automatic Reload")) {

				Update.updateConfig();
			}
		} catch (final @NotNull ObjectNullException e) {
			Update.updateConfig();
		}
	}

	public void updateConfig() {
		try {
			final @NotNull List<String> fileExtensions = InitMode.getConfig().hasKeyUseArray("File Extensions")
														 ? Objects.notNull(InitMode.getConfig().getListUseArray("File Extensions"))
														 : Arrays.asList("schem", "schematic");
			final int listmax = InitMode.getConfig().hasKeyUseArray("Listmax")
								? InitMode.getConfig().getIntUseArray("Listmax")
								: 10;
			final boolean spaceLists = !InitMode.getConfig().hasKeyUseArray("Space Lists")
									   || InitMode.getConfig().getBooleanUseArray("Space Lists");
			final boolean deleteEmptyFolders = !InitMode.getConfig().hasKeyUseArray("Delete empty Folders")
											   || InitMode.getConfig().getBooleanUseArray("Delete empty Folders");
			final boolean saveOverride = !InitMode.getConfig().hasKeyUseArray("Save Function Override")
										 || InitMode.getConfig().getBooleanUseArray("Save Function Override");
			final boolean stoplagOverride = !InitMode.getConfig().hasKeyUseArray("Stoplag Override")
											|| InitMode.getConfig().getBooleanUseArray("Stoplag Override");
			final boolean autoReload = !InitMode.getConfig().hasKeyUseArray("Automatic Reload")
									   || InitMode.getConfig().getBooleanUseArray("Automatic Reload");

			InitMode.getConfig().setDataFromResource("resources/config.tf");

			//noinspection unchecked
			InitMode.getConfig().setAllUseArray(new Pair<>(new String[]{"Plugin Version"}, SchemManager.getInstance().getDescription().getVersion()),
												new Pair<>(new String[]{"File Extensions"}, fileExtensions),
												new Pair<>(new String[]{"Listmax"}, listmax),
												new Pair<>(new String[]{"Space Lists"}, spaceLists),
												new Pair<>(new String[]{"Delete empty Folders"}, deleteEmptyFolders),
												new Pair<>(new String[]{"Save Function Override"}, saveOverride),
												new Pair<>(new String[]{"Stoplag Override"}, stoplagOverride),
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