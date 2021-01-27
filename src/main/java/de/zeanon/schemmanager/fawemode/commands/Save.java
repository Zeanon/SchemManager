package de.zeanon.schemmanager.fawemode.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.fawemode.FastAsyncWorldEditMode;
import de.zeanon.schemmanager.fawemode.utils.FastAsyncWorldEditModeRequestUtils;
import de.zeanon.schemmanager.fawemode.utils.FastAsyncWorldEditModeSchemUtils;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
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

	public void execute(final @NotNull String @NotNull [] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias, final @NotNull PlayerCommandPreprocessEvent event) {
		if (!ConfigUtils.getBoolean("Save Function Override")) {
			if (args.length < 3) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Missing argument for "
							  + ChatColor.YELLOW + "<"
							  + ChatColor.GOLD + "filename"
							  + ChatColor.YELLOW + ">");
				Save.defaultSaveUsage(p, slash, schemAlias);
			} else if (args[2].contains("./")) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
				Save.defaultSaveUsage(p, slash, schemAlias);
			} else if (args.length > 4 && !args[2].equalsIgnoreCase("-f")) {
				event.setCancelled(true);
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
				Save.usage(p, slash, schemAlias);
			}
		} else {
			if (args.length < 3) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Missing argument for "
							  + ChatColor.YELLOW + "<"
							  + ChatColor.GOLD + "filename"
							  + ChatColor.YELLOW + ">");
				Save.usage(p, slash, schemAlias);
			} else if (args[2].contains("./")) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
				Save.usage(p, slash, schemAlias);
			} else if (args.length > 4 || (args.length == 4
										   && !FastAsyncWorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])
										   && !args[3].equalsIgnoreCase("confirm")
										   && !args[3].equalsIgnoreCase("deny"))) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Too many arguments.");
				Save.usage(p, slash, schemAlias);
			} else {
				event.setCancelled(true);
				Save.executeInternally(p, args);
			}
		}
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " save "
			   + ChatColor.YELLOW + "<"
			   + ChatColor.GOLD + "filename"
			   + ChatColor.YELLOW + ">";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " save "
			   + ChatColor.GOLD + "example";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " save ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String @NotNull [] args) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final @Nullable Path schemPath = FastAsyncWorldEditModeSchemUtils.getSchemPath();
				final @Nullable File file = schemPath != null
											? (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(args[2])) //NOSONAR
											   ? FastAsyncWorldEditModeSchemUtils.getSchemPath().resolve(args[2]).toFile()
											   : FastAsyncWorldEditModeSchemUtils.getSchemPath().resolve(
													   BaseFileUtils.removeExtension(args[2])).toFile())
											: null;
				final boolean fileExists = file != null && file.exists() && !file.isDirectory();

				if (args.length == 3) {
					try {
						Objects.notNull(FastAsyncWorldEditMode.getWorldEditPlugin()).getSession(p).getClipboard();
						FastAsyncWorldEditModeRequestUtils.addOverwriteRequest(p.getUniqueId(), args[2]);
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
					if (args[3].equalsIgnoreCase("confirm") && FastAsyncWorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
						FastAsyncWorldEditModeRequestUtils.removeOverWriteRequest(p.getUniqueId());
						new BukkitRunnable() {
							@Override
							public void run() {
								p.performCommand("/schem save -f " + args[2]);
							}
						}.runTask(SchemManager.getInstance());
					} else if (args[3].equalsIgnoreCase("deny") && FastAsyncWorldEditModeRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
						FastAsyncWorldEditModeRequestUtils.removeOverWriteRequest(p.getUniqueId());
						p.sendMessage(ChatColor.LIGHT_PURPLE + args[2] + " was not overwritten.");
					}
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
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

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										Save.usageMessage(slash, schemAlias),
										Save.usageHoverMessage(slash, schemAlias),
										Save.usageCommand(slash, schemAlias), p);
	}
}