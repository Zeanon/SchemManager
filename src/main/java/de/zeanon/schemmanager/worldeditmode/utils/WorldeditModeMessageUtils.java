package de.zeanon.schemmanager.worldeditmode.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class WorldeditModeMessageUtils {

	public static void sendInvalidSubCommand(final @NotNull Player target, final String slash, final String schemAlias) {
		@NotNull final TextComponent base = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "Usage: "));
		@NotNull final TextComponent schem = new TextComponent(TextComponent.fromLegacyText(ChatColor.GRAY + slash + "schem"));
		@NotNull final TextComponent help = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "help"));
		@NotNull final TextComponent formats = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "formats"));
		@NotNull final TextComponent load = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "load"));
		@NotNull final TextComponent save = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "save"));
		@NotNull final TextComponent rename = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "rename"));
		@NotNull final TextComponent renamefolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "renamefolder"));
		@NotNull final TextComponent delete = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "delete"));
		@NotNull final TextComponent deletefolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "deletefolder"));
		@NotNull final TextComponent list = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "list"));
		@NotNull final TextComponent listfolders = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "listfolders"));
		@NotNull final TextComponent search = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "search"));
		@NotNull final TextComponent searchfolder = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "searchfolder"));
		schem.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " "));
		schem.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										   new ComponentBuilder(new TextComponent(
												   TextComponent.fromLegacyText(
														   ChatColor.RED + "e.g. "
														   + ChatColor.GRAY + "" + slash + "schem")))
												   .create()));
		help.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " help"));
		help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										  new ComponentBuilder(new TextComponent(
												  TextComponent.fromLegacyText(
														  ChatColor.LIGHT_PURPLE + ""
														  + ChatColor.UNDERLINE + ""
														  + ChatColor.ITALIC + ""
														  + ChatColor.BOLD + "OMG PLS HELP ME")))
												  .create()));
		formats.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " formats"));
		formats.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											 new ComponentBuilder(new TextComponent(
													 TextComponent.fromLegacyText(
															 ChatColor.BLUE + ""
															 + ChatColor.UNDERLINE + ""
															 + ChatColor.ITALIC + ""
															 + ChatColor.BOLD + "There are different formats? :O")))
													 .create()));
		load.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " load "));
		load.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										  new ComponentBuilder(new TextComponent(
												  TextComponent.fromLegacyText(
														  ChatColor.RED + "e.g. "
														  + ChatColor.GRAY + "" + slash + schemAlias + " "
														  + ChatColor.AQUA + "load"
														  + ChatColor.GOLD + " example "
														  + ChatColor.YELLOW + "["
														  + ChatColor.DARK_PURPLE + "format"
														  + ChatColor.YELLOW + "]")))
												  .create()));
		save.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " save "));
		save.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										  new ComponentBuilder(new TextComponent(
												  TextComponent.fromLegacyText(
														  ChatColor.RED + "e.g. " +
														  ChatColor.GRAY + "" + slash + schemAlias + " " +
														  ChatColor.AQUA + "save" +
														  ChatColor.GOLD + " example")))
												  .create()));
		rename.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " rename "));
		rename.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(new TextComponent(
													TextComponent.fromLegacyText(
															ChatColor.RED + "e.g. "
															+ ChatColor.GRAY + "" + slash + schemAlias + " "
															+ ChatColor.AQUA + "rename"
															+ ChatColor.GOLD + " example newname")))
													.create()));
		renamefolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " renamefolder "));
		renamefolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												  new ComponentBuilder(new TextComponent(
														  TextComponent.fromLegacyText(
																  ChatColor.RED + "e.g. "
																  + ChatColor.GRAY + "" + slash + schemAlias + " "
																  + ChatColor.AQUA + "renamefolder"
																  + ChatColor.GREEN + " example newname")))
														  .create()));
		delete.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " delete "));
		delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(new TextComponent(
													TextComponent.fromLegacyText(
															ChatColor.RED + "e.g. "
															+ ChatColor.GRAY + "" + slash + schemAlias + " "
															+ ChatColor.AQUA + "delete"
															+ ChatColor.GOLD + " example")))
													.create()));
		deletefolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " deletefolder "));
		deletefolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												  new ComponentBuilder(new TextComponent(
														  TextComponent.fromLegacyText(
																  ChatColor.RED + "e.g. "
																  + ChatColor.GRAY + "" + slash + schemAlias + " "
																  + ChatColor.AQUA + "deletefolder"
																  + ChatColor.GREEN + " example")))
														  .create()));
		list.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " list "));
		list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										  new ComponentBuilder(new TextComponent(
												  TextComponent.fromLegacyText(
														  ChatColor.RED + "e.g. "
														  + ChatColor.GRAY + "" + slash + schemAlias + " "
														  + ChatColor.AQUA + "list "
														  + ChatColor.YELLOW + "["
														  + ChatColor.DARK_PURPLE + "-d"
														  + ChatColor.YELLOW + "] ["
														  + ChatColor.GREEN + "folder"
														  + ChatColor.YELLOW + "] ["
														  + ChatColor.DARK_PURPLE + "page"
														  + ChatColor.YELLOW + "]")))
												  .create()));
		listfolders.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " listfolders "));
		listfolders.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												 new ComponentBuilder(new TextComponent(
														 TextComponent.fromLegacyText(
																 ChatColor.RED + "e.g. "
																 + ChatColor.GRAY + ""
																 + slash + schemAlias + " "
																 + ChatColor.AQUA + "listfolders "
																 + ChatColor.YELLOW + "["
																 + ChatColor.DARK_PURPLE + "-d"
																 + ChatColor.YELLOW + "] ["
																 + ChatColor.GREEN + "folder"
																 + ChatColor.YELLOW + "] ["
																 + ChatColor.DARK_PURPLE + "page"
																 + ChatColor.YELLOW + "]")))
														 .create()));
		search.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " search "));
		search.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(new TextComponent(
													TextComponent.fromLegacyText(
															ChatColor.RED + "e.g. "
															+ ChatColor.GRAY + "" + slash + schemAlias + " "
															+ ChatColor.AQUA + "search "
															+ ChatColor.YELLOW + "["
															+ ChatColor.DARK_PURPLE + "-d"
															+ ChatColor.YELLOW + "] ["
															+ ChatColor.GREEN + "folder"
															+ ChatColor.YELLOW + "] ["
															+ ChatColor.DARK_PURPLE + "page"
															+ ChatColor.YELLOW + "]")))
													.create()));
		searchfolder.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, slash + schemAlias + " searchfolder "));
		searchfolder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												  new ComponentBuilder(new TextComponent(
														  TextComponent.fromLegacyText(
																  ChatColor.RED + "e.g. "
																  + ChatColor.GRAY + "" + slash + schemAlias + " "
																  + ChatColor.AQUA + "searchfolder "
																  + ChatColor.YELLOW + "["
																  + ChatColor.DARK_PURPLE + "-d"
																  + ChatColor.YELLOW + "] ["
																  + ChatColor.GREEN + "folder"
																  + ChatColor.YELLOW + "] ["
																  + ChatColor.DARK_PURPLE + "page"
																  + ChatColor.YELLOW + "]")))
														  .create()));
		base.addExtra(schem);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + " <")));
		base.addExtra(help);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(formats);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(load);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(save);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(rename);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(renamefolder);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(delete);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(deletefolder);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(list);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(listfolders);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(search);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + "|")));
		base.addExtra(searchfolder);
		base.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.YELLOW + ">")));
		target.spigot().sendMessage(base);
	}
}
