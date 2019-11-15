package de.zeanon.schemmanager.global.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.global.utils.RequestUtils;
import de.zeanon.schemmanager.global.utils.Update;
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
	@SuppressWarnings("NullableProblems")
	@Override
	public boolean onCommand(final CommandSender sender, @NotNull final Command command, final String label, @NotNull final String[] args) {
		if (command.getName().equalsIgnoreCase("schemmanager")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Missing argument for "
								  + ChatColor.YELLOW + "<"
								  + ChatColor.GOLD + "argument"
								  + ChatColor.YELLOW + ">");
					sendUpdateUsage(p);
					sendDisableUsage(p);
				} else if (args[0].equalsIgnoreCase("disable")
						   && p.hasPermission("schemmanager.disable")) {
					if (args.length == 1) {
						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to disable "
														+ ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
														+ ChatColor.RED + "? "
								, "/schemmanager disable confirm"
								, "/schemmanager disable deny"
								, p);
						RequestUtils.addDisableRequest(p);
					} else if (args.length == 2
							   && (args[1].equalsIgnoreCase("confirm")
								   || args[1].equalsIgnoreCase("deny"))) {
						if (args[1].equalsIgnoreCase("confirm")
							&& RequestUtils.checkDisableRequest(p)) {
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " is being disabled.");
							SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						} else if (args[1].equalsIgnoreCase("deny")
								   && RequestUtils.checkDisableRequest(p)) {
							RequestUtils.removeDisableRequest(p);
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " will not be disabled.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						sendDisableUsage(p);
					}
				} else if (args[0].equalsIgnoreCase("update")
						   && p.hasPermission("schemmanager.update")) {
					if (args.length == 1) {
						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to update?"
								, "/schemmanager update confirm"
								, "/schemmanager update deny"
								, p);
						RequestUtils.addUpdateRequest(p);
					} else if (args.length == 2
							   && (args[1].equalsIgnoreCase("confirm")
								   || args[1].equalsIgnoreCase("deny"))) {
						if (args[1].equalsIgnoreCase("confirm") && RequestUtils.checkUpdateRequest(p)) {
							RequestUtils.removeUpdateRequest(p);
							new BukkitRunnable() {
								@Override
								public void run() {
									Update.updatePlugin(p);
								}
							}.runTaskAsynchronously(SchemManager.getInstance());
						} else if (args[1].equalsIgnoreCase("deny")
								   && RequestUtils.checkUpdateRequest(p)) {
							RequestUtils.removeUpdateRequest(p);
							p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName()
										  + ChatColor.RED + " will not be updated.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						sendUpdateUsage(p);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Invalid sub-command '"
								  + ChatColor.GOLD + "" + args[0] + ChatColor.RED + ".");
					sendUpdateUsage(p);
					sendDisableUsage(p);
				}
			} else {
				if (args.length == 1 && args[0].equalsIgnoreCase("disable")) {
					SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
				} else if (args[0].equalsIgnoreCase("update")) {
					new BukkitRunnable() {
						@Override
						public void run() {
							Update.updatePlugin();
						}
					}.runTaskAsynchronously(SchemManager.getInstance());
				}
			}
		}
		return true;
	}

	private void sendUpdateUsage(@NotNull final Player p) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + "/schemmanager"
										+ ChatColor.AQUA + " update",
										ChatColor.DARK_GREEN + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "!!UPDATE BABY!!",
										"/schemmanager update", p);
	}

	private void sendDisableUsage(@NotNull final Player p) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + "/schemmanager"
										+ ChatColor.AQUA + " disable",
										ChatColor.DARK_RED + ""
										+ ChatColor.UNDERLINE + ""
										+ ChatColor.ITALIC + ""
										+ ChatColor.BOLD + "PLS DON'T D;",
										"/schemmanager disable", p);
	}
}