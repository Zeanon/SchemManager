package de.zeanon.schemmanager.worldeditversion.listener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("schem")) {
            return Arrays.asList("SchemTest", "HI");
        }
        if (command.getName().equals("schematic")) {
            return Arrays.asList("SchematicTest", "HI");
        } else {
            return null;
        }
    }
}
