package de.zeanon.schemmanager.worldeditmode.listener.tabcompleter;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;


public class SpigotTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(@NotNull final TabCompleteEvent event) {
		String message = event.getBuffer();
		message = message.replaceAll("\\s+", " ");
		boolean argumentEnded = message.endsWith(" ");
		String[] args = message.replace("worldedit:", "/").split(" ");
		if (args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {
			if (message.contains("./")) {
				event.setCompletions(new ArrayList<>());
			} else {
				boolean deep = false;
				if (((args.length == 3 && argumentEnded) || args.length > 3) && args[2].equalsIgnoreCase("-deep")) {
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
					deep = true;
				}
				if (((args.length == 3 && argumentEnded) || args.length > 3) && args[2].equalsIgnoreCase("-d")) {
					args = (String[]) ArrayUtils.removeElement(args, "-d");
					deep = true;
				}
				event.setCompletions(WorldEditModeTabCompleter.onTab(args, event.getBuffer(), deep, argumentEnded));
			}
		} else if (args[0].equalsIgnoreCase("/stoplag")) {
			if (args.length == 1 || (args.length == 2 && !message.endsWith(" "))) {
				event.setCompletions(Collections.singletonList("-c"));
			} else {
				event.setCompletions(new ArrayList<>());
			}
		}
	}
}