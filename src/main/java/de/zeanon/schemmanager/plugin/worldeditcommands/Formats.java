package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Formats {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		if (args.length == 2) {
			Formats.executeInternally(p, false);
		} else {
			p.sendMessage(ChatColor.RED + "Too many arguments.");
			Formats.usage(p, slash, schemAlias);
		}
	}

	public void executeInternally(final @NotNull Player p, final boolean suppressBlankLine) {
		if (ConfigUtils.getBoolean("Space Lists") && !suppressBlankLine) {
			p.sendMessage("");
		}
		p.sendMessage(ChatColor.RED + "Available clipboard formats:");
		if (!Objects.notNull(ConfigUtils.getStringList("File Extensions")).isEmpty()) {
			final @NotNull String[] formats = Objects.notNull(ConfigUtils.getStringList("File Extensions")).toArray(new String[0]);
			final @NotNull StringBuilder pathBuilder = new StringBuilder(ChatColor.LIGHT_PURPLE + formats[0] + ChatColor.AQUA + ", ");
			for (int i = 1; i < formats.length - 1; i++) {
				pathBuilder.append(ChatColor.LIGHT_PURPLE).append(formats[i]).append(ChatColor.AQUA).append(", ");
			}
			pathBuilder.append(ChatColor.LIGHT_PURPLE).append(formats[formats.length - 1]);
			p.sendMessage(pathBuilder.toString());
		} else {
			p.sendMessage(ChatColor.LIGHT_PURPLE + "schem"
						  + ChatColor.AQUA + ", "
						  + ChatColor.LIGHT_PURPLE + "schematic");
		}
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " formats";
	}

	public @NotNull String usageHoverMessage() {
		return ChatColor.DARK_BLUE + ""
			   + ChatColor.UNDERLINE + ""
			   + ChatColor.ITALIC + ""
			   + ChatColor.BOLD + "There are different formats? :O";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " formats";
	}

	private void usage(final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  Formats.usageMessage(slash, schemAlias),
											  Formats.usageHoverMessage(),
											  Formats.usageCommand(slash, schemAlias), p);
	}
}