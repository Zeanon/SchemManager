package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Update {

	public static boolean updateConfig(final boolean force) {
		if (force
			|| !SchemManager.getLocalConfig().hasKey("Plugin Version")
			|| !SchemManager.getLocalConfig().getString("Plugin Version").equals(SchemManager.getInstance().getDescription().getVersion())
			|| !SchemManager.getLocalConfig().hasKey("File Extensions")
			|| !SchemManager.getLocalConfig().hasKey("Listmax")
			|| !SchemManager.getLocalConfig().hasKey("Space Lists")
			|| !SchemManager.getLocalConfig().hasKey("Delete empty Folders")
			|| !SchemManager.getLocalConfig().hasKey("Save Function Override")
			|| !SchemManager.getLocalConfig().hasKey("Stoplag Override")
			|| !SchemManager.getLocalConfig().hasKey("Automatic Reload")) {

			List<String> fileExtensions = SchemManager.getLocalConfig().hasKey("File Extensions") ? SchemManager.getLocalConfig().getStringList("File Extensions") : Arrays.asList("schem", "schematic");
			int listmax = SchemManager.getLocalConfig().hasKey("Listmax") ? SchemManager.getLocalConfig().getInt("Listmax") : 10;
			boolean spaceLists = !SchemManager.getLocalConfig().hasKey("Space Lists") || SchemManager.getLocalConfig().getBoolean("Space Lists");
			boolean deleteEmptyFolders = !SchemManager.getLocalConfig().hasKey("Delete empty Folders") || SchemManager.getLocalConfig().getBoolean("Delete empty Folders");
			boolean saveOverride = !SchemManager.getLocalConfig().hasKey("Save Function Override") || SchemManager.getLocalConfig().getBoolean("Save Function Override");
			boolean stoplagOverride = !SchemManager.getLocalConfig().hasKey("Stoplag Override") || SchemManager.getLocalConfig().getBoolean("Stoplag Override");
			boolean autoReload = !SchemManager.getLocalConfig().hasKey("Automatic Reload") || SchemManager.getLocalConfig().getBoolean("Automatic Reload");

			try {
				SchemManager.getLocalConfig().setFileContentFromResource("resources/config.ls");

				SchemManager.getLocalConfig().set("Plugin Version", SchemManager.getInstance().getDescription().getVersion());
				SchemManager.getLocalConfig().set("File Extensions", fileExtensions);
				SchemManager.getLocalConfig().set("Listmax", listmax);
				SchemManager.getLocalConfig().set("Space Lists", spaceLists);
				SchemManager.getLocalConfig().set("Delete empty Folders", deleteEmptyFolders);
				SchemManager.getLocalConfig().set("Save Function Override", saveOverride);
				SchemManager.getLocalConfig().set("Stoplag Override", stoplagOverride);
				SchemManager.getLocalConfig().set("Automatic Reload", autoReload);

				System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.getLocalConfig().getFile().getName() + " updated");
				return true;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + SchemManager.getLocalConfig().getFile().getName() + " could not be updated");
				return false;
			}
		} else {
			return true;
		}
	}

	public static void updatePlugin() {
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
		} else {
			DefaultUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"), SchemManager.getInstance());
		}
	}

	public static void updatePlugin(final Player p) {
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null && SchemManager.getPluginManager().isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
		} else {
			DefaultUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"), SchemManager.getInstance());
		}
	}
}