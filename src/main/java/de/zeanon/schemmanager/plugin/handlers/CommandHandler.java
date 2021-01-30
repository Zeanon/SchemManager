package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


public class CommandHandler implements CommandExecutor {

	/**
	 * Gets the user commands and processes them("/schemmanager")
	 */
	@Override
	public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
		if (command.getName().equalsIgnoreCase("schemmanager")) {
			if (sender instanceof Player) {
				final @NotNull Player p = (Player) sender;
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Missing argument for "
								  + ChatColor.YELLOW + "<"
								  + ChatColor.GOLD + "argument"
								  + ChatColor.YELLOW + ">");
					this.sendUpdateUsage(p);
					this.sendDisableUsage(p);
				} else if (args[0].equalsIgnoreCase("disable")
						   && p.hasPermission("schemmanager.disable")) {
					if (args.length == 1) {
						GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to disable "
															  + ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
															  + ChatColor.RED + "? "
								, "/schemmanager disable confirm"
								, "/schemmanager disable deny"
								, p);
						GlobalRequestUtils.addDisableRequest(p.getUniqueId());
					} else if (args.length == 2
							   && (args[1].equalsIgnoreCase("confirm")
								   || args[1].equalsIgnoreCase("deny"))
							   && GlobalRequestUtils.checkDisableRequest(p.getUniqueId())) {
						GlobalRequestUtils.removeDisableRequest(p.getUniqueId());
						if (args[1].equalsIgnoreCase("confirm")) {
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " is being disabled.");
							SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						} else {
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " will not be disabled.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.sendDisableUsage(p);
					}
				} else if (args[0].equalsIgnoreCase("update")
						   && p.hasPermission("schemmanager.update")) {
					if (args.length == 1) {
						if (Update.checkForUpdate()) {
							GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to update?"
									, "/schemmanager update confirm"
									, "/schemmanager update deny"
									, p);
						} else {
							GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "You are already running on the latest version. Do you really want to update?"
									, "/schemmanager update confirm"
									, "/schemmanager update deny"
									, p);
						}
						GlobalRequestUtils.addUpdateRequest(p.getUniqueId());
					} else if (args.length == 2
							   && (args[1].equalsIgnoreCase("confirm")
								   || args[1].equalsIgnoreCase("deny"))
							   && GlobalRequestUtils.checkUpdateRequest(p.getUniqueId())) {
						GlobalRequestUtils.removeUpdateRequest(p.getUniqueId());
						if (args[1].equalsIgnoreCase("confirm")) {
							if (Bukkit.getVersion().contains("git-Paper")) {
								new BukkitRunnable() {
									@Override
									public void run() {
										Update.updatePlugin(p);
									}
								}.runTaskAsynchronously(SchemManager.getInstance());
							} else {
								Update.updatePlugin(p);
							}
						} else {
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " will not be updated.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						this.sendUpdateUsage(p);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Invalid sub-command '"
								  + ChatColor.GOLD + "" + args[0] + ChatColor.RED + ".");
					this.sendUpdateUsage(p);
					this.sendDisableUsage(p);
				}
			} else {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("disable")) {
						GlobalRequestUtils.addConsoleDisableRequest();
						System.out.println("To disable type 'schemmanager disable confirm', otherwise type 'schemmanager disable deny'");
					} else if (args[0].equalsIgnoreCase("update")) {
						GlobalRequestUtils.addConsoleUpdateRequest();
						System.out.println("To update type 'schemmanager update confirm', otherwise type 'schemmanager update deny'");
					}
				} else if (args.length == 2 && (args[1].equalsIgnoreCase("deny") || args[1].equalsIgnoreCase("confirm"))) {
					if (args[0].equalsIgnoreCase("disable") && GlobalRequestUtils.checkConsoleDisableRequest()) {
						GlobalRequestUtils.removeConsoleDisableRequest();
						if (args[1].equalsIgnoreCase("confirm")) {
							SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						} else {
							System.out.println("SchemManager will not be disabled.");
						}
					} else if (args[0].equalsIgnoreCase("update") && GlobalRequestUtils.checkConsoleUpdateRequest()) {
						GlobalRequestUtils.removeConsoleUpdateRequest();
						if (args[1].equalsIgnoreCase("confirm")) {
							if (Bukkit.getVersion().contains("git-Paper")) {
								new BukkitRunnable() {
									@Override
									public void run() {
										Update.updatePlugin();
									}
								}.runTaskAsynchronously(SchemManager.getInstance());
							} else {
								Update.updatePlugin();
							}
						} else {
							System.out.println("SchemManager will not be updated.");
						}
					}
				}
			}
		}
		return true;
	}

	private void sendUpdateUsage(final @NotNull Player p) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  ChatColor.GRAY + "/schemmanager"
											  + ChatColor.AQUA + " update",
											  ChatColor.DARK_GREEN + ""
											  + ChatColor.UNDERLINE + ""
											  + ChatColor.ITALIC + ""
											  + ChatColor.BOLD + "!!UPDATE BABY!!",
											  "/schemmanager update", p);
	}

	private void sendDisableUsage(final @NotNull Player p) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  ChatColor.GRAY + "/schemmanager"
											  + ChatColor.AQUA + " disable",
											  ChatColor.DARK_RED + ""
											  + ChatColor.UNDERLINE + ""
											  + ChatColor.ITALIC + ""
											  + ChatColor.BOLD + "PLS DON'T D;",
											  "/schemmanager disable", p);
	}
}