package de.zeanon.schemmanager.plugin.worldeditcommands;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.session.SessionManager;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class SearchSession {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length <= 4) {
					if (args.length == 4 && (StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3]))) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						SearchSession.usage(p, slash);
					} else {
						SearchSession.executeInternally(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					SearchSession.usage(p, slash);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash) {
		return ChatColor.GRAY + slash + "session"
			   + ChatColor.AQUA + " search "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-c"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] <"
			   + ChatColor.GREEN + "sessionname"
			   + ChatColor.YELLOW + "> ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + "session"
			   + ChatColor.AQUA + " search "
			   + ChatColor.YELLOW + "["
			   + ChatColor.DARK_PURPLE + "-c"
			   + ChatColor.YELLOW + "] ["
			   + ChatColor.DARK_PURPLE + "-d"
			   + ChatColor.YELLOW + "] "
			   + ChatColor.GREEN + "example"
			   + ChatColor.YELLOW + " ["
			   + ChatColor.DARK_PURPLE + "page"
			   + ChatColor.YELLOW + "]";
	}

	public @NotNull String usageCommand(final @NotNull String slash) {
		return slash + "session search ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String[] args) {
		final int page;
		if (StringUtils.isNumeric(args[args.length - 1])) {
			page = Integer.parseInt(args[args.length - 1]);
		} else {
			page = 1;
		}
		SearchSession.printList(p, page, args[2]);
	}

	private void printList(final @NotNull Player p, final int page, final @NotNull String sequence) {
		int listmax = ConfigUtils.getInt("Listmax");
		final boolean spaceLists = ConfigUtils.getBoolean("Space Lists");

		final @Nullable Map<String, SessionManager.SessionHolder> sessions = WorldEdit.getInstance().getSessionManager().listSessions(new BukkitPlayer(p));
		final java.util.List<String> sessionList = sessions == null ? new GapList<>() : sessions.keySet().stream().filter(name -> !name.equals("current") && name.toLowerCase().startsWith(sequence)).collect(Collectors.toList());

		final double count = sessionList.size();
		final int pageAmount = (int) (((count / listmax) % 1 != 0) ? (count / listmax) + 1 : (count / listmax));

		if (pageAmount != 0 && page > pageAmount) {
			GlobalMessageUtils.sendHoverMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "]",
												ChatColor.RED + "There are only " + pageAmount + " pages of sessions in this list",
												"",
												ChatColor.GRAY + "schematics", p);
			return;
		}

		if (spaceLists) {
			p.sendMessage("");
		}

		if (sessionList.isEmpty()) {
			GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												ChatColor.AQUA + "No sessions found",
												ChatColor.AQUA + " ===",
												ChatColor.GRAY + "sessions", p);
		} else {
			GlobalMessageUtils.sendHoverMessage(ChatColor.AQUA + "=== ",
												ChatColor.AQUA + "" + (int) count + " Sessions | Page " + page + "/" + pageAmount,
												ChatColor.AQUA + " ===", ChatColor.GRAY + "sessions", p);
			int id = (page - 1) * listmax;

			if (count < listmax * page) {
				listmax = (int) count - (listmax * (page - 1));
			}

			for (int i = 0; i < listmax; i++) {
				SearchSession.sendListLine(p, id, sessionList.get(id));
				id++;
			}

			final int nextPage = page >= pageAmount ? 1 : page + 1;
			final int previousPage = (page <= 1 ? pageAmount : page - 1);
			if (pageAmount > 1) {
				GlobalMessageUtils.sendScrollMessage("//session list " + nextPage,
													 "//session list " + previousPage,
													 ChatColor.DARK_PURPLE + "Page " + nextPage,
													 ChatColor.DARK_PURPLE + "Page " + previousPage, p, ChatColor.DARK_AQUA);
			} else {
				GlobalMessageUtils.sendScrollMessage("",
													 "",
													 ChatColor.DARK_PURPLE + "There is only one page of sessions in this list",
													 ChatColor.DARK_PURPLE + "There is only one page of sessions in this list", p, ChatColor.BLUE);
			}
		}
	}

	private void sendListLine(final @NotNull Player p, final int id, final String name) {
		GlobalMessageUtils.sendCommandMessage(ChatColor.RED + Integer.toString(id + 1) + ": ",
											  ChatColor.GREEN + name,
											  ChatColor.RED + "Load " + ChatColor.GREEN + name + ChatColor.RED + " as your session.",
											  "//session load " + name, p);
	}

	private void usage(final @NotNull Player p, @NotNull final String slash) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  SearchSession.usageMessage(slash),
											  SearchSession.usageHoverMessage(slash),
											  SearchSession.usageCommand(slash), p);
	}
}