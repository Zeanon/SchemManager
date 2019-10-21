package de.zeanon.schemmanager.worldeditversion.listener.tabcompleter;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;


public class SpigotTabListener implements Listener {

	@SuppressWarnings("Duplicates")
	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final TabCompleteEvent event) {
		String message = event.getBuffer();
		while (message.contains("  ")) {
			message = message.replaceAll(" {2}", " ");
		}
		String[] args = message.replaceAll("worldedit:", "/").split(" ");
		if (args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {
			if (message.contains("./")) {
				event.setCompletions(new ArrayList<>());
			} else {
				boolean deep = false;
				if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
					args = (String[]) ArrayUtils.removeElement(args, "-deep");
					deep = true;
				}
				if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
					args = (String[]) ArrayUtils.removeElement(args, "-d");
					deep = true;
				}
				event.setCompletions(WorldEditVersionTabCompleter.onTab(args, event.getBuffer(), deep, message.endsWith(" ")));
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