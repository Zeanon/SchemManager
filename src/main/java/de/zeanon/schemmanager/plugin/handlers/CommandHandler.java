package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.RunningMode;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import net.md_5.bungee.api.ChatColor;
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
		new BukkitRunnable() {
			@Override
			public void run() {
				if (command.getName().equalsIgnoreCase("schemmanager")) {
					if (sender instanceof Player) {
						final @NotNull Player p = (Player) sender;
						if (args.length == 0) {
							p.sendMessage(ChatColor.RED + "Missing argument for "
										  + ChatColor.YELLOW + "<"
										  + ChatColor.GOLD + "argument"
										  + ChatColor.YELLOW + ">");
							CommandHandler.sendUpdateUsage(p);
							CommandHandler.sendDisableUsage(p);
						} else if (args[0].equalsIgnoreCase("disable")
								   && p.hasPermission("schemmanager.disable")) {
							if (args.length == 1) {
								GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
																	  + ChatColor.RED + "Do you really want to disable "
																	  + ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
																	  + ChatColor.RED + "? "
										, "/sm disable confirm"
										, "/sm disable deny"
										, p);
								GlobalRequestUtils.addDisableRequest(p.getUniqueId().toString());
							} else if (args.length == 2
									   && (args[1].equalsIgnoreCase("confirm")
										   || args[1].equalsIgnoreCase("deny"))) {
								if (GlobalRequestUtils.checkDisableRequest(p.getUniqueId().toString())) {
									GlobalRequestUtils.removeDisableRequest(p.getUniqueId().toString());
									if (args[1].equalsIgnoreCase("confirm")) {
										p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
													  + ChatColor.RED + " is being disabled.");
										if (RunningMode.isPaperSpigot()) {
											SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
										} else {
											new BukkitRunnable() {
												@Override
												public void run() {
													SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
												}
											}.runTask(SchemManager.getInstance());
										}
									} else {
										p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
													  + ChatColor.RED + " will not be disabled.");
									}
								} else {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
												  + ChatColor.RED + "You don't have a pending disable request.");
								}
							} else {
								p.sendMessage(ChatColor.RED + "Too many arguments.");
								CommandHandler.sendDisableUsage(p);
							}
						} else if (args[0].equalsIgnoreCase("update")
								   && p.hasPermission("schemmanager.update")) {
							if (args.length == 1) {
								if (!Update.checkForUpdate()) {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
												  + ChatColor.RED + "You are already running the latest Version.");
								}
								GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
																	  + ChatColor.RED + "Do you really want to update?"
										, "/sm update confirm"
										, "/sm update deny"
										, p);
								GlobalRequestUtils.addUpdateRequest(p.getUniqueId().toString());
							} else if (args.length == 2
									   && (args[1].equalsIgnoreCase("confirm")
										   || args[1].equalsIgnoreCase("deny"))) {
								if (GlobalRequestUtils.checkUpdateRequest(p.getUniqueId().toString())) {
									GlobalRequestUtils.removeUpdateRequest(p.getUniqueId().toString());
									if (args[1].equalsIgnoreCase("confirm")) {
										if (RunningMode.isPaperSpigot()) {
											Update.updatePlugin(SchemManager.getInstance());
										} else {
											new BukkitRunnable() {
												@Override
												public void run() {
													Update.updatePlugin(p, SchemManager.getInstance());
												}
											}.runTask(SchemManager.getInstance());
										}
									} else {
										p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
													  + ChatColor.RED + "Plugin will not be updated.");
									}
								} else {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
												  + ChatColor.RED + "You don't have a pending update request.");
								}
							} else {
								p.sendMessage(ChatColor.RED + "Too many arguments.");
								CommandHandler.sendUpdateUsage(p);
							}
						} else {
							p.sendMessage(ChatColor.RED + "Invalid sub-command '"
										  + ChatColor.GOLD + "" + args[0] + ChatColor.RED + "'.");
							CommandHandler.sendUpdateUsage(p);
							CommandHandler.sendDisableUsage(p);
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
									if (RunningMode.isPaperSpigot()) {
										SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
									} else {
										new BukkitRunnable() {
											@Override
											public void run() {
												SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
											}
										}.runTask(SchemManager.getInstance());
									}
								} else {
									System.out.println("SchemManager will not be disabled.");
								}
							} else if (args[0].equalsIgnoreCase("update") && GlobalRequestUtils.checkConsoleUpdateRequest()) {
								GlobalRequestUtils.removeConsoleUpdateRequest();
								if (args[1].equalsIgnoreCase("confirm")) {
									if (RunningMode.isPaperSpigot()) {
										Update.updatePlugin(SchemManager.getInstance());
									} else {
										new BukkitRunnable() {
											@Override
											public void run() {
												Update.updatePlugin(SchemManager.getInstance());
											}
										}.runTask(SchemManager.getInstance());
									}
								} else {
									System.out.println("SchemManager will not be updated.");
								}
							}
						}
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
		return true;
	}

	private static void sendUpdateUsage(final @NotNull Player p) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  ChatColor.GRAY + "/schemmanager"
											  + ChatColor.AQUA + " update",
											  ChatColor.DARK_GREEN + ""
											  + ChatColor.UNDERLINE + ""
											  + ChatColor.ITALIC + ""
											  + ChatColor.BOLD + "!!UPDATE BABY!!",
											  "/schemmanager update", p);
	}

	private static void sendDisableUsage(final @NotNull Player p) {
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