package de.zeanon.schemmanager.plugin.handlers.tabcompleter;

import java.io.IOException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;


public class SpigotTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final @NotNull TabCompleteEvent event) throws IOException {
		if (event.getBuffer().toLowerCase().startsWith("//schem")
			|| event.getBuffer().toLowerCase().startsWith("/schem")) {
			final @NotNull String message = event.getBuffer().replaceAll("\\s+", " ");
			event.setCompletions(WorldEditModeTabCompleter.execute(message));
		}
	}
}