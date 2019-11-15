package de.zeanon.schemmanager.global.handlers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;


public class InternalTabCompleter implements TabCompleter {

	@SuppressWarnings("NullableProblems")
	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, @NotNull final String[] args) {
		if (args.length == 1) {
			ArrayList<String> completions = new ArrayList<>();
			if ("update".startsWith(args[0].toLowerCase())) {
				completions.add("update");
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