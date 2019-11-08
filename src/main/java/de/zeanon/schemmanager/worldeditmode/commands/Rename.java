package de.zeanon.schemmanager.worldeditmode.commands;

import de.leonhard.storage.internal.utils.LightningFileUtils;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Rename {

	public static void onRename(final Player p, final String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				ArrayList<File> oldFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2])) : null;
				ArrayList<File> newFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[3])) : null;
				final boolean oldFileExists = oldFiles != null && oldFiles.size() > 0;
				final boolean newFileExists = newFiles != null && newFiles.size() > 0;

				if (args.length == 4) {
					if (oldFileExists) {
						if (newFileExists) {
							p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
						}

						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?", "//schem rename " + args[2] + " " + args[3] + " confirm", "//schem rename " + args[2] + " " + args[3] + " deny", p);
						WorldEditModeRequestUtils.addRenameRequest(p, args[2]);
					} else {
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
					}
				} else if (args.length == 5 && WorldEditModeRequestUtils.checkRenameRequest(p, args[2])) {
					if (args[4].equalsIgnoreCase("confirm")) {
						WorldEditModeRequestUtils.removeRenameRequest(p);
						if (oldFileExists) {
							moveFile(p, args[2], oldFiles, newFiles, schemPath.resolve(args[3]));
						} else {
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
						}
					} else if (args[4].equalsIgnoreCase("deny")) {
						WorldEditModeRequestUtils.removeRenameRequest(p);
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not renamed.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static void moveFile(final Player p, final String fileName, final ArrayList<File> oldFiles, final ArrayList<File> newFiles, final Path destPath) {
		try {
			if (newFiles != null) {
				for (File file : newFiles) {
					if (!file.delete()) {
						p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
						return;
					}
				}
			}
			String parentName = null;
			for (File file : oldFiles) {
				if (ConfigUtils.getStringList("File Extensions").stream().noneMatch(LightningFileUtils.getExtension(destPath)::equals)) {
					FileUtils.moveFile(file, new File(destPath + LightningFileUtils.getExtension(file)));
					parentName = Objects.requireNonNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0 || ConfigUtils.getBoolean("Delete empty Folders") ? null : InternalFileUtils.deleteEmptyParent(file);
				} else {
					FileUtils.moveFile(file, destPath.toFile());
					parentName = Objects.requireNonNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0 || ConfigUtils.getBoolean("Delete empty Folders") ? null : InternalFileUtils.deleteEmptyParent(file);
				}
			}
			p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " was renamed successfully.");
			if (parentName != null) {
				p.sendMessage(ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted successfully due to being empty.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed.");
		}
	}
}