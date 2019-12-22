package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
class DefaultUpdate {

	static void updatePlugin(final boolean autoReload, final @NotNull JavaPlugin instance) {
		System.out.println(SchemManager.getInstance().getName() + " is updating...");
		try {
			BaseFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																  .getCodeSource()
																  .getLocation()
																  .toURI()
																  .getPath())
											  .getCanonicalFile(),
									  new BufferedInputStream(
											  new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar")
													  .openStream()));
			System.out.println(SchemManager.getInstance().getName() + " was updated successfully.");
			if (autoReload) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Bukkit.getServer().reload();
					}
				}.runTask(instance);
			}
		} catch (@NotNull IOException | URISyntaxException e) {
			e.printStackTrace();
			System.out.println(SchemManager.getInstance().getName() + " could not be updated.");
		}
	}

	static void updatePlugin(final @NotNull Player p, final boolean autoReload, final @NotNull JavaPlugin instance) {
		p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
					  ChatColor.RED + "updating plugin...");
		try {
			BaseFileUtils.writeToFile(new File(WorldEditMode.class.getProtectionDomain()
																  .getCodeSource()
																  .getLocation()
																  .toURI()
																  .getPath())
											  .getCanonicalFile(),
									  new BufferedInputStream(
											  new URL("https://github.com/Zeanon/SchemManager/releases/latest/download/SchemManager.jar")
													  .openStream()));
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
						  ChatColor.RED + " update successful.");
			if (autoReload) {
				new BukkitRunnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
									  ChatColor.RED + "server is reloading.");
						Bukkit.getServer().reload();
					}
				}.runTask(instance);
			}
		} catch (@NotNull IOException | URISyntaxException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]  " +
						  ChatColor.RED + " could not update.");
		}
	}
}