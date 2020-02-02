package de.zeanon.schemmanager.global.utils;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RequestUtils {

	private final @NotNull List<String> disableRequests = new GapList<>();
	private final @NotNull List<String> updateRequests = new GapList<>();

	public void removeDisableRequest(final @NotNull Player p) {
		RequestUtils.disableRequests.remove(p.getUniqueId().toString());
	}

	public void removeUpdateRequest(final @NotNull Player p) {
		RequestUtils.updateRequests.remove(p.getUniqueId().toString());
	}

	public void addDisableRequest(final @NotNull Player p) {
		if (!RequestUtils.checkDisableRequest(p)) {
			RequestUtils.disableRequests.add(p.getUniqueId().toString());
		}
	}

	public boolean checkDisableRequest(final @NotNull Player p) {
		return RequestUtils.disableRequests.contains(p.getUniqueId().toString());
	}

	public void addUpdateRequest(final @NotNull Player p) {
		if (!RequestUtils.checkUpdateRequest(p)) {
			RequestUtils.updateRequests.add(p.getUniqueId().toString());
		}
	}

	public boolean checkUpdateRequest(final @NotNull Player p) {
		return RequestUtils.updateRequests.contains(p.getUniqueId().toString());
	}
}