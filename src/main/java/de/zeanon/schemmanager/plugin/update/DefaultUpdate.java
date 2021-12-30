package de.zeanon.schemmanager.plugin.update;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


@UtilityClass
class DefaultUpdate {

	void updatePlugin(final boolean autoReload, final @NotNull JavaPlugin instance) {
		System.out.println(instance.getName() + " is updating...");
		try {
			BaseFileUtils.writeToFile(new File(SchemManager.class.getProtectionDomain()
																 .getCodeSource()
																 .getLocation()
																 .toURI()
																 .getPath())
											  .getCanonicalFile(),
									  new BufferedInputStream(
											  new URL(Update.DOWNLOAD_URL)
													  .openStream()));

			System.out.println(instance.getName() + " was updated successfully.");

			if (autoReload) {
				System.out.println("Server is reloading.");
				Bukkit.getServer().reload();
			}
		} catch (@NotNull final IOException | URISyntaxException e) {
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
			System.out.println(instance.getName() + " could not be updated.");
		}
	}

	void updatePlugin(final @NotNull Player p, final boolean autoReload, final @NotNull JavaPlugin instance) {
		p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + instance.getName() + ChatColor.DARK_GRAY + "] " +
					  ChatColor.RED + "Updating plugin...");
		try {
			BaseFileUtils.writeToFile(new File(SchemManager.class.getProtectionDomain()
																 .getCodeSource()
																 .getLocation()
																 .toURI()
																 .getPath())
											  .getCanonicalFile(),
									  new BufferedInputStream(
											  new URL(Update.DOWNLOAD_URL)
													  .openStream()));

			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + instance.getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Update successful.");

			if (autoReload) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + instance.getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "Server is reloading...");
				Bukkit.getServer().reload();
			}
		} catch (@NotNull final IOException | URISyntaxException e) {
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + instance.getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.RED + "Could not update.");
		}
	}
}