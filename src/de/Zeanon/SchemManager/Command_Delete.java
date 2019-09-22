package de.Zeanon.SchemManager;

import java.io.File;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Command_Delete {

	public static boolean onDelete(Player p, String[] args) {
		File schematicFile = new File(Helper.getSchemPath() + args[2] + ".schematic");
		File schemFile = new File(Helper.getSchemPath() + args[2] + ".schem");
		
		if (args.length == 3) {
			if ((schematicFile.exists() && !schematicFile.isDirectory()) || (schemFile.exists() && !schemFile.isDirectory())) {
				Helper.sendBooleanMessage(ChatColor.RED + "Willst du " + ChatColor.GOLD + args[2] + ChatColor.RED + " wirklich löschen?", "//schem del " + args[2] + " confirm", "//schem del " + args[2] + " deny", p);
				Helper.addDeleteRequest(p, args[2]);
				return true;
			}
			if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " existiert nicht.");
				return false;
			}
		}
		
		if (args.length == 4 && Helper.checkDeleteRequest(p, args[2])) {
			if (args[3].equals("confirm")) {
				if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
					return false;
				}
				Helper.removeDeleteRequest(p);
				if ((schematicFile.exists() && !schematicFile.isDirectory()) || (schemFile.exists() && !schemFile.isDirectory())) {
					if (schematicFile.exists()) {
						schematicFile.delete();
					}
					if (schemFile.exists()) {
						schemFile.delete();
					}
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " wurde erfolgreich gelöscht.");
					return true;
				}
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " konnte leider nicht gelöscht werden.");
				return false;
			}
			if (args[3].equals("deny")) {
				if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
					return false;
				}
				Helper.removeDeleteRequest(p);
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " wurde nicht gelöscht.");
				return true;
			}
		}
		return false;
	}
}
