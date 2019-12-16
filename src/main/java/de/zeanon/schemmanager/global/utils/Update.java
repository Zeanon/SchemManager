package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
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

	public static void updatePlugin(final @NotNull Player p) {
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
			if (!Objects.notNull(Utils.getConfig().getStringUseArray("Plugin Version"))
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
			final @NotNull List<String> fileExtensions = Utils.getConfig().hasKeyUseArray("File Extensions")
														 ? Objects.notNull(Utils.getConfig().getStringListUseArray("File Extensions"))
														 : Arrays.asList("schem", "schematic");
			final int listmax = Utils.getConfig().hasKeyUseArray("Listmax")
								? Utils.getConfig().getIntUseArray("Listmax")
								: 10;
			final boolean spaceLists = !Utils.getConfig().hasKeyUseArray("Space Lists")
									   || Utils.getConfig().getBooleanUseArray("Space Lists");
			final boolean deleteEmptyFolders = !Utils.getConfig().hasKeyUseArray("Delete empty Folders")
											   || Utils.getConfig().getBooleanUseArray("Delete empty Folders");
			final boolean saveOverride = !Utils.getConfig().hasKeyUseArray("Save Function Override")
										 || Utils.getConfig().getBooleanUseArray("Save Function Override");
			final boolean stoplagOverride = !Utils.getConfig().hasKeyUseArray("Stoplag Override")
											|| Utils.getConfig().getBooleanUseArray("Stoplag Override");
			final boolean autoReload = !Utils.getConfig().hasKeyUseArray("Automatic Reload")
									   || Utils.getConfig().getBooleanUseArray("Automatic Reload");

			Utils.getConfig().setDataFromResource("resources/config.tf");

			//noinspection unchecked
			Utils.getConfig().setAllUseArray(new Pair<>(new String[]{"Plugin Version"}, SchemManager.getInstance().getDescription().getVersion()),
											 new Pair<>(new String[]{"File Extensions"}, fileExtensions),
											 new Pair<>(new String[]{"Listmax"}, listmax),
											 new Pair<>(new String[]{"Space Lists"}, spaceLists),
											 new Pair<>(new String[]{"Delete empty Folders"}, deleteEmptyFolders),
											 new Pair<>(new String[]{"Save Function Override"}, saveOverride),
											 new Pair<>(new String[]{"Stoplag Override"}, stoplagOverride),
											 new Pair<>(new String[]{"Automatic Reload"}, autoReload));

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' updated.");
		} catch (RuntimeIOException e) {
			throw new RuntimeIOException("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be updated.", e);
		}
	}
}