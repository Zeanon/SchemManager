package de.zeanon.schemmanager.worldedit.commands;

import java.io.File;

import org.bukkit.entity.Player;

import de.zeanon.schemmanager.worldedit.helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class Delete {

	public static boolean onDelete(Player p, String[] args) {
		File schematicFile = new File(Helper.getSchemPath() + args[2] + ".schematic");
		File schemFile = new File(Helper.getSchemPath() + args[2] + ".schem");
		
		if (args.length == 3) {
			if ((schematicFile.exists() && !schematicFile.isDirectory()) || (schemFile.exists() && !schemFile.isDirectory())) {
				Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem del " + args[2] + " confirm", "//schem del " + args[2] + " deny", p);
				Helper.addDeleteRequest(p, args[2]);
				return true;
			}
			else {
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				return false;
			}
		}
		
		
		else if (args.length == 4 && Helper.checkDeleteRequest(p, args[2])) {
			if (args[3].equals("confirm")) {
				if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
					Helper.removeDeleteRequest(p);
					return false;
				}
				
				else if ((schematicFile.exists() && !schematicFile.isDirectory()) || (schemFile.exists() && !schemFile.isDirectory())) {
					if (schematicFile.exists()) {
						schematicFile.delete();
					}
					if (schemFile.exists()) {
						schemFile.delete();
					}
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
					Helper.removeDeleteRequest(p);
					return true;
				}
				else {
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
					Helper.removeDeleteRequest(p);
					return false;
				}

			}
			else if (args[3].equals("deny")) {
				if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
					return false;
				}
				else {
					Helper.removeDeleteRequest(p);
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
					return true;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}