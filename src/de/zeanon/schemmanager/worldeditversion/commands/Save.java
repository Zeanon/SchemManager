package de.zeanon.schemmanager.worldeditversion.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

public class Save {

	public static boolean onSave(Player p, String[] args) {
		File file = new File(Helper.getSchemPath() + args[2] + ".schem");
		final boolean fileExists = file.exists() && !file.isDirectory();

		LocalSession session = Helper.we.getSession(p);
		if (args.length == 3) {
			try {
				session.getClipboard();
				Helper.addOverwriteRequest(p, args[2]);
				if (fileExists) {
					p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
					Helper.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem save " + args[2] + " confirm", "//schem save " + args[2] + " deny", p);
					return true;
				} else {
					p.performCommand("/schem save -f " + args[2]);
					return true;
				}
			} catch (EmptyClipboardException e) {
				p.sendMessage(ChatColor.RED + "Your clipboard is empty. Use //copy first.");
				return false;
			}
		} else {
			if (args[3].equals("confirm") && Helper.checkOverWriteRequest(p, args[2])) {
				Helper.removeOverWriteRequest(p);
				p.performCommand("/schem save -f " + args[2]);
				return true;
			} else if (args[3].equals("deny") && Helper.checkOverWriteRequest(p, args[2])) {
				Helper.removeOverWriteRequest(p);
				p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
				return true;
			} else {
				return false;
			}
		}
	}
}