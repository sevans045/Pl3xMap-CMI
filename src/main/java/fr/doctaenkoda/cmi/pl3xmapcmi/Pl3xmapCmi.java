package fr.doctaenkoda.cmi.pl3xmapcmi;

import fr.doctaenkoda.cmi.pl3xmapcmi.hook.Pl3xMapHook;
import fr.doctaenkoda.cmi.pl3xmapcmi.listener.CMI_Vanish_Listener;
import fr.doctaenkoda.cmi.pl3xmapcmi.utils.configuration.Config;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

public final class Pl3xmapCmi extends JavaPlugin {

    // Instance with Getters
    @Getter
    private static Pl3xmapCmi instance;
    // Logger Minecraft
    private static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.reload(this);
        instance = this;
        try {
            if (!new File(getDataFolder(), "warp.png").exists()) {
                URL url = new URL("https://raw.githubusercontent.com/DoctaEnkoda/Pl3xMap-CMI/master/src/main/resources/warp.png");
                image = ImageIO.read(url);
                ImageIO.write(image, "png",new File(getDataFolder(), "warp.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PluginManager pluginManager = this.getServer().getPluginManager();

        for (String s : Arrays.asList("Pl3xMap", "CMI")) {
            if (!getServer().getPluginManager().isPluginEnabled(s)) {
                getLogger().severe(String.format("Plugin %s not found or disabled ! Plugin will be disabled!", s));
                pluginManager.disablePlugin(this);
                return;
            }
        }

        pluginManager.registerEvents(new CMI_Vanish_Listener(),this);

        // Run Task
        Pl3xMapHook.load(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Pl3xMapHook.disable();
    }

    public static Logger getLog() {
        return logger;
    }
}
