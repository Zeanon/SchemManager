package de.zeanon.schemmanager.plugin.handlers.tabcompleter.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.SchematicTabCompleter;
import java.io.IOException;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;


public class PaperTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final @NotNull AsyncTabCompleteEvent event) throws IOException {
		if (event.getBuffer().startsWith("//schem")
			|| event.getBuffer().startsWith("/schem")) {
			final @NotNull String message = event.getBuffer().replaceAll("\\s+", " ");
			final @NotNull List<String> completions = SchematicTabCompleter.getCompletions(event.getSender(), message);
			if (completions.isEmpty()) {
				event.setCancelled(true);
			} else {
				event.setCompletions(completions);
			}
		}
	}
}