package de.zeanon.schemmanager.worldeditmode.listener.tabcompleter;

import java.io.IOException;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SpigotTabListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onTab(final @NotNull TabCompleteEvent event) throws IOException {
		final @NotNull String message = event.getBuffer().replaceAll("\\s+", " ");
		final @Nullable List<String> completions = WorldEditModeTabCompleter.execute(message);
		if (completions != null) {
			event.setCompletions(completions);
		}
	}
}