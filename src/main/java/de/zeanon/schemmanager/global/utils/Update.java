package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Update {

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

	static boolean updateConfig(final boolean force) {
		if (force
			|| !Utils.getConfig().getString("Plugin Version").equals(SchemManager.getInstance().getDescription().getVersion())
			|| !Utils.getConfig().hasKey("File Extensions")
			|| !Utils.getConfig().hasKey("Listmax")
			|| !Utils.getConfig().hasKey("Space Lists")
			|| !Utils.getConfig().hasKey("Delete empty Folders")
			|| !Utils.getConfig().hasKey("Save Function Override")
			|| !Utils.getConfig().hasKey("Stoplag Override")
			|| !Utils.getConfig().hasKey("Automatic Reload")) {

			try {
				List<String> fileExtensions = Utils.getConfig().hasKey("File Extensions") ? Utils.getConfig().getStringList("File Extensions") : Arrays.asList("schem", "schematic");
				int listmax = Utils.getConfig().hasKey("Listmax") ? Utils.getConfig().getInt("Listmax") : 10;
				boolean spaceLists = !Utils.getConfig().hasKey("Space Lists") || Utils.getConfig().getBoolean("Space Lists");
				boolean deleteEmptyFolders = !Utils.getConfig().hasKey("Delete empty Folders") || Utils.getConfig().getBoolean("Delete empty Folders");
				boolean saveOverride = !Utils.getConfig().hasKey("Save Function Override") || Utils.getConfig().getBoolean("Save Function Override");
				boolean stoplagOverride = !Utils.getConfig().hasKey("Stoplag Override") || Utils.getConfig().getBoolean("Stoplag Override");
				boolean autoReload = !Utils.getConfig().hasKey("Automatic Reload") || Utils.getConfig().getBoolean("Automatic Reload");

				Utils.getConfig().setFileContentFromResource("resources/config.tf");

				LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>() {{
					put("Plugin Version", SchemManager.getInstance().getDescription().getVersion());
					put("File Extensions", fileExtensions);
					put("Listmax", listmax);
					put("Space Lists", spaceLists);
					put("Delete empty Folders", deleteEmptyFolders);
					put("Save Function Override", saveOverride);
					put("Stoplag Override", stoplagOverride);
					put("Automatic Reload", autoReload);
				}};
				Utils.getConfig().setAll(dataMap);

				System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' updated.");
				return true;
			} catch (IllegalStateException e) {
				System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be updated.");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		} else {
			return false;
		}
	}
}