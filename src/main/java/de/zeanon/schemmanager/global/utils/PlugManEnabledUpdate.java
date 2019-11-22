package de.zeanon.schemmanager.global.utils;

import com.rylinaux.plugman.util.PluginUtil;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.storage.internal.utility.utils.SMFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PlugManEnabledUpdate {

	static void updatePlugin(final boolean autoReload) {
		System.out.println(SchemManager.getInstance().getName() + " is updating...");
		try {
			SMFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																.getCodeSource()
																.getLocation()
																.toURI()
																.getPath())
											.getCanonicalFile(), new BufferedInputStream(
					new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar")
							.openStream()));
			System.out.println(SchemManager.getInstance().getName() + " was updated successfully.");
			if (autoReload) {
				PluginUtil.reload(SchemManager.getInstance());
			}
		} catch (@NotNull IOException | URISyntaxException e) {
			e.printStackTrace();
			System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
		}
	}

	static void updatePlugin(final @NotNull Player p, final boolean autoReload) {
		p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " is updating...");
		try {
			SMFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																.getCodeSource()
																.getLocation()
																.toURI()
																.getPath())
											.getCanonicalFile(), new BufferedInputStream(
					new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar")
							.openStream()));
			p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
						  + ChatColor.RED + " was updated successfully.");
			if (autoReload) {
				p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
							  + ChatColor.RED + " is reloading.");
				PluginUtil.reload(SchemManager.getInstance());
			}
		} catch (@NotNull IOException | URISyntaxException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
						  + ChatColor.RED + " could not be updated.");
		}
	}
}