package de.zeanon.schemmanager.plugin.utils.commands;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommandConfirmation {

	CONFIRM(true),
	DENY(false);

	@Getter
	@Accessors(fluent = true)
	private final boolean confirm;


	public static CommandConfirmation map(final @NotNull String arg) {
		switch (arg.toLowerCase()) {
			case "-confirm":
				return CommandConfirmation.CONFIRM;
			case "-deny":
				return CommandConfirmation.DENY;
			default:
				return null;
		}
	}
}