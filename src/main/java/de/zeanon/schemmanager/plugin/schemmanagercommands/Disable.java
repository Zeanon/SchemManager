package de.zeanon.schemmanager.plugin.schemmanagercommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.RunningMode;
import de.zeanon.schemmanager.plugin.utils.commands.CommandConfirmation;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalRequestUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Disable {

	public void execute(final @NotNull CommandSender sender, final @Nullable CommandConfirmation confirmation) {
		if (sender instanceof Player) {
			final @NotNull Player p = (Player) sender;

			if (confirmation == null) {
				GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
													  + ChatColor.RED + "Do you really want to disable "
													  + ChatColor.DARK_RED + SchemManager.getInstance().getName()
													  + ChatColor.RED + "? "
						, "/sm disable -confirm"
						, "/sm disable -deny"
						, p);
				GlobalRequestUtils.addDisableRequest(p.getUniqueId());
			} else {
				if (GlobalRequestUtils.checkDisableRequest(p.getUniqueId())) {
					GlobalRequestUtils.removeDisableRequest(p.getUniqueId());
					if (confirmation.confirm()) {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
									  + ChatColor.RED + "is being disabled.");
						if (RunningMode.isPaperSpigot()) {
							SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						} else {
							new BukkitRunnable() {
								@Override
								public void run() {
									SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
								}
							}.runTask(SchemManager.getInstance());
						}
					} else {
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
									  + ChatColor.RED + "will not be disabled.");
					}
				} else {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
								  + ChatColor.RED + "You don't have a pending disable request.");
				}
			}
		} else {
			if (confirmation == null) {
				GlobalRequestUtils.addConsoleDisableRequest();
				System.out.println("To disable type 'schemmanager disable confirm', otherwise type 'schemmanager disable deny'");
			} else {
				if (GlobalRequestUtils.checkConsoleDisableRequest()) {
					GlobalRequestUtils.removeConsoleDisableRequest();
					if (confirmation.confirm()) {
						if (RunningMode.isPaperSpigot()) {
							SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						} else {
							new BukkitRunnable() {
								@Override
								public void run() {
									SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
								}
							}.runTask(SchemManager.getInstance());
						}
					} else {
						System.out.println("SchemManager will not be disabled.");
					}
				}
			}
		}
	}

	public void usage(final @NotNull CommandSender sender, final @NotNull String... args) {
		if (sender instanceof Player) {
			final @NotNull Player p = (Player) sender;

			if (args.length > 0) {
				p.sendMessage(ChatColor.RED + "Too many arguments.");
			}

			GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
												  ChatColor.GRAY + "/schemmanager"
												  + ChatColor.AQUA + " disable",
												  ChatColor.DARK_RED + ""
												  + ChatColor.UNDERLINE + ""
												  + ChatColor.ITALIC + ""
												  + ChatColor.BOLD + "PLS DON'T D;",
												  "/schemmanager disable", p);
		}
	}
}
