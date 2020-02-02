package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Delete {

	public void onDelete(final @NotNull Player p, final @NotNull String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				@Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				@Nullable List<File> files = schemPath != null
											 ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2]))
											 : null;
				final boolean fileExists = files != null && !files.isEmpty();

				if (args.length == 3) {
					if (fileExists) {
						MessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
														ChatColor.RED + "Do you really want to delete "
														+ ChatColor.GOLD + args[2]
														+ ChatColor.RED + "?",
														"//schem del " + args[2] + " confirm",
														"//schem del " + args[2] + " deny", p);
						WorldEditModeRequestUtils.addDeleteRequest(p, args[2]);
					} else {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
					}
				} else if (args.length == 4 && WorldEditModeRequestUtils.checkDeleteRequest(p, args[2])) {
					if (args[3].equalsIgnoreCase("confirm")) {
						WorldEditModeRequestUtils.removeDeleteRequest(p);
						if (fileExists) {
							@Nullable String parentName = null;
							for (@NotNull File file : files) {
								try {
									Files.delete(file.toPath());
									if (ConfigUtils.getBoolean("Delete empty Folders")
										&& !file.getAbsoluteFile().getParentFile()
												.equals(WorldEditModeSchemUtils.getSchemFolder())) {
										parentName = Objects.notNull(
												file.getAbsoluteFile().getParentFile().listFiles()).length > 0
													 ? null
													 : InternalFileUtils.deleteEmptyParent(file);
										if (file.getName().equals(parentName)) {
											parentName = null;
										}
									}
								} catch (IOException e) {
									p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												  ChatColor.GOLD + args[2] + ChatColor.RED + " could not be deleted, for further information please see [console].");
									return;
								}
							}
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GOLD + args[2] + ChatColor.RED + " was deleted successfully.");
							if (parentName != null) {
								p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
											  ChatColor.RED + "Folder "
											  + ChatColor.GREEN + parentName
											  + ChatColor.RED + " was deleted successfully due to being empty.");
							}
						} else {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
						}
					} else if (args[3].equalsIgnoreCase("deny")) {
						WorldEditModeRequestUtils.removeDeleteRequest(p);
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}