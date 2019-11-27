package de.zeanon.schemmanager.global.utils;

import java.util.ArrayList;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RequestUtils {

	@NotNull
	private static final ArrayList<String> disableRequests = new ArrayList<>();
	@NotNull
	private static final ArrayList<String> updateRequests = new ArrayList<>();

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