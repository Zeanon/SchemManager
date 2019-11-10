package de.zeanon.schemmanager.worldeditmode.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
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
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				File file = schemPath != null ? (args[2].endsWith(".schem") ? WorldEditModeSchemUtils.getSchemPath().resolve(args[2]).toFile() : WorldEditModeSchemUtils.getSchemPath().resolve(args[2] + ".schem").toFile()) : null;
				final boolean fileExists = file != null && file.exists() && !file.isDirectory();

				if (args.length == 3) {
					try {
						WorldEditMode.getWorldEditPlugin().getSession(p).getClipboard();
						WorldEditModeRequestUtils.addOverwriteRequest(p, args[2]);
						if (fileExists) {
							p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
							MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem save " + args[2] + " confirm", "//schem save " + args[2] + " deny", p);
						} else {
							new BukkitRunnable() {
								@Override
								public void run() {
									p.performCommand("/schem save -f " + args[2]);
								}
							}.runTask(SchemManager.getInstance());
						}
					} catch (EmptyClipboardException e) {
						p.sendMessage(ChatColor.RED + "Your clipboard is empty. Use //copy first.");
					}
				} else {
					if (args[3].equalsIgnoreCase("confirm") && WorldEditModeRequestUtils.checkOverWriteRequest(p, args[2])) {
						WorldEditModeRequestUtils.removeOverWriteRequest(p);
						new BukkitRunnable() {
							@Override
							public void run() {
								p.performCommand("/schem save -f " + args[2]);
							}
						}.runTask(SchemManager.getInstance());
					} else if (args[3].equalsIgnoreCase("deny") && WorldEditModeRequestUtils.checkOverWriteRequest(p, args[2])) {
						WorldEditModeRequestUtils.removeOverWriteRequest(p);
						p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}