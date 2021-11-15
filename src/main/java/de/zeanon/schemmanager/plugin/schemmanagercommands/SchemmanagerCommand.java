package de.zeanon.schemmanager.plugin.schemmanagercommands;

import de.steamwar.commandframework.SWCommand;
import de.steamwar.commandframework.TypeMapper;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SchemmanagerCommand extends SWCommand {

    public SchemmanagerCommand() {
        super("schemmanager", "sm");
    }

    @Register(value = {"disable"}, help = true)
    public void disableHelp(final @NotNull Player p, final @NotNull String... args) {
        Disable.usage(p);
    }

    @Register("disable")
    public void disableCommand(final @NotNull Player p) {
        Disable.execute(p, null);
    }

    @Register("disable")
    public void disableCommandConfirm(final @NotNull Player p, final @NotNull CommandConfirmation confirmation) {
        Disable.execute(p, confirmation);
    }

    @Register(value = {"update"}, help = true)
    public void updateHelp(final @NotNull Player p, final @NotNull String... args) {
        Update.usage(p);
    }

    @Register("update")
    public void updateCommand(final @NotNull Player p) {
        Update.execute(p, null);
    }

    @Register("update")
    public void updateCommandConfirmation(final @NotNull Player p, final @NotNull CommandConfirmation confirmation) {
        Update.execute(p, confirmation);
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
                final @NotNull java.util.List<String> tabCompletions = Arrays.asList("-confirm", "-deny");
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
