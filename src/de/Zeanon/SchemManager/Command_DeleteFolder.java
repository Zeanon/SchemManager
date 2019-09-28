package de.Zeanon.SchemManager;

import java.io.File;
import java.io.IOException;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Command_DeleteFolder {

	public static boolean onDeleteFolder(Player p, String[] args) {
		File file = new File(Helper.getSchemPath() + args[2]);
		
		if (args.length == 3) {
			if (file.exists() && file.isDirectory()) {
				if (file.listFiles().length > 0) {
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " still contains files.");
				}
				Helper.sendBooleanMessage(ChatColor.RED + "Do you really want to delete " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem delfolder " + args[2] + " confirm", "//schem delfolder " + args[2] + " deny", p);
				Helper.addDeleteFolderRequest(p, args[2]);
				return true;
			}
			if (!file.exists() || !file.isDirectory()) {
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				return false;
			}
		}
		
		
		if (args.length == 4 && Helper.checkDeleteFolderRequest(p, args[2])) {
			if (args[3].equals("confirm")) {
				if (!file.exists() || !file.isDirectory()) {
					return false;
				}
				Helper.removeDeleteFolderRequest(p);
				if (file.exists() && file.isDirectory()) {
					try {
						FileUtils.deleteDirectory(file);
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
						return true;
					} catch (IOException e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
						return false;
					}
				}
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
				return false;
			}
			if (args[3].equals("deny")) {
				if (!file.exists() || !file.isDirectory()) {
					return false;
				}
				Helper.removeDeleteFolderRequest(p);
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
				return true;
			}
		}
		return false;
	}
}