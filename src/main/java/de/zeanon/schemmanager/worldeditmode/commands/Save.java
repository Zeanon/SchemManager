package de.zeanon.schemmanager.worldeditmode.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Save {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias, final @NotNull PlayerCommandPreprocessEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!ConfigUtils.getBoolean("Save Function Override")) {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						Save.defaultSaveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						Save.defaultSaveUsage(p, slash, schemAlias);
					} else if (args.length > 4 && !args[2].equalsIgnoreCase("-f")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Save.defaultSaveUsage(p, slash, schemAlias);
					}
				} else if (args.length > 2 && args.length < 5 && args[2].equalsIgnoreCase("-f")) {
					if (args.length == 3) {
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						Save.saveUsage(p, slash, schemAlias);
					}
				} else {
					event.setCancelled(true);
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						Save.saveUsage(p, slash, schemAlias);
					} else if (args[2].contains("./")) {
						p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
						Save.saveUsage(p, slash, schemAlias);
					} else if (args.length > 4 || (args.length == 4
												   && !WorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])
												   && !args[3].equalsIgnoreCase("confirm")
												   && !args[3].equalsIgnoreCase("deny"))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Save.saveUsage(p, slash, schemAlias);
					} else {
						Save.onSave(p, args);
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private void onSave(final @NotNull Player p, final @NotNull String[] args) {
		final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
		final @Nullable File file = schemPath != null
									? (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(args[2])) //NOSONAR
									   ? WorldEditModeSchemUtils.getSchemPath().resolve(args[2]).toFile()
									   : WorldEditModeSchemUtils.getSchemPath().resolve(
											   BaseFileUtils.removeExtension(args[2])).toFile())
									: null;
		final boolean fileExists = file != null && file.exists() && !file.isDirectory();

		if (args.length == 3) {
			try {
				Objects.notNull(WorldEditMode.getWorldEditPlugin()).getSession(p).getClipboard();
				WorldEditModeRequestUtils.addOverwriteRequest(p.getUniqueId(), args[2]);
				if (fileExists) {
					p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
					MessageUtils.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?",
													"//schem save " + args[2] + " confirm",
													"//schem save " + args[2] + " deny", p);
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
			if (args[3].equalsIgnoreCase("confirm") && WorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
				WorldEditModeRequestUtils.removeOverWriteRequest(p.getUniqueId());
				new BukkitRunnable() {
					@Override
					public void run() {
						p.performCommand("/schem save -f " + args[2]);
					}
				}.runTask(SchemManager.getInstance());
			} else if (args[3].equalsIgnoreCase("deny") && WorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
				WorldEditModeRequestUtils.removeOverWriteRequest(p.getUniqueId());
				p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
			}
		}
	}

	private void defaultSaveUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "-f"
										+ ChatColor.YELLOW + "] <"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "[" + ChatColor.DARK_PURPLE + "-f" + ChatColor.YELLOW + "]"
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " save ", p);
	}

	private void saveUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " save "
										+ ChatColor.GOLD + "example",
										slash + schemAlias + " save ", p);
	}
}