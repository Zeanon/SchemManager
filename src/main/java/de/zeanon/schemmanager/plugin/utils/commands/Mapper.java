package de.zeanon.schemmanager.plugin.utils.commands;

import de.steamwar.commandframework.SWCommandUtils;
import de.steamwar.commandframework.TypeMapper;
import java.util.Collections;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Mapper {

	public void initialize() {
		SWCommandUtils.addMapper(CommandConfirmation.class, Mapper.mapCommandConfirmation());
	}

	private @NotNull TypeMapper<CommandConfirmation> mapCommandConfirmation() {
		return SWCommandUtils.createMapper(s -> {
			switch (s.toLowerCase()) {
				case "-confirm":
					return CommandConfirmation.CONFIRM;
				case "-deny":
					return CommandConfirmation.DENY;
				default:
					return null;
			}
		}, s -> Collections.emptyList());
	}
}
