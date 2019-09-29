package de.zeanon.schemmanager.WorldEdit.Commands;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import de.zeanon.schemmanager.WorldEdit.helper.Helper;
import net.md_5.bungee.api.ChatColor;

public class DeleteFolder {

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
			else {
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				return false;
			}
		}

		
		else if (args.length == 4 && Helper.checkDeleteFolderRequest(p, args[2])) {
			if (args[3].equals("confirm")) {
				if (!file.exists() || !file.isDirectory()) {
					p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted.");
					Helper.removeDeleteFolderRequest(p);
					return false;
				}
				else {
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
			}
			else if (args[3].equals("deny")) {
				Helper.removeDeleteFolderRequest(p);
				p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
				return true;
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