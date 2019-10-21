package de.zeanon.schemmanager.worldeditversion.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import java.io.File;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Save {

	public static void onSave(final Player p, final String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Path schemPath = WorldEditVersionSchemUtils.getSchemPath();
				File file = schemPath != null ? (args[2].endsWith(".schem") ? WorldEditVersionSchemUtils.getSchemPath().resolve(args[2]).toFile() : WorldEditVersionSchemUtils.getSchemPath().resolve(args[2] + ".schem").toFile()) : null;
				final boolean fileExists = file != null && file.exists() && !file.isDirectory();

				if (args.length == 3) {
					try {
						WorldEditVersionSchemUtils.getWorldEditPlugin().getSession(p).getClipboard();
						WorldEditVersionRequestUtils.addOverwriteRequest(p, args[2]);
						if (fileExists) {
							p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
							MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem save " + args[2] + " confirm", "//schem save " + args[2] + " deny", p);
						} else {
							p.performCommand("/schem save -f " + args[2]);
						}
					} catch (EmptyClipboardException e) {
						p.sendMessage(ChatColor.RED + "Your clipboard is empty. Use //copy first.");
					}
				} else {
					if (args[3].equalsIgnoreCase("confirm") && WorldEditVersionRequestUtils.checkOverWriteRequest(p, args[2])) {
						WorldEditVersionRequestUtils.removeOverWriteRequest(p);
						p.performCommand("/schem save -f " + args[2]);
					} else if (args[3].equalsIgnoreCase("deny") && WorldEditVersionRequestUtils.checkOverWriteRequest(p, args[2])) {
						WorldEditVersionRequestUtils.removeOverWriteRequest(p);
						p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}