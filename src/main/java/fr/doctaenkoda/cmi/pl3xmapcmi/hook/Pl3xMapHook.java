package fr.doctaenkoda.cmi.pl3xmapcmi.hook;

import fr.doctaenkoda.cmi.pl3xmapcmi.Pl3xmapCmi;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.configuration.Config;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.configuration.WorldConfig;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.task.Pl3xMapTask;
import net.pl3x.map.api.Key;
import net.pl3x.map.api.Pl3xMap;
import net.pl3x.map.api.Pl3xMapProvider;
import net.pl3x.map.api.SimpleLayerProvider;
import org.bukkit.plugin.Plugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Pl3xMapHook {
    public static final Key warpIconKey = Key.of("cmi_warp_icon");

    private static final Map<UUID, Pl3xMapTask> providers = new HashMap<>();

    public static void load(Plugin plugin) {
        try {
            BufferedImage icon = ImageIO.read(new File(plugin.getDataFolder(), "warp.png"));
            api().iconRegistry().register(warpIconKey, icon);
        } catch (IOException e) {
            Pl3xmapCmi.getLog().log(Level.WARNING, "Failed to register warp icon", e);
        }

        api().mapWorlds().forEach(mapWorld -> {
            WorldConfig worldConfig = WorldConfig.get(mapWorld);
            if (worldConfig.ENABLED) {
                SimpleLayerProvider provider = SimpleLayerProvider.builder(worldConfig.WARPS_LABEL)
                        .showControls(worldConfig.WARPS_SHOW_CONTROLS)
                        .defaultHidden(worldConfig.WARPS_CONTROLS_HIDDEN)
                        .build();
                mapWorld.layerRegistry().register(Key.of("cmi_" + mapWorld.uuid() + "_warps"), provider);
                Pl3xMapTask task = new Pl3xMapTask(mapWorld, worldConfig, provider);
                task.runTaskTimerAsynchronously(plugin, 0, 20L * Config.UPDATE_INTERVAL);
                providers.put(mapWorld.uuid(), task);
            }
        });
    }

    public static void disable() {
        providers.values().forEach(Pl3xMapTask::disable);
        providers.clear();
    }

    public static Pl3xMap api() {
        return Pl3xMapProvider.get();
    }
}