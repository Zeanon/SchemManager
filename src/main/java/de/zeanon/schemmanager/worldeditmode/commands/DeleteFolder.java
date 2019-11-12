package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteFolder {

	public static void onDeleteFolder(final Player p, final String[] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Path schemPath = WorldEditModeSchemUtils.getSchemPath();
				File file = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
				final boolean fileExists = file != null && file.exists() && file.isDirectory();

				if (args.length == 3) {
					if (fileExists) {
						if (Objects.requireNonNull(file.listFiles()).length > 0) {
							MessageUtils.sendInvertedCommandMessage(ChatColor.RED + " still contains files.",
																	ChatColor.GREEN + args[2],
																	ChatColor.RED + "Open "
																	+ ChatColor.GREEN + args[2],
																	"//schem list " + args[2], p);
						}
						MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to delete "
														+ ChatColor.GREEN + args[2]
														+ ChatColor.RED + "?",
														"//schem delfolder " + args[2] + " confirm",
														"//schem delfolder " + args[2] + " deny", p);
						WorldEditModeRequestUtils.addDeleteFolderRequest(p, args[2]);
					} else {
						p.sendMessage(ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
					}
				} else if (args.length == 4 && WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])) {
					if (args[3].equalsIgnoreCase("confirm")) {
						WorldEditModeRequestUtils.removeDeleteFolderRequest(p);
						if (fileExists) {
							try {
								FileUtils.deleteDirectory(file);
								String parentName = getParentName(file);
								p.sendMessage(ChatColor.GREEN + args[2]
											  + ChatColor.RED + " was deleted successfully.");
								if (parentName != null) {
									p.sendMessage(ChatColor.RED + "Folder "
												  + ChatColor.GREEN + parentName
												  + ChatColor.RED + " was deleted successfully due to being empty.");
								}
							} catch (IOException e) {
								e.printStackTrace();
								p.sendMessage(ChatColor.GREEN + args[2]
											  + ChatColor.RED + " could not be deleted.");
							}
						} else {
							p.sendMessage(ChatColor.GREEN + args[2]
										  + ChatColor.RED + " does not exist.");
						}
					} else if (args[3].equalsIgnoreCase("deny")) {
						WorldEditModeRequestUtils.removeDeleteFolderRequest(p);
						p.sendMessage(ChatColor.GREEN + args[2]
									  + ChatColor.RED + " was not deleted.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private static String getParentName(final File file) {

		String parentName = null;
		if (ConfigUtils.getBoolean("Delete empty Folders")
			&& !file.getAbsoluteFile().getParentFile().equals(WorldEditModeSchemUtils.getSchemFolder())) {
			parentName = Objects.requireNonNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0
						 ? null
						 : InternalFileUtils.deleteEmptyParent(file);
		}
		return parentName;
	}
}