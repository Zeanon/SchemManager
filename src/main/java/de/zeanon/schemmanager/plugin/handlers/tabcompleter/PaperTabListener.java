package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.io.IOException;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PaperTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final @NotNull AsyncTabCompleteEvent event) throws IOException {
		if (event.getBuffer().toLowerCase().startsWith("//schem")
			|| event.getBuffer().toLowerCase().startsWith("/schem")
			|| event.getBuffer().toLowerCase().startsWith("/stoplag")) {
			final @NotNull String message = event.getBuffer().replaceAll("\\s+", " ");
			final @Nullable List<String> completions = WorldEditModeTabCompleter.execute(message);
			if (completions.isEmpty()) {
				event.setCancelled(true);
			} else {
				event.setCompletions(completions);
			}
		}
	}
}