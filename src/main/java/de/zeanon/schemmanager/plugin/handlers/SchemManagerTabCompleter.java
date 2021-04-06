package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SchemManagerTabCompleter implements TabCompleter {

	public static @NotNull List<String> getCompletions(final @NotNull String arg, final @NotNull String... completions) {
		List<String> result = new GapList<>();
		for (final @NotNull String completion : completions) {
			if (completion.startsWith(arg.toLowerCase()) && !completion.equalsIgnoreCase(arg)) {
				result.add(completion);
			}
		}
		return result;
	}

	@Override
	public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final @NotNull String[] args) {
		if (args.length == 1) {
			List<String> completions = SchemManagerTabCompleter.getCompletions(args[0], "update", "disable");
			return completions.isEmpty() ? null : completions;
		} else if (args.length == 2 && ((sender instanceof Player
										 && (GlobalRequestUtils.checkUpdateRequest(((Player) sender).getUniqueId())
											 || GlobalRequestUtils.checkDisableRequest(((Player) sender).getUniqueId())))
										|| (sender instanceof ConsoleCommandSender
											&& (GlobalRequestUtils.checkConsoleUpdateRequest()
												|| GlobalRequestUtils.checkConsoleDisableRequest())))) {
			List<String> completions = SchemManagerTabCompleter.getCompletions(args[1], "-confirm", "-deny");
			return completions.isEmpty() ? null : completions;
		} else {
			return null;
		}
	}
}