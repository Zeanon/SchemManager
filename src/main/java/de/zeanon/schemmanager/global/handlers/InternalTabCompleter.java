package de.zeanon.schemmanager.global.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;


public class InternalTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final @NotNull String[] args) {
		if (args.length == 1) {
			@NotNull ArrayList<String> completions = new ArrayList<>();
			if ("update".startsWith(args[0].toLowerCase())) {
				completions.add("update");
			}
			if ("disable".startsWith(args[0].toLowerCase())) {
				completions.add("disable");
			}
			return completions;
		} else {
			return Collections.emptyList();
		}
	}
}