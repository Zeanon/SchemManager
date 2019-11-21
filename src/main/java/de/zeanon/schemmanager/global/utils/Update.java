package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Update {

	public static void updatePlugin() {
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"));
		} else {
			DefaultUpdate.updatePlugin(ConfigUtils.getBoolean("Automatic Reload"), SchemManager.getInstance());
		}
	}

	public static void updatePlugin(@NotNull final Player p) {
		if (SchemManager.getPluginManager().getPlugin("PlugMan") != null
			&& SchemManager.getPluginManager()
						   .isPluginEnabled(SchemManager.getPluginManager().getPlugin("PlugMan"))) {
			PlugManEnabledUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"));
		} else {
			DefaultUpdate.updatePlugin(p, ConfigUtils.getBoolean("Automatic Reload"), SchemManager.getInstance());
		}
	}

	static void checkConfigUpdate() {
		try {
			if (!Utils.getConfig().getStringUseArray("Plugin Version")
					  .equals(SchemManager.getInstance().getDescription().getVersion())
				|| !Utils.getConfig().hasKeyUseArray("File Extensions")
				|| !Utils.getConfig().hasKeyUseArray("Listmax")
				|| !Utils.getConfig().hasKeyUseArray("Space Lists")
				|| !Utils.getConfig().hasKeyUseArray("Delete empty Folders")
				|| !Utils.getConfig().hasKeyUseArray("Save Function Override")
				|| !Utils.getConfig().hasKeyUseArray("Stoplag Override")
				|| !Utils.getConfig().hasKeyUseArray("Automatic Reload")) {

				updateConfig();
			}
		} catch (ObjectNullException e) {
			updateConfig();
		}
	}

	static void updateConfig() {
		try {
			@NotNull List<String> fileExtensions = Objects.notNull(Utils.getConfig()).hasKeyUseArray("File Extensions")
												   ? Utils.getConfig().getStringListUseArray("File Extensions")
												   : Arrays.asList("schem", "schematic");
			int listmax = Utils.getConfig().hasKeyUseArray("Listmax")
						  ? Utils.getConfig().getIntUseArray("Listmax")
						  : 10;
			boolean spaceLists = !Utils.getConfig().hasKeyUseArray("Space Lists")
								 || Utils.getConfig().getBooleanUseArray("Space Lists");
			boolean deleteEmptyFolders = !Utils.getConfig().hasKeyUseArray("Delete empty Folders")
										 || Utils.getConfig().getBooleanUseArray("Delete empty Folders");
			boolean saveOverride = !Utils.getConfig().hasKeyUseArray("Save Function Override")
								   || Utils.getConfig().getBooleanUseArray("Save Function Override");
			boolean stoplagOverride = !Utils.getConfig().hasKeyUseArray("Stoplag Override")
									  || Utils.getConfig().getBooleanUseArray("Stoplag Override");
			boolean autoReload = !Utils.getConfig().hasKeyUseArray("Automatic Reload")
								 || Utils.getConfig().getBooleanUseArray("Automatic Reload");

			Utils.getConfig().setDataFromResource("resources/config.tf");

			@NotNull LinkedHashMap<String[], Object> dataMap = new LinkedHashMap<>();
			dataMap.put(new String[]{"Plugin Version"}, SchemManager.getInstance().getDescription().getVersion());
			dataMap.put(new String[]{"File Extensions"}, fileExtensions);
			dataMap.put(new String[]{"Listmax"}, listmax);
			dataMap.put(new String[]{"Space Lists"}, spaceLists);
			dataMap.put(new String[]{"Delete empty Folders"}, deleteEmptyFolders);
			dataMap.put(new String[]{"Save Function Override"}, saveOverride);
			dataMap.put(new String[]{"Stoplag Override"}, stoplagOverride);
			dataMap.put(new String[]{"Automatic Reload"}, autoReload);
			Utils.getConfig().setAllUseArray(dataMap);

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' updated.");
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be updated.", e);
		}
	}
}