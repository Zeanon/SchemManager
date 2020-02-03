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
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class DeleteFolder {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						DeleteFolder.deleteFolderUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						DeleteFolder.deleteFolderUsage(p, slash, schemAlias);
					} else if (args.length == 4
							   && !WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						DeleteFolder.deleteFolderUsage(p, slash, schemAlias);
					} else {
						DeleteFolder.onDeleteFolder(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					DeleteFolder.deleteFolderUsage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private void onDeleteFolder(final @NotNull Player p, final @NotNull String[] args) {
		final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
		final @Nullable File file = schemPath != null ? schemPath.resolve(args[2]).toFile() : null;
		final boolean fileExists = file != null && file.exists() && file.isDirectory();

		if (args.length == 3) {
			if (fileExists) {
				if (Objects.notNull(file.listFiles()).length > 0) {
					MessageUtils.sendInvertedCommandMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
															ChatColor.RED + " still contains files.",
															ChatColor.GREEN + args[2],
															ChatColor.RED + "Open "
															+ ChatColor.GREEN + args[2],
															"//schem list " + args[2], p);
				}
				MessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												ChatColor.RED + "Do you really want to delete "
												+ ChatColor.GREEN + args[2]
												+ ChatColor.RED + "?",
												"//schem delfolder " + args[2] + " confirm",
												"//schem delfolder " + args[2] + " deny", p);
				WorldEditModeRequestUtils.addDeleteFolderRequest(p, args[2]);
			} else {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
			}
		} else if (args.length == 4 && WorldEditModeRequestUtils.checkDeleteFolderRequest(p, args[2])) {
			if (args[3].equalsIgnoreCase("confirm")) {
				WorldEditModeRequestUtils.removeDeleteFolderRequest(p);
				if (fileExists) {
					try {
						FileUtils.deleteDirectory(file);
						@Nullable String parentName = DeleteFolder.getParentName(file);
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GREEN + args[2] +
									  ChatColor.RED + " was deleted successfully.");
						if (parentName != null) {
							p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
										  ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted successfully due to being empty.");
						}
					} catch (IOException e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
									  ChatColor.GREEN + args[2] + ChatColor.RED + " could not be deleted, for further information please see [console].");
					}
				} else {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GREEN + args[2] + ChatColor.RED + " does not exist.");
				}
			} else if (args[3].equalsIgnoreCase("deny")) {
				WorldEditModeRequestUtils.removeDeleteFolderRequest(p);
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GREEN + args[2] + ChatColor.RED + " was not deleted.");
			}
		}
	}

	private @Nullable String getParentName(final @NotNull File file) {
		@Nullable String parentName = null;
		if (ConfigUtils.getBoolean("Delete empty Folders")
			&& !file.getAbsoluteFile().getParentFile().equals(WorldEditModeSchemUtils.getSchemFolder())) {
			parentName = Objects.notNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0
						 ? null
						 : InternalFileUtils.deleteEmptyParent(file);
			if (file.getName().equals(parentName)) {
				parentName = null;
			}
		}
		return parentName;
	}

	private void deleteFolderUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " deletefolder "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GREEN + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " deletefolder "
										+ ChatColor.GREEN + "example",
										slash + schemAlias + " deletefolder ", p);
	}
}