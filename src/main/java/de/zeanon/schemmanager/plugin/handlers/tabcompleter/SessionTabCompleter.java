package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.session.SessionManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SessionTabCompleter {

	public @NotNull List<String> getCompletions(final @NotNull CommandSender sender, final @NotNull String message) {
		final @NotNull String[] args = message.split(" ");
		final boolean argumentEnded = message.endsWith(" ");
		if ((args[0].equals("//session")
			 || args[0].equals("/session"))
			&& sender instanceof Player) {
			return SessionTabCompleter.sessionCompletions(new BukkitPlayer((Player) sender), args, argumentEnded);
		} else {
			return Collections.emptyList();
		}
	}

	private @NotNull List<String> sessionCompletions(final @NotNull BukkitPlayer p, final @NotNull String[] args, final boolean argumentEnded) {
		if ((args.length == 2 && !argumentEnded) || (args.length == 1 && argumentEnded)) {
			if (argumentEnded) {
				return Arrays.asList("delete", "list", "search", "load", "save", "swap", "help");
			} else {
				return SchematicTabCompleter.getCompletions(args[1], "delete", "list", "search", "load", "save", "swap", "help");
			}
		} else if ((args.length == 3 && !argumentEnded) || args.length == 2) {
			if (argumentEnded) {
				if (args[1].equalsIgnoreCase("delete")
					|| args[1].equalsIgnoreCase("load")
					|| args[1].equalsIgnoreCase("save")
					|| args[1].equalsIgnoreCase("swap")) {
					final @Nullable Map<String, SessionManager.SessionHolder> sessions = WorldEdit.getInstance().getSessionManager().listSessions(p);
					return sessions == null ? Collections.emptyList()
											: sessions.keySet()
													  .stream()
													  .filter(name -> !name.equals("current"))
													  .collect(Collectors.toList());
				}
			} else {
				if (args[1].equalsIgnoreCase("delete")
					|| args[1].equalsIgnoreCase("load")
					|| args[1].equalsIgnoreCase("save")
					|| args[1].equalsIgnoreCase("swap")) {
					final @Nullable Map<String, SessionManager.SessionHolder> sessions = WorldEdit.getInstance().getSessionManager().listSessions(p);
					return sessions == null ? Collections.emptyList()
											: sessions.keySet()
													  .stream()
													  .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())
																	  && !name.equals("current"))
													  .collect(Collectors.toList());
				}
			}
		}
		return Collections.emptyList();
	}
}