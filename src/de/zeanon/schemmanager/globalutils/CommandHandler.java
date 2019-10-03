package de.zeanon.schemmanager.globalutils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    /**
     * Gets the user commands and processes them("/schemmanager")
     */
    @SuppressWarnings("NullableProblems")
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
                        DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you really want to disable " + ChatColor.DARK_PURPLE
                                        + "SchemManager" + ChatColor.RED + "? ", "/schemmanager disable confirm", "/schemmanager disable deny",
                                p);
                        DefaultHelper.addDisableRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && DefaultHelper.checkDisableRequest(p)) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " is being disabled.");
                            DefaultHelper.disable();
                            return true;
                        } else if (args[1].equalsIgnoreCase("deny") && DefaultHelper.checkDisableRequest(p)) {
                            DefaultHelper.removeDisableRequest(p);
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
                        DefaultHelper.sendBooleanMessage(ChatColor.RED + "Do you really want to update?", "/schemmanager update confirm",
                                "/schemmanager update deny", p);
                        DefaultHelper.addUpdateRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && DefaultHelper.checkUpdateRequest(p)) {
                            DefaultHelper.removeUpdateRequest(p);
                            return DefaultHelper.update(p);
                        } else if (args[1].equalsIgnoreCase("deny") && DefaultHelper.checkUpdateRequest(p)) {
                            DefaultHelper.removeUpdateRequest(p);
                            p.sendMessage(ChatColor.DARK_PURPLE + "SchemManager" + ChatColor.RED + " will not be updated.");
                            return true;
                        } else {
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        DefaultHelper.sendSuggestMessage(ChatColor.RED + "Usage: ",
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
                    DefaultHelper.disable();
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    return DefaultHelper.update();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param p Player to send to
     * @return usage
     */
    private boolean sendUsage(Player p) {
        DefaultHelper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " update", ChatColor.DARK_GREEN + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
                "/schemmanager update", p);
        return sendDisableUsage(p);
    }

    private boolean sendDisableUsage(Player p) {
        DefaultHelper.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " disable", ChatColor.DARK_RED + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
                "/schemmanager disable", p);
        return true;
    }
}
