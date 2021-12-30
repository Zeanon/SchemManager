package de.zeanon.schemmanager.plugin.schemmanagercommands;

import de.steamwar.commandframework.GuardChecker;
import de.steamwar.commandframework.SWCommand;
import de.steamwar.commandframework.TypeMapper;
import de.zeanon.schemmanager.plugin.utils.commands.CommandConfirmation;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalRequestUtils;
import de.zeanon.schemmanager.plugin.utils.guards.DisableGuard;
import de.zeanon.schemmanager.plugin.utils.guards.UpdateGuard;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SchemmanagerCommand extends SWCommand {

	public SchemmanagerCommand() {
		super(new Prefix("schemmanager"), "schemmanager", "sm");
	}


	@Register(help = true)
	public void helpCommand(final @NotNull Player p, final @NotNull String... args) {
		Help.execute(p, args);
	}


	@Register("help")
	public void helpCommand(final @NotNull Player p) {
		Help.execute(p);
	}


	@Register(value = {"disable"}, help = true)
	public void disableHelp(final @NotNull @Guard("disable") Player p, final @NotNull String... args) {
		Disable.usage(p);
	}

	@Register("disable")
	public void disableCommand(final @NotNull @Guard("disable") CommandSender sender) {
		Disable.execute(sender, null);
	}

	@Register("disable")
	public void disableCommandConfirm(final @NotNull @Guard("disable") CommandSender sender, final @NotNull CommandConfirmation confirmation) {
		Disable.execute(sender, confirmation);
	}


	@Register(value = {"update"}, help = true)
	public void updateHelp(final @NotNull @Guard("update") Player p, final @NotNull String... args) {
		Update.usage(p);
	}

	@Register("update")
	public void updateCommand(final @NotNull @Guard("update") CommandSender sender) {
		Update.execute(sender, null);
	}

	@Register("update")
	public void updateCommandConfirmation(final @NotNull @Guard("update") CommandSender sender, final @NotNull CommandConfirmation confirmation) {
		Update.execute(sender, confirmation);
	}


	@SuppressWarnings("unused")
	@Guard(value = "update", local = true)
	private GuardChecker updateGuard() {
		return new UpdateGuard();
	}

	@SuppressWarnings("unused")
	@Guard(value = "disable", local = true)
	private GuardChecker disableGuard() {
		return new DisableGuard();
	}


	@ClassMapper(value = CommandConfirmation.class, local = true)
	private @NotNull TypeMapper<CommandConfirmation> mapCommandConfirmation() {
		return new TypeMapper<CommandConfirmation>() {
			@Override
			public CommandConfirmation map(final @NotNull String[] previousArguments, final @NotNull String s) {
				return CommandConfirmation.map(s);
			}

			@Override
			public java.util.List<String> tabCompletes(final @NotNull CommandSender commandSender, final @NotNull String[] previousArguments, final @NotNull String arg) {
				final @NotNull List<String> tabCompletions = Arrays.asList("-confirm", "-deny");
				if (previousArguments.length > 0) {
					if (commandSender instanceof Player) {
						final @NotNull Player p = (Player) commandSender;
						if (previousArguments[0].equalsIgnoreCase("disable")
							&& GlobalRequestUtils.checkDisableRequest(p.getUniqueId())) {
							return tabCompletions;
						} else if (previousArguments[0].equalsIgnoreCase("update")
								   && GlobalRequestUtils.checkUpdateRequest(p.getUniqueId())) {
							return tabCompletions;
						} else {
							return null;
						}
					} else {
						if (previousArguments[0].equalsIgnoreCase("disable")
							&& GlobalRequestUtils.checkConsoleDisableRequest()) {
							return tabCompletions;
						} else if (previousArguments[0].equalsIgnoreCase("update")
								   && GlobalRequestUtils.checkConsoleUpdateRequest()) {
							return tabCompletions;
						} else {
							return null;
						}
					}
				} else {
					return null;
				}
			}
		};
	}
}
