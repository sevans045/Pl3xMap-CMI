package fr.doctaenkoda.cmi.pl3xmapcmi.listener;

import com.Zrips.CMI.events.CMIPlayerUnVanishEvent;
import com.Zrips.CMI.events.CMIPlayerVanishEvent;
import fr.doctaenkoda.cmi.pl3xmapcmi.hook.CMIHook;
import fr.doctaenkoda.cmi.pl3xmapcmi.hook.Pl3xMapHook;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.configuration.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CMI_Vanish_Listener implements Listener {

    // Player Join vanish ?
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoinIsVanish(final PlayerJoinEvent playerJoinEvent) {
        if (Config.HIDE_VANISHED) {
            Player player = playerJoinEvent.getPlayer();
            if (CMIHook.isVanished(player)) {
                Pl3xMapHook.api().playerManager().hide(player.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishStatusChange(final CMIPlayerVanishEvent cmiPlayerVanishEvent) {
        if (Config.HIDE_VANISHED) {
            Player player = cmiPlayerVanishEvent.getPlayer();
            Pl3xMapHook.api().playerManager().hidden(player.getUniqueId(), true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishStatusChange(final CMIPlayerUnVanishEvent cmiPlayerUnVanishEvent) {
        if (Config.HIDE_VANISHED) {
            Player player = cmiPlayerUnVanishEvent.getPlayer();
            Pl3xMapHook.api().playerManager().hidden(player.getUniqueId(), false);
        }
    }
}
