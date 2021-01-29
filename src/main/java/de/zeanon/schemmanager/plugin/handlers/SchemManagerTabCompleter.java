package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import de.zeanon.storagemanager.external.browniescollections.GapList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SchemManagerTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final @NotNull String @NotNull [] args) {
		if (args.length == 1) {
			final @NotNull List<String> completions = new GapList<>();
			if ("update".startsWith(args[0].toLowerCase())) {
				completions.add("update");
			}
			if ("disable".startsWith(args[0].toLowerCase())) {
				completions.add("disable");
			}
			return completions;
		} else if (args.length == 2 && ((sender instanceof Player
										 && (GlobalRequestUtils.checkUpdateRequest(((Player) sender).getUniqueId())
											 || GlobalRequestUtils.checkDisableRequest(((Player) sender).getUniqueId())))
										|| (sender instanceof ConsoleCommandSender
											&& (GlobalRequestUtils.checkConsoleUpdateRequest()
												|| GlobalRequestUtils.checkConsoleDisableRequest())))) {
			final @NotNull List<String> completions = new GapList<>();
			if ("confirm".startsWith(args[1].toLowerCase())) {
				completions.add("confirm");
			}
			if ("deny".startsWith(args[1].toLowerCase())) {
				completions.add("deny");
			}
			return completions;
		} else {
			return Collections.emptyList();
		}
	}
}