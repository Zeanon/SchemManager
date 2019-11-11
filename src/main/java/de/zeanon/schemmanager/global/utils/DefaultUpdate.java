package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.storage.internal.utils.SMFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DefaultUpdate {

	static void updatePlugin(final boolean autoReload, final JavaPlugin instance) {
		System.out.println(SchemManager.getInstance().getName() + " is updating...");
		try {
			SMFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																.getCodeSource()
																.getLocation()
																.toURI()
																.getPath())
											.getCanonicalFile(), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()));
			System.out.println(SchemManager.getInstance().getName() + " was updated successfully.");
			if (autoReload) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Bukkit.getServer().reload();
					}
				}.runTask(instance);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
		}
	}

	static void updatePlugin(final Player p, final boolean autoReload, final JavaPlugin instance) {
		p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " is updating...");
		try {
			SMFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																.getCodeSource()
																.getLocation()
																.toURI()
																.getPath())
											.getCanonicalFile(), new BufferedInputStream(new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar").openStream()));
			p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " was updated successfully.");
			if (autoReload) {
				new BukkitRunnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.RED + "Server is reloading.");
						Bukkit.getServer().reload();
					}
				}.runTask(instance);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " could not be updated.");
		}
	}
}