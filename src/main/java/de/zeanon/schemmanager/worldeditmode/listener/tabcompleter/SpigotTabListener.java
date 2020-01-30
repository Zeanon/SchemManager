package de.zeanon.schemmanager.worldeditmode.listener.tabcompleter;

import java.io.IOException;
import java.util.Collections;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;


public class SpigotTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final @NotNull TabCompleteEvent event) throws IOException {
		final @NotNull String message = event.getBuffer().replaceAll("\\s+", " ");
		final boolean argumentEnded = message.endsWith(" ");
		final @NotNull String[] args = message.replace("worldedit:", "/").split(" ");
		if (args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {
			if (message.contains("./")) {
				event.setCompletions(Collections.emptyList());
			} else {
				boolean deep = false;
				boolean caseSensitive = false;
				int modifierCount = 0;

				if (args.length > 2 && (args[2].equalsIgnoreCase("-deep") || args[2].equalsIgnoreCase("-d"))) {
					deep = true;
					modifierCount++;
				}

				if (args.length > 2 + modifierCount && (args[2 + modifierCount].equalsIgnoreCase("-casesensitive") || args[2 + modifierCount].equalsIgnoreCase("-c"))) {
					caseSensitive = true;
					modifierCount++;
				}

				if (!deep && (args.length > 2 + modifierCount && (args[2 + modifierCount].equalsIgnoreCase("-deep") || args[2 + modifierCount].equalsIgnoreCase("-d")))) {
					deep = true;
					modifierCount++;
				}

				event.setCompletions(WorldEditModeTabCompleter.onTab(args, event.getBuffer(), deep, caseSensitive, modifierCount, argumentEnded));
			}
		} else if (args[0].equalsIgnoreCase("/stoplag")) {
			if (args.length == 1 || (args.length == 2 && !message.endsWith(" "))) {
				event.setCompletions(Collections.singletonList("-c"));
			} else {
				event.setCompletions(Collections.emptyList());
			}
		}
	}
}