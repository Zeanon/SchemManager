package de.zeanon.schemmanager.globalutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<>();
            if ("update".startsWith(args[0])) {
                completions.add("update");
            }
            if ("disable".startsWith(args[0])) {
                completions.add("disable");
            }
            return completions;
        } else {
            return null;
        }
    }
}
