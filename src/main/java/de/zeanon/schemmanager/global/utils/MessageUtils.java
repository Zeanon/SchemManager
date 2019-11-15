package de.zeanon.schemmanager.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

	/**
	 * Sends a clickable message performing a command
	 *
	 * @param message        the non clickable part of the message
	 * @param commandMessage the clickable part of the message
	 * @param hoverMessage   the message to be shown when hovering over commandMessage.
	 * @param command        the command to be executed when clicked
	 * @param target         the player the message is sent to
	 */
	public static void sendCommandMessage(@NotNull final String message,
										  @NotNull final String commandMessage,
										  final String hoverMessage,
										  final String command,
										  @NotNull final Player target) {
		new TextComponent();
		TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
		TextComponent commandPart = new TextComponent(TextComponent.fromLegacyText(commandMessage));
		commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												 new ComponentBuilder(hoverMessage).create()));
		localMessage.addExtra(commandPart);
		target.spigot().sendMessage(localMessage);
	}

	/**
	 * Sends a clickable message performing a command
	 *
	 * @param message        the non clickable part of the message
	 * @param commandMessage the clickable part of the message
	 * @param hoverMessage   the message to be shown when hovering over commandMessage.
	 * @param command        the command to be executed when clicked
	 * @param target         the player the message is sent to
	 */
	public static void sendInvertedCommandMessage(@NotNull final String message, @NotNull final String commandMessage, final String hoverMessage, final String command, @NotNull final Player target) {
		new TextComponent();
		TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
		TextComponent commandPart = new TextComponent(TextComponent.fromLegacyText(commandMessage));
		commandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		commandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												 new ComponentBuilder(hoverMessage).create()));
		commandPart.addExtra(localMessage);
		target.spigot().sendMessage(commandPart);
	}

	/**
	 * Sends a boolean type message to the player
	 *
	 * @param message    the message to be sent
	 * @param commandYes the command to be executed when clicked on yes
	 * @param commandNo  the command to be executed when clicked on no
	 * @param target     the player the message is sent to
	 */
	public static void sendBooleanMessage(@NotNull final String message, final String commandYes, final String commandNo, @NotNull final Player target) {
		new TextComponent();
		TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
		TextComponent separator = new TextComponent(TextComponent.fromLegacyText(ChatColor.BLACK + " "
																				 + ChatColor.BOLD + "| "));
		TextComponent commandPartYes = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_GREEN + ""
																					  + ChatColor.BOLD + "[Y]"));
		TextComponent commandPartNo = new TextComponent(TextComponent.fromLegacyText(ChatColor.DARK_RED + ""
																					 + ChatColor.BOLD + "[N]"));
		commandPartYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandYes));
		commandPartYes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
													new ComponentBuilder(new TextComponent(
															TextComponent.fromLegacyText(
																	ChatColor.DARK_GREEN + ""
																	+ ChatColor.BOLD + "[YES]")))
															.create()));
		commandPartNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandNo));
		commandPartNo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												   new ComponentBuilder(new TextComponent(
														   TextComponent.fromLegacyText(
																   ChatColor.DARK_RED + ""
																   + ChatColor.BOLD + "[NO]")))
														   .create()));
		localMessage.addExtra(" ");
		localMessage.addExtra(commandPartYes);
		localMessage.addExtra(separator);
		localMessage.addExtra(commandPartNo);
		target.spigot().sendMessage(localMessage);
	}

	/**
	 * Sends the player a message with scroll buttons
	 *
	 * @param commandForward  the command to be executed when clicking on forward
	 * @param commandBackward the command to be executed when clicking on backwards
	 * @param messageForward  the message to be shown when hovering over the forward button
	 * @param messageBackward the message to be shown when hovering over the backwards button
	 * @param target          the player the message is sent to
	 * @param buttonColor     the color of the buttons
	 */
	public static void sendScrollMessage(final String commandForward, final String commandBackward, @NotNull final String messageForward, @NotNull final String messageBackward, @NotNull final Player target, final ChatColor buttonColor) {
		TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(ChatColor.AQUA + "=== "));
		TextComponent commandPartBackward = new TextComponent(TextComponent.fromLegacyText(buttonColor + "[<<<]"));
		TextComponent commandPartForward = new TextComponent(TextComponent.fromLegacyText(buttonColor + "[>>>]"));
		commandPartBackward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandBackward));
		commandPartBackward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
														 new ComponentBuilder(new TextComponent(
																 TextComponent.fromLegacyText(
																		 messageBackward))).create()));
		commandPartForward.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandForward));
		commandPartForward.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
														new ComponentBuilder(new TextComponent(
																TextComponent.fromLegacyText(
																		messageForward))).create()));
		localMessage.addExtra(commandPartBackward);
		localMessage.addExtra(ChatColor.AQUA + " " + ChatColor.BOLD + "| ");
		localMessage.addExtra(commandPartForward);
		localMessage.addExtra(ChatColor.AQUA + " ===");
		target.spigot().sendMessage(localMessage);
	}

	/**
	 * sends the player a message with suggestCommand capabilities
	 *
	 * @param message        the non clickable part of the message
	 * @param suggestMessage the clickable part of the message
	 * @param hoverMessage   the message to sho when hovering over suggestMessage.
	 * @param command        the command to be suggested when clicked
	 * @param target         the player the message is sent to
	 */
	public static void sendSuggestMessage(@NotNull final String message, @NotNull final String suggestMessage, @NotNull final String hoverMessage, final String command, @NotNull final Player target) {
		TextComponent localMessage = new TextComponent(TextComponent.fromLegacyText(message));
		TextComponent suggestPart = new TextComponent(TextComponent.fromLegacyText(suggestMessage));
		suggestPart.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		suggestPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
												 new ComponentBuilder(new TextComponent(
														 TextComponent.fromLegacyText(hoverMessage))).create()));
		localMessage.addExtra(suggestPart);
		target.spigot().sendMessage(localMessage);
	}

	/**
	 * @param message1     the first part of the message (no hovermessage)
	 * @param message2     the second part of the message (hovermessage)
	 * @param message3     the third part of the message (no hovermessage)
	 * @param hoverMessage the message to be shown when hovering over message2
	 * @param target       the player the message is sent to
	 */
	public static void sendHoverMessage(@NotNull final String message1, @NotNull final String message2, @NotNull final String message3, @NotNull final String hoverMessage, @NotNull final Player target) {
		TextComponent localMessage1 = new TextComponent(TextComponent.fromLegacyText(message1));
		TextComponent hoverPart = new TextComponent(TextComponent.fromLegacyText(message2));
		TextComponent localMessage2 = new TextComponent(TextComponent.fromLegacyText(message3));
		hoverPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											   new ComponentBuilder(new TextComponent(
													   TextComponent.fromLegacyText(hoverMessage))).create()));
		localMessage1.addExtra(hoverPart);
		localMessage1.addExtra(localMessage2);
		target.spigot().sendMessage(localMessage1);
	}
}