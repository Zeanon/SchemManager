package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.plugin.utils.commands.CommandMessageUtils;
import de.zeanon.schemmanager.plugin.worldeditcommands.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;


public class CommandListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(final @NotNull PlayerCommandPreprocessEvent event) {
		final @NotNull Player p = event.getPlayer();
		final @NotNull String[] args = event.getMessage().replace("worldedit:", "/").split("\\s+");

		if (args[0].equalsIgnoreCase("/schem")
			|| args[0].equalsIgnoreCase("/schematic")
			|| args[0].equalsIgnoreCase("//schem")
			|| args[0].equalsIgnoreCase("//schematic")) {

			final @NotNull String slash = args[0].equalsIgnoreCase("//schem")
										  || args[0].equalsIgnoreCase("//schematic") ? "//" : "/";
			final @NotNull String schemAlias = args[0].equalsIgnoreCase("/schematic")
											   || args[0].equalsIgnoreCase("//schematic") ? "schematic" : "schem";

			// <Help>
			if (args.length == 1) {
				event.setCancelled(true);
				Help.executeInternally(p, slash, schemAlias);
				// </Help>

				// <Delete>
			} else if ((args[1].equalsIgnoreCase("delete")
						|| args[1].equalsIgnoreCase("del")
						   && p.hasPermission("worldedit.schematic.delete"))) {
				event.setCancelled(true);
				Delete.execute(args, p, slash, schemAlias);
				// </Delete>

				// <DeleteFolder>
			} else if ((args[1].equalsIgnoreCase("deletefolder")
						|| args[1].equalsIgnoreCase("delfolder"))
					   && p.hasPermission("worldedit.schematic.delete")) {
				event.setCancelled(true);
				DeleteFolder.execute(args, p, slash, schemAlias);
				// </DeleteFolder>

				// <Rename>
			} else if (args[1].equalsIgnoreCase("rename")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				Rename.execute(args, p, slash, schemAlias);
				// </Rename>

				// <RenameFolder>
			} else if (args[1].equalsIgnoreCase("renamefolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				RenameFolder.execute(args, p, slash, schemAlias);
				// </RenameFolder>

				// <Copy>
			} else if (args[1].equalsIgnoreCase("copy")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				Copy.execute(args, p, slash, schemAlias);
				// </Copy>

				// <CopyFolder>
			} else if (args[1].equalsIgnoreCase("copyfolder")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				CopyFolder.execute(args, p, slash, schemAlias);
				// </CopyFolder>

				// <Load>
			} else if (args[1].equalsIgnoreCase("load")
					   && p.hasPermission("worldedit.schematic.load")) {
				Load.execute(args, p, slash, schemAlias, event);
				// </Load>

				// <Save>
			} else if (args[1].equalsIgnoreCase("save")
					   && p.hasPermission("worldedit.schematic.save")) {
				Save.execute(args, p, slash, schemAlias, event);
				// </Save>

				// <List>
			} else if (args[1].equalsIgnoreCase("list")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				List.execute(args, p, slash, schemAlias);
				// </List>

				// <ListSchems>
			} else if (args[1].equalsIgnoreCase("listschems")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				ListSchems.execute(args, p, slash, schemAlias);
				// </ListSchems>

				// <ListFolder>
			} else if (args[1].equalsIgnoreCase("listfolders")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				ListFolder.execute(args, p, slash, schemAlias);
				// </ListFolder>

				// <Search>
			} else if (args[1].equalsIgnoreCase("search")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				Search.execute(args, p, slash, schemAlias);
				// </Search>

				// <SearchSchem>
			} else if (args[1].equalsIgnoreCase("searchschem")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				SearchSchem.execute(args, p, slash, schemAlias);
				// </SearchSchem>

				// <SearchFolder>
			} else if (args[1].equalsIgnoreCase("searchfolder")
					   && p.hasPermission("worldedit.schematic.list")) {
				event.setCancelled(true);
				SearchFolder.execute(args, p, slash, schemAlias);
				// </SearchFolder>

				// <Download>
			} else if (args[1].equalsIgnoreCase("download")
					   && p.hasPermission("worldedit.schematic.save")) {
				event.setCancelled(true);
				Download.execute(args, p, slash, schemAlias);
				// </Download>

				// <Help>
			} else if (args[1].equalsIgnoreCase("help")) {
				event.setCancelled(true);
				Help.execute(args, p, slash, schemAlias);
				// </Help>

				// <Formats>
			} else if (args[1].equalsIgnoreCase("formats")) {
				event.setCancelled(true);
				Formats.execute(args, p, slash, schemAlias);
				// </Formats>

				// <Invalid Command>
			} else {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Invalid sub-command '"
							  + ChatColor.GOLD + "" + args[1] + ChatColor.RED + "'. Options: "
							  + ChatColor.GOLD + "help" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "formats" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "save" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "load" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "rename" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "renamefolder" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "copy" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "copyfolder" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "delete" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "deletefolder" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "list" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "listfolders" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "search" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "searchfolder" + ChatColor.RED + ", "
							  + ChatColor.GOLD + "download");
				CommandMessageUtils.sendInvalidSubCommand(p, slash, schemAlias);
			}
			// </Invalid Command>
		} else if (args[0].equalsIgnoreCase("/session")
				   || args[0].equalsIgnoreCase("//session")) {

			final @NotNull String slash = args[0].equalsIgnoreCase("//session") ? "//" : "/";

			if (args.length == 1) {
				event.setCancelled(true);
				Help.executeInternally(p, slash, "schematic");
			} else if (args[1].equalsIgnoreCase("list")) {
				event.setCancelled(true);
				ListSessions.execute(args, p, slash);
			} else if (args[1].equalsIgnoreCase("search")) {
				event.setCancelled(true);
				SearchSession.execute(args, p, slash);
			}
		}
	}
}
