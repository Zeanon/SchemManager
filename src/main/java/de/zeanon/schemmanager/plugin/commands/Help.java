package de.zeanon.schemmanager.plugin.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Help {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		if (args.length == 2) {
			Help.executeInternally(p, slash, schemAlias);
		} else {
			p.sendMessage(ChatColor.RED + "Too many arguments.");
			GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
												  ChatColor.GRAY + slash + schemAlias
												  + ChatColor.AQUA + " help",
												  ChatColor.LIGHT_PURPLE + ""
												  + ChatColor.UNDERLINE + ""
												  + ChatColor.ITALIC + ""
												  + ChatColor.BOLD + "OMG PLS HELP ME",
												  slash + schemAlias + " help", p);
		}
	}

	public void executeInternally(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (ConfigUtils.getBoolean("Space Lists")) {
					p.sendMessage("");
				}

				p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
							  + SchemManager.getInstance().getDescription().getVersion()
							  + " ===");
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Get some help: ",
													  ChatColor.GRAY + slash + schemAlias + " "
													  + ChatColor.AQUA + "help",
													  ChatColor.LIGHT_PURPLE + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "OMG PLS HELP ME",
													  slash + schemAlias + " help",
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Show Available formats: ",
													  Formats.usageMessage(slash, schemAlias),
													  Formats.usageHoverMessage(),
													  Formats.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Load a schematic: ",
													  Load.usageMessage(slash, schemAlias),
													  Load.usageHoverMessage(slash, schemAlias),
													  Load.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Save a schematic: ",
													  Save.usageMessage(slash, schemAlias),
													  Save.usageHoverMessage(slash, schemAlias),
													  Save.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Rename a schematic: ",
													  Rename.usageMessage(slash, schemAlias),
													  Rename.usageHoverMessage(slash, schemAlias),
													  Rename.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Rename a folder: ",
													  RenameFolder.usageMessage(slash, schemAlias),
													  RenameFolder.usageHoverMessage(slash, schemAlias),
													  RenameFolder.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Copy a schematic: ",
													  Copy.usageMessage(slash, schemAlias),
													  Copy.usageHoverMessage(slash, schemAlias),
													  Copy.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Copy a folder: ",
													  CopyFolder.usageMessage(slash, schemAlias),
													  CopyFolder.usageHoverMessage(slash, schemAlias),
													  CopyFolder.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Delete a schematic: ",
													  Delete.usageMessage(slash, schemAlias),
													  Delete.usageHoverMessage(slash, schemAlias),
													  Delete.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Delete a folder: ",
													  DeleteFolder.usageMessage(slash, schemAlias),
													  DeleteFolder.usageHoverMessage(slash, schemAlias),
													  DeleteFolder.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "List schematics: ",
													  List.usageMessage(slash, schemAlias),
													  List.usageHoverMessage(slash, schemAlias),
													  List.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "List folders: ",
													  ListFolder.usageMessage(slash, schemAlias),
													  ListFolder.usageHoverMessage(slash, schemAlias),
													  ListFolder.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Search for a schematic: ",
													  Search.usageMessage(slash, schemAlias),
													  Search.usageHoverMessage(slash, schemAlias),
													  Search.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Search for a folder: ",
													  SearchFolder.usageMessage(slash, schemAlias),
													  SearchFolder.usageHoverMessage(slash, schemAlias),
													  SearchFolder.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Download a schematic: ",
													  Download.usageMessage(slash, schemAlias),
													  Download.usageHoverMessage(slash, schemAlias),
													  Download.usageCommand(slash, schemAlias),
													  p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Update the plugin: ",
													  ChatColor.GRAY + "/schemmanager "
													  + ChatColor.AQUA + "update",
													  ChatColor.DARK_GREEN + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "!!UPDATE BABY!!",
													  "/schemmanager update", p);
				GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Disable the plugin: ",
													  ChatColor.GRAY + "/schemmanager "
													  + ChatColor.AQUA + "disable",
													  ChatColor.DARK_RED + ""
													  + ChatColor.UNDERLINE + ""
													  + ChatColor.ITALIC + ""
													  + ChatColor.BOLD + "PLS DON'T D;",
													  "/schemmanager disable", p);
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}