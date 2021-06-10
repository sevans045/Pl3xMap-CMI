package fr.doctaenkoda.cmi.pl3xmapcmi.utils.task;

import com.Zrips.CMI.Modules.Warps.CmiWarp;
import fr.doctaenkoda.cmi.pl3xmapcmi.hook.CMIHook;
import fr.doctaenkoda.cmi.pl3xmapcmi.hook.Pl3xMapHook;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.configuration.WorldConfig;
import net.pl3x.map.api.Key;
import net.pl3x.map.api.MapWorld;
import net.pl3x.map.api.Point;
import net.pl3x.map.api.SimpleLayerProvider;
import net.pl3x.map.api.marker.Icon;
import net.pl3x.map.api.marker.Marker;
import net.pl3x.map.api.marker.MarkerOptions;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Pl3xMapTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private final WorldConfig worldConfig;

    private boolean stop;

    public Pl3xMapTask(MapWorld world, WorldConfig worldConfig, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
        this.worldConfig = worldConfig;
    }

    @Override
    public void run() {
        if (stop) {
            cancel();
        }

        provider.clearMarkers();

        HashMap<String, CmiWarp> warps = CMIHook.getWarps();

        warps.forEach((s, cmiWarp) -> {
            Location loc = cmiWarp.getLoc().getBukkitLoc();
            if (loc.getWorld().getUID().equals(world.uuid())) {
                this.handle(s, loc);
            }
        });
    }

    private void handle(String warpName, Location loc) {
        Icon icon = Marker.icon(Point.fromLocation(loc), Pl3xMapHook.warpIconKey, worldConfig.ICON_SIZE);

        icon.anchor(Point.of(worldConfig.ICON_ANCHOR_X, worldConfig.ICON_ANCHOR_Z));

        icon.markerOptions(MarkerOptions.builder()
                .hoverTooltip(worldConfig.WARPS_TOOLTIP
                        .replace("{warp}", warpName)
                )
        );

        String markerid = "cmi_" + world.name() + "_warp_" + warpName.hashCode();
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}