package de.zeanon.schemmanager.plugin.utils.commands;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class GlobalRequestUtils {

	private final @NotNull Set<String> disableRequests = new ConcurrentSkipListSet<>();
	private final @NotNull Set<String> updateRequests = new ConcurrentSkipListSet<>();
	private boolean consoleUpdate = false;
	private boolean consoleDisable = false;

	public void addDisableRequest(final @NotNull UUID uuid) {
		GlobalRequestUtils.disableRequests.add(uuid.toString());
	}

	public void removeDisableRequest(final @NotNull UUID uuid) {
		GlobalRequestUtils.disableRequests.remove(uuid.toString());
	}

	public boolean checkDisableRequest(final @NotNull UUID uuid) {
		return GlobalRequestUtils.disableRequests.contains(uuid.toString());
	}

	public void addUpdateRequest(final @NotNull UUID uuid) {
		GlobalRequestUtils.updateRequests.add(uuid.toString());
	}

	public void removeUpdateRequest(final @NotNull UUID uuid) {
		GlobalRequestUtils.updateRequests.remove(uuid.toString());
	}

	public boolean checkUpdateRequest(final @NotNull UUID uuid) {
		return GlobalRequestUtils.updateRequests.contains(uuid.toString());
	}

	public void addConsoleDisableRequest() {
		GlobalRequestUtils.consoleDisable = true;
	}

	public void removeConsoleDisableRequest() {
		GlobalRequestUtils.consoleDisable = false;
	}

	public boolean checkConsoleDisableRequest() {
		return GlobalRequestUtils.consoleDisable;
	}

	public void addConsoleUpdateRequest() {
		GlobalRequestUtils.consoleUpdate = true;
	}

	public void removeConsoleUpdateRequest() {
		GlobalRequestUtils.consoleUpdate = false;
	}

	public boolean checkConsoleUpdateRequest() {
		return GlobalRequestUtils.consoleUpdate;
	}
}