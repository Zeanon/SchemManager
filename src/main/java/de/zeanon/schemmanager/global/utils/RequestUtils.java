package de.zeanon.schemmanager.global.utils;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

	private static final ArrayList<String> disableRequests = new ArrayList<>();
	private static final ArrayList<String> updateRequests = new ArrayList<>();

	public static void removeDisableRequest(final Player p) {
		disableRequests.remove(p.getUniqueId().toString());
	}

	public static void removeUpdateRequest(final Player p) {
		updateRequests.remove(p.getUniqueId().toString());
	}

	public static void addDisableRequest(final Player p) {
		if (!checkDisableRequest(p)) {
			disableRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkDisableRequest(final Player p) {
		return disableRequests.contains(p.getUniqueId().toString());
	}

	public static void addUpdateRequest(final Player p) {
		if (!checkUpdateRequest(p)) {
			updateRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkUpdateRequest(final Player p) {
		return updateRequests.contains(p.getUniqueId().toString());
	}
}