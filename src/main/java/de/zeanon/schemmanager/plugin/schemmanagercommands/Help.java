package de.zeanon.schemmanager.plugin.schemmanagercommands;


import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Help {

	public void execute(final @NotNull Player p, final @NotNull String... args) {
		if (args.length > 1) {
			p.sendMessage(ChatColor.RED + "Too many arguments.");
			GlobalMessageUtils.sendCommandMessage(ChatColor.RED + "Usage: ",
												  ChatColor.GRAY + "/sm"
												  + ChatColor.AQUA + " help",
												  ChatColor.LIGHT_PURPLE + ""
												  + ChatColor.UNDERLINE + ""
												  + ChatColor.ITALIC + ""
												  + ChatColor.BOLD + "OMG PLS HELP ME",
												  "/sm help",
												  p);
		} else {
			Help.executeInternally(p);
		}
	}

	public void executeInternally(final @NotNull Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (ConfigUtils.getBoolean("Space Lists")) {
					p.sendMessage("");
				}

				p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
							  + SchemManager.getInstance().getDescription().getVersion()
							  + " ===");
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + "Usage for schem: ",
													  ChatColor.GRAY + "//schem "
													  + ChatColor.AQUA + "help",
													  ChatColor.LIGHT_PURPLE + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "OMG PLS HELP ME",
													  "//schem help",
													  p);
				GlobalMessageUtils.sendCommandMessage(ChatColor.RED + "Usage for session: ",
													  ChatColor.GRAY + "//session "
													  + ChatColor.AQUA + "help",
													  ChatColor.LIGHT_PURPLE + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "OMG PLS HELP ME",
													  "//session help",
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Update the plugin: ",
													  ChatColor.GRAY + "/schemmanager "
													  + ChatColor.AQUA + "update",
													  ChatColor.DARK_GREEN + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "!!UPDATE BABY!!",
													  "/schemmanager update",
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Disable the plugin: ",
													  ChatColor.GRAY + "/schemmanager "
													  + ChatColor.AQUA + "disable",
													  ChatColor.DARK_RED + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "PLS DON'T D;",
													  "/schemmanager disable",
													  p);
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}