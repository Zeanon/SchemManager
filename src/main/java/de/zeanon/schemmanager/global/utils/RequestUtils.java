package de.zeanon.schemmanager.global.utils;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RequestUtils {

	private final @NotNull Set<UUID> disableRequests = new ConcurrentSkipListSet<>();
	private final @NotNull Set<UUID> updateRequests = new ConcurrentSkipListSet<>();
	private boolean consoleUpdate = false;
	private boolean consoleDisable = false;

	public void addDisableRequest(final @NotNull UUID uuid) {
		RequestUtils.disableRequests.add(uuid);
	}

	public void removeDisableRequest(final @NotNull UUID uuid) {
		RequestUtils.disableRequests.remove(uuid);
	}

	public boolean checkDisableRequest(final @NotNull UUID uuid) {
		return RequestUtils.disableRequests.contains(uuid);
	}

	public void addUpdateRequest(final @NotNull UUID uuid) {
		RequestUtils.updateRequests.add(uuid);
	}

	public void removeUpdateRequest(final @NotNull UUID uuid) {
		RequestUtils.updateRequests.remove(uuid);
	}

	public boolean checkUpdateRequest(final @NotNull UUID uuid) {
		return RequestUtils.updateRequests.contains(uuid);
	}

	public void addConsoleDisableRequest() {
		RequestUtils.consoleDisable = true;
	}

	public void removeConsoleDisableRequest() {
		RequestUtils.consoleDisable = false;
	}

	public boolean checkConsoleDisableRequest() {
		return RequestUtils.consoleDisable;
	}

	public void addConsoleUpdateRequest() {
		RequestUtils.consoleUpdate = true;
	}

	public void removeConsoleUpdateRequest() {
		RequestUtils.consoleUpdate = false;
	}

	public boolean checkConsoleUpdateRequest() {
		return RequestUtils.consoleUpdate;
	}
}