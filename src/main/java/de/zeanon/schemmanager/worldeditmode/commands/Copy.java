package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.internal.utils.SMFileUtils;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Copy {

	public static void onCopy(@NotNull final Player p, @NotNull final String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				List<File> oldFiles = schemPath != null
									  ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2]))
									  : null;
				List<File> newFiles = schemPath != null
									  ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[3]))
									  : null;
				final boolean oldFileExists = oldFiles != null && !oldFiles.isEmpty();
				final boolean newFileExists = newFiles != null && !newFiles.isEmpty();

				if (args.length == 4) {
					if (oldFileExists) {
						if (newFileExists) {
							p.sendMessage(ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
						}

						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to copy "
														+ ChatColor.GOLD + args[2]
														+ ChatColor.RED + "?",
														"//schem copy " + args[2] + " " + args[3] + " confirm",
														"//schem copy " + args[2] + " " + args[3] + " deny", p);
						WorldEditModeRequestUtils.addCopyRequest(p, args[2]);
					} else {
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
					}
				} else if (args.length == 5 && WorldEditModeRequestUtils.checkCopyRequest(p, args[2])) {
					if (args[4].equalsIgnoreCase("confirm")) {
						WorldEditModeRequestUtils.removeCopyRequest(p);
						if (oldFileExists) {
							copyFile(p, args[2], oldFiles, newFiles, schemPath.resolve(args[3]));
						} else {
							p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
						}
					} else if (args[4].equalsIgnoreCase("deny")) {
						WorldEditModeRequestUtils.removeCopyRequest(p);
						p.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RED + " was not copied.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}


	private static void copyFile(@NotNull final Player p, final String fileName, @NotNull final List<File> oldFiles, @Nullable final List<File> newFiles, @NotNull final Path destPath) {
		try {
			if (newFiles != null) {
				for (File file : newFiles) {
					Files.delete(file.toPath());
				}
			}
			for (File file : oldFiles) {
				if (Objects.notNull(ConfigUtils.getStringList("File Extensions"))
						   .stream()
						   .noneMatch(SMFileUtils.getExtension(destPath.toString())::equals)) {
					FileUtils.copyFile(file, new File(destPath.toString() + SMFileUtils.getExtension(file.getName())));
				} else {
					FileUtils.copyFile(file, destPath.toFile());
				}
			}
			p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " was copied successfully.");
		} catch (IOException e) {
			e.printStackTrace();
			p.sendMessage(ChatColor.GOLD + fileName + ChatColor.RED + " could not be copied.");
		}
	}
}