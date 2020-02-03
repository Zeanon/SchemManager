package de.zeanon.schemmanager.worldeditmode.commands;


import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Load {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias, final @NotNull PlayerCommandPreprocessEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length < 3) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Missing argument for "
								  + ChatColor.YELLOW + "<"
								  + ChatColor.GOLD + "filename"
								  + ChatColor.YELLOW + ">");
					Load.loadUsage(p, slash, schemAlias);
				} else if (args[2].contains("./")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "File '" + args[2] + "'resolution error: Path is not allowed.");
					Load.loadUsage(p, slash, schemAlias);
				} else if (args.length > 4) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Load.loadUsage(p, slash, schemAlias);
				} else if (args.length > 3 && !Objects.notNull(ConfigUtils.getStringList("File Extensions")).contains(args[3])) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + " is no valid file format.");
					Formats.onFormats(p, true);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private void loadUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " load "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> ["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " load "
										+ ChatColor.GOLD + "example "
										+ ChatColor.YELLOW + "["
										+ ChatColor.DARK_PURPLE + "format"
										+ ChatColor.YELLOW + "]",
										slash + schemAlias + " load ", p);
	}
}