package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.plugin.utils.commands.CommandMessageUtils;
import de.zeanon.schemmanager.plugin.worldeditcommands.ListSessions;
import de.zeanon.schemmanager.plugin.worldeditcommands.SearchSession;
import de.zeanon.schemmanager.plugin.worldeditcommands.SessionHelp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;


public class SteamEditCommandListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCommand(final @NotNull PlayerCommandPreprocessEvent event) {
		final @NotNull Player p = event.getPlayer();
		final @NotNull String[] args = event.getMessage().replace("worldedit:", "/").split("\\s+");

		if (args[0].equalsIgnoreCase("/session")
			|| args[0].equalsIgnoreCase("//session")) {

			final @NotNull String slash = args[0].equalsIgnoreCase("//session") ? "//" : "/";

			if (args.length == 1) {
				event.setCancelled(true);
				SessionHelp.executeInternally(p, slash);
			} else if (args[1].equalsIgnoreCase("list")
					   && p.hasPermission("worldedit.session.list")) {
				event.setCancelled(true);
				ListSessions.execute(args, p, slash);
			} else if (args[1].equalsIgnoreCase("search")
					   && p.hasPermission("worldedit.session.list")) {
				event.setCancelled(true);
				SearchSession.execute(args, p, slash);
			} else if (args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				SessionHelp.execute(args, p, slash);
			} else {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Invalid sub-command '"
							  + ChatColor.GOLD + "" + args[1] + ChatColor.RED + "'. Options: "
							  + ChatColor.GOLD + "load" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "save" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "swap" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "delete" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "list" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "search");
				CommandMessageUtils.sendInvalidSessionSubCommand(p, slash);
			}
		}
	}
}