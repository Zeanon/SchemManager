package de.zeanon.schemmanager.globalutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> completions = new ArrayList<>();
            if ("updatePlugin".startsWith(args[0].toLowerCase())) {
                completions.add("updatePlugin");
            }
            if ("disable".startsWith(args[0].toLowerCase())) {
                completions.add("disable");
            }
            return completions;
        } else {
            return new ArrayList<>();
        }
    }
}
