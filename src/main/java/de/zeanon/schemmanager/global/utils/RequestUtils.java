package de.zeanon.schemmanager.global.utils;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RequestUtils {

	private final @NotNull
	List<UUID> disableRequests = new GapList<>();
	private final @NotNull
	List<UUID> updateRequests = new GapList<>();

	public void removeDisableRequest(final @NotNull UUID uuid) {
		RequestUtils.disableRequests.remove(uuid);
	}

	public void removeUpdateRequest(final @NotNull UUID uuid) {
		RequestUtils.updateRequests.remove(uuid);
	}

	public void addDisableRequest(final @NotNull UUID uuid) {
		if (!RequestUtils.checkDisableRequest(uuid)) {
			RequestUtils.disableRequests.add(uuid);
		}
	}

	public boolean checkDisableRequest(final @NotNull UUID uuid) {
		return RequestUtils.disableRequests.contains(uuid);
	}

	public void addUpdateRequest(final @NotNull UUID uuid) {
		if (!RequestUtils.checkUpdateRequest(uuid)) {
			RequestUtils.updateRequests.add(uuid);
		}
	}

	public boolean checkUpdateRequest(final @NotNull UUID uuid) {
		return RequestUtils.updateRequests.contains(uuid);
	}
}