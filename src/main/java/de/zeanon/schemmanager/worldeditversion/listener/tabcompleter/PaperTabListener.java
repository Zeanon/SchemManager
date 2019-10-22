package de.zeanon.schemmanager.worldeditversion.listener.tabcompleter;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class PaperTabListener implements Listener {

	@SuppressWarnings("Duplicates")
	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final AsyncTabCompleteEvent event) {
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
				List<String> tempList = WorldEditVersionTabCompleter.onTab(args, event.getBuffer(), deep, message.endsWith(" "));
				if (!tempList.isEmpty()) {
					event.setCompletions(tempList);
				} else {
					event.setCancelled(true);
				}
			}
		} else if (args[0].equalsIgnoreCase("/stoplag")) {
			if (args.length == 1 || (args.length == 2 && !args[1].equals("-c") && !message.endsWith(" "))) {
				event.setCompletions(Collections.singletonList("-c"));
			} else {
				event.setCancelled(true);
			}
		}
	}
}