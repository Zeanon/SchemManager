package de.zeanon.schemmanager.globallistener;

import de.zeanon.schemmanager.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {

    private Plugin plugin;

    public CommandHandler (Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("schemmanager")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Missing argument for " + ChatColor.YELLOW + "<" + ChatColor.GOLD
                            + "argument" + ChatColor.YELLOW + ">");

                    return sendUsage(p);
                } else if (args[0].equalsIgnoreCase("disable") && p.hasPermission("schemmanager.disable")) {
                    if (args.length == 1) {
                        Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to disable " + ChatColor.DARK_PURPLE
                                        + "SchemManager" + ChatColor.RED + "? ", "/schemmanager disable confirm", "/schemmanager disable deny",
                                p);
                        Helper.addDisableRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && Helper.checkDisableRequest(p)) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " is being disabled.");
                            Helper.disable(this.plugin);
                            return true;
                        } else if (args[1].equalsIgnoreCase("deny") && Helper.checkDisableRequest(p)) {
                            Helper.removeDisableRequest(p);
                            p.sendMessage(
                                    ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be disabled.");
                            return true;
                        } else {
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return sendDisableUsage(p);
                    }
                } else if (args[0].equalsIgnoreCase("update") && p.hasPermission("schemmanager.update")) {
                    if (args.length == 1) {
                        Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to update?", "/schemmanager update confirm",
                                "/schemmanager update deny", p);
                        Helper.addUpdateRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && Helper.checkUpdateRequest(p)) {
                            Helper.removeUpdateRequest(p);
                            return Helper.update(p);
                        } else if (args[1].equalsIgnoreCase("deny") && Helper.checkUpdateRequest(p)) {
                            Helper.removeUpdateRequest(p);
                            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be updated.");
                            return true;
                        } else {
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " update", ChatColor.DARK_GREEN + ""
                                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
                                "/schemmanager update", p);
                        return true;
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid sub-command '" + ChatColor.GOLD + "" + args[0] + ChatColor.RED + ".");
                    return sendUsage(p);
                }
            } else {
                if (args[0].equalsIgnoreCase("disable")) {
                    Helper.disable(this.plugin);
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    return Helper.update();
                } else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    private boolean sendUsage(Player p) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " update", ChatColor.DARK_GREEN + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
                "/schemmanager update", p);
        return sendDisableUsage(p);
    }

    private boolean sendDisableUsage(Player p) {
        Helper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " disable", ChatColor.DARK_RED + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
                "/schemmanager disable", p);
        return true;
    }
}
