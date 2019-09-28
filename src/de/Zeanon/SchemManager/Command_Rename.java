package de.Zeanon.SchemManager;

import java.io.File;
import java.io.IOException;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Command_Rename {

	public static boolean onRename(Player p, String[] args) {
		File schematicFile_old = new File(Helper.getSchemPath() + args[2] + ".schematic");
		File schemFile_old = new File(Helper.getSchemPath() + args[2] + ".schem");
		File schematicFile_new = new File(Helper.getSchemPath() + args[3] + ".schematic");
		File schemFile_new = new File(Helper.getSchemPath() + args[3] + ".schem");
		
		if (args.length == 4) {
			if ((!schematicFile_old.exists() || schematicFile_old.isDirectory()) && (!schemFile_old.exists() || schemFile_old.isDirectory())) {
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				return false;
			}
			if ((schematicFile_new.exists() && !schematicFile_new.isDirectory()) || (schemFile_new.exists() && !schemFile_new.isDirectory())) {
				p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
			}
			Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem rename " + args[2] + " " + args[3] + " confirm", "//schem rename " + args[2] + " " + args[3] + " deny", p);
			Helper.addRenameRequest(p, args[2]);
			return true;
		}
		
		
		if (args.length == 5 && Helper.checkRenameRequest(p, args[2])) {
			if (args[4].equals("confirm")) {
				if ((!schematicFile_old.exists() || schematicFile_old.isDirectory()) && (!schemFile_old.exists() || schemFile_old.isDirectory())) {
					return false;
				}
				Helper.removeRenameRequest(p);
				if ((schematicFile_old.exists() && !schematicFile_old.isDirectory()) || (schemFile_old.exists() && !schemFile_old.isDirectory())) {
					if (schematicFile_old.exists()) {
						try {
							if ((schematicFile_new.exists() && !schematicFile_new.isDirectory())) {
								schematicFile_new.delete();
							}
							FileUtils.moveFile(schematicFile_old, schematicFile_new);
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was renamed successfully.");
							return true;
						} catch (IOException e) {
							e.printStackTrace();
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
							return false;
						}
					}
					if (schemFile_old.exists()) {
						try {
							if ((schemFile_new.exists() && !schemFile_new.isDirectory())) {
								schemFile_new.delete();
							}
							FileUtils.moveFile(schemFile_old, schemFile_new);
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was renamed successfully.");
							return true;
						} catch (IOException e) {
							e.printStackTrace();
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
							return false;
						}
					}
				}
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be renamed.");
				return false;
			}
			if (args[4].equals("deny")) {
				if ((!schematicFile_old.exists() || schematicFile_old.isDirectory()) && (!schemFile_old.exists() || schemFile_old.isDirectory())) {
					return false;
				}
				Helper.removeRenameRequest(p);
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not renamed.");
				return true;
			}
		}
		return false;
	}
}