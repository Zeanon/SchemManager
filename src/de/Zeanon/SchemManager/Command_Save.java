package de.Zeanon.SchemManager;

import java.io.File;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;

import net.md_5.bungee.api.ChatColor;

public class Command_Save {

	public static boolean onSave(Player p, String[] args) {
		File schematicFile = new File(Helper.getSchemPath() + args[2] + ".schematic");
		File schemFile = new File(Helper.getSchemPath() + args[2] + ".schem");
		
		LocalSession session = Helper.we.getSession(p);
		if (args.length == 3) {
			try {
				session.getClipboard();
				Helper.addOverwriteRequest(p, args[2]);
				if ((schematicFile.exists() && !schematicFile.isDirectory()) || (schemFile.exists() && !schemFile.isDirectory())) {
					p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
					Helper.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem save " + args[2] + " confirm", "//schem save " + args[2] + " deny", p);
					return true;
				}
				else {
					p.performCommand("/schem save -f " + args[2]);
					return true;
				}
			} catch (EmptyClipboardException e) {
				p.sendMessage(ChatColor.RED + "Your clipboard is empty. Use //copy first.");
				return true;
			}
		}
		
		
		else {
			if (args[3].equals("confirm")) {
				p.performCommand("/schem save -f " + args[2]);
				Helper.removeOverWriteRequest(p);
				return true;
			}
			else if (args[3].equals("deny")) {
				if ((!schematicFile.exists() || schematicFile.isDirectory()) && (!schemFile.exists() || schemFile.isDirectory())) {
					return false;
				}
				Helper.removeOverWriteRequest(p);
				p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
				return true;
			}
			else {
				return false;
			}
		}
	}
}