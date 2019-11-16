package de.zeanon.schemmanager.global.utils;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

	@NotNull
	private static final ArrayList<String> disableRequests = new ArrayList<>();
	@NotNull
	private static final ArrayList<String> updateRequests = new ArrayList<>();

	public static void removeDisableRequest(@NotNull final Player p) {
		disableRequests.remove(p.getUniqueId().toString());
	}

	public static void removeUpdateRequest(@NotNull final Player p) {
		updateRequests.remove(p.getUniqueId().toString());
	}

	public static void addDisableRequest(@NotNull final Player p) {
		if (!checkDisableRequest(p)) {
			disableRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkDisableRequest(@NotNull final Player p) {
		return disableRequests.contains(p.getUniqueId().toString());
	}

	public static void addUpdateRequest(@NotNull final Player p) {
		if (!checkUpdateRequest(p)) {
			updateRequests.add(p.getUniqueId().toString());
		}
	}

	public static boolean checkUpdateRequest(@NotNull final Player p) {
		return updateRequests.contains(p.getUniqueId().toString());
	}
}