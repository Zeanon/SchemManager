package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.InternalFileUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.schemmanager.plugin.utils.commands.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
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

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length <= 4) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						Delete.usage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args[2].contains(".\\")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "' resolution error: Path is not allowed.");
						Delete.usage(p, slash, schemAlias);
					} else if (args.length == 4 && !CommandRequestUtils.checkDeleteFolderRequest(p.getUniqueId(), args[2])
							   && !args[3].equalsIgnoreCase("-confirm")
							   && !args[3].equalsIgnoreCase("-deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Delete.usage(p, slash, schemAlias);
					} else {
						Delete.executeInternally(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Delete.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " delete "
			   + ChatColor.YELLOW + "<"
			   + ChatColor.GOLD + "filename"
			   + ChatColor.YELLOW + ">";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " delete "
			   + ChatColor.GOLD + "example";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " delete ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String[] args) {
		final @Nullable Path schemPath = SchemUtils.getSchemPath();
		final @Nullable List<File> files = InternalFileUtils.getExistingFiles(schemPath.resolve(args[2]));

		if (args.length == 3) {
			if (!files.isEmpty()) {
				GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
													  ChatColor.RED + "Do you really want to delete "
													  + ChatColor.GOLD + args[2]
													  + ChatColor.RED + "?",
													  "//schem del " + args[2] + " -confirm",
													  "//schem del " + args[2] + " -deny", p);
				CommandRequestUtils.addDeleteRequest(p.getUniqueId(), args[2]);
			} else {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
			}
		} else if (args.length == 4 && CommandRequestUtils.checkDeleteRequest(p.getUniqueId(), args[2])) {
			if (args[3].equalsIgnoreCase("-confirm")) {
				CommandRequestUtils.removeDeleteRequest(p.getUniqueId());
				if (!files.isEmpty()) {
					@Nullable String parentName = null;
					for (final @NotNull File file : files) {
						try {
							Files.delete(file.toPath());
							parentName = Objects.notNull(file.getAbsoluteFile().getParentFile().listFiles()).length == 0
										 && ConfigUtils.getBoolean("Delete empty Folders") ? InternalFileUtils.deleteEmptyParent(file, SchemUtils.getSchemFolder()) : null;
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
			} else if (args[3].equalsIgnoreCase("-deny")) {
				CommandRequestUtils.removeDeleteRequest(p.getUniqueId());
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " was not deleted.");
			}
		}
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  Delete.usageMessage(slash, schemAlias),
											  Delete.usageHoverMessage(slash, schemAlias),
											  Delete.usageCommand(slash, schemAlias), p);
	}
}