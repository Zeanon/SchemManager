package de.zeanon.schemmanager.global.utils;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RequestUtils {

	@NotNull
	private static final List<String> disableRequests = new GapList<>();
	@NotNull
	private static final List<String> updateRequests = new GapList<>();

	public static void removeDisableRequest(final @NotNull Player p) {
		disableRequests.remove(p.getUniqueId().toString());
	}

	public static void removeUpdateRequest(final @NotNull Player p) {
		updateRequests.remove(p.getUniqueId().toString());
	}

	public static void addDisableRequest(final @NotNull Player p) {
		if (!checkDisableRequest(p)) {
			disableRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkDisableRequest(final @NotNull Player p) {
		return disableRequests.contains(p.getUniqueId().toString());
	}

	public static void addUpdateRequest(final @NotNull Player p) {
		if (!checkUpdateRequest(p)) {
			updateRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkUpdateRequest(final @NotNull Player p) {
		return updateRequests.contains(p.getUniqueId().toString());
	}
}