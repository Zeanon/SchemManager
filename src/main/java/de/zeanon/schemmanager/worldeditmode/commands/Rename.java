package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.internal.utility.basic.BaseFileUtils;
import de.zeanon.storage.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Rename {

	public static void onRename(final @NotNull Player p, final @NotNull String[] args) {
		new BukkitRunnable() {
			@SuppressWarnings("DuplicatedCode")
			@Override
			public void run() {
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				@Nullable List<File> oldFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2])) : null;
				@Nullable List<File> newFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[3])) : null;
				final boolean oldFileExists = oldFiles != null && !oldFiles.isEmpty();
				final boolean newFileExists = newFiles != null && !newFiles.isEmpty();

				if (args.length == 4) {
					if (oldFileExists) {
						if (newFileExists) {
							p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
						}

						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?",
														"//schem rename " + args[2] + " " + args[3] + " confirm",
														"//schem rename " + args[2] + " " + args[3] + " deny", p);
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


	private static void moveFile(final @NotNull Player p, final String fileName, final @NotNull List<File> oldFiles, @Nullable final List<File> newFiles, final @NotNull Path destPath) {
		try {
			if (newFiles != null) {
				for (@NotNull File file : newFiles) {
					Files.delete(file.toPath());
				}
			}
			@Nullable String parentName = null;
			for (@NotNull File file : oldFiles) {
				if (Objects.notNull(ConfigUtils.getStringList("File Extensions")).stream().noneMatch(BaseFileUtils.getExtension(destPath)::equals)) {
					FileUtils.moveFile(file, new File(destPath + BaseFileUtils.getExtension(file)));
					parentName = Objects.notNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0
								 || ConfigUtils.getBoolean("Delete empty Folders") ? null : InternalFileUtils.deleteEmptyParent(file);
					if (file.getName().equals(parentName)) {
						parentName = null;
					}
				} else {
					FileUtils.moveFile(file, destPath.toFile());
					parentName = Objects.notNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0
								 || ConfigUtils.getBoolean("Delete empty Folders") ? null : InternalFileUtils.deleteEmptyParent(file);
					if (file.getName().equals(parentName)) {
						parentName = null;
					}
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