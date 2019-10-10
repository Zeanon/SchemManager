package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
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

                    return sendUpdateUsage(p) && sendDisableUsage(p);
                } else if (args[0].equalsIgnoreCase("disable") && p.hasPermission("schemmanager.disable")) {
                    if (args.length == 1) {
                        MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to disable " + ChatColor.DARK_PURPLE
                                        + SchemManager.getInstance().getName() + ChatColor.RED + "? ", "/schemmanager disable confirm", "/schemmanager disable deny",
                                p);
                        RequestUtils.addDisableRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && RequestUtils.checkDisableRequest(p)) {
                            p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " is being disabled.");
                            SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
                            return true;
                        } else if (args[1].equalsIgnoreCase("deny") && RequestUtils.checkDisableRequest(p)) {
                            RequestUtils.removeDisableRequest(p);
                            p.sendMessage(
                                    ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " will not be disabled.");
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
                        MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to update?", "/schemmanager update confirm",
                                "/schemmanager update deny", p);
                        RequestUtils.addUpdateRequest(p);
                        return true;
                    } else if (args.length == 2 && (args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("deny"))) {
                        if (args[1].equalsIgnoreCase("confirm") && RequestUtils.checkUpdateRequest(p)) {
                            RequestUtils.removeUpdateRequest(p);
                            return Update.updatePlugin(p);
                        } else if (args[1].equalsIgnoreCase("deny") && RequestUtils.checkUpdateRequest(p)) {
                            RequestUtils.removeUpdateRequest(p);
                            p.sendMessage(ChatColor.DARK_PURPLE + SchemManager.getInstance().getName() + ChatColor.RED + " will not be updated.");
                            return true;
                        } else {
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        return sendUpdateUsage(p);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid sub-command '" + ChatColor.GOLD + "" + args[0] + ChatColor.RED + ".");
                    return sendUpdateUsage(p) && sendDisableUsage(p);

                }
            } else {
                if (args.length == 1 && args[0].equalsIgnoreCase("disable")) {
                    SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    return Update.updatePlugin();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * @param p Player to send to
     * @return usage
     */
    private boolean sendUpdateUsage(Player p) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " update", ChatColor.DARK_GREEN + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "!!UPDATE BABY!!",
                "/schemmanager update", p);
        return true;
    }

    private boolean sendDisableUsage(Player p) {
        MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                ChatColor.GRAY + "/schemmanager" + ChatColor.AQUA + " disable", ChatColor.DARK_RED + ""
                        + ChatColor.UNDERLINE + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "PLS DON'T D;",
                "/schemmanager disable", p);
        return true;
    }
}