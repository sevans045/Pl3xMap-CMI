package fr.doctaenkoda.cmi.pl3xmapcmi.hook;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Warps.CmiWarp;
import org.bukkit.entity.Player;

public class CMIHook {

    public static java.util.HashMap<String, CmiWarp> getWarps() {
        return cmi().getWarpManager().getWarps();
    }

    public static boolean isVanished(Player player) {
        return cmi().getVanishManager().getAllVanished().stream().anyMatch(playerUUID -> playerUUID.toString().equalsIgnoreCase(player.getUniqueId().toString()));
    }

    public static CMI cmi() {
        return CMI.getPlugin(CMI.class);
    }
}
