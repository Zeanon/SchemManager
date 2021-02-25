package de.zeanon.schemmanager.plugin.commands;


import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Load {

	public void execute(final @NotNull String @NotNull [] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias, final @NotNull PlayerCommandPreprocessEvent event) {
		if (args.length < 3) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "Missing argument for "
						  + ChatColor.YELLOW + "<"
						  + ChatColor.GOLD + "filename"
						  + ChatColor.YELLOW + ">");
			Load.usage(p, slash, schemAlias);
		} else if (args[2].contains("./")) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "File '" + args[2] + "' resolution error: Path is not allowed.");
			Load.usage(p, slash, schemAlias);
		} else if (args.length > 4) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "Too many arguments.");
			Load.usage(p, slash, schemAlias);
		} else if (args.length > 3 && !Objects.notNull(ConfigUtils.getStringList("File Extensions")).contains(args[3])) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + ChatColor.RED + " is no valid file format.");
			Formats.executeInternally(p, true);
		}
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " load "
			   + ChatColor.YELLOW + "<"
			   + ChatColor.GOLD + "filename"
			   + ChatColor.YELLOW + "> ["
			   + ChatColor.DARK_PURPLE + "format"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " load "
			   + ChatColor.GOLD + "example "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "format"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " load ";
	}

	private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  Load.usageMessage(slash, schemAlias),
											  Load.usageHoverMessage(slash, schemAlias),
											  Load.usageCommand(slash, schemAlias), p);
	}
}