package de.zeanon.schemmanager.plugin.utils.guards;

import de.steamwar.commandframework.GuardCheckType;
import de.steamwar.commandframework.GuardChecker;
import de.steamwar.commandframework.GuardResult;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class UpdateGuard implements GuardChecker {

	@Override
	public GuardResult guard(final CommandSender commandSender, final GuardCheckType guardCheckType, final String[] previousArguments, final String s) {
		if (commandSender instanceof Player) {
			final @NotNull Player p = (Player) commandSender;

			if (!p.hasPermission("schemmanager.update")) {
				if (guardCheckType == GuardCheckType.COMMAND) {
					p.sendMessage(ChatColor.RED + "You don't have permission to perform this command.");
				}
				return GuardResult.DENIED;
			} else {
				return GuardResult.ALLOWED;
			}
		} else {
			return GuardResult.ALLOWED;
		}
	}
}