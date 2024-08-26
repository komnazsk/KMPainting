package org.github.komnazsk.kmpainting;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * KM Painting Plugin Class
 */
public final class KMPainting extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PaitingClickListener paintingClickListener = new PaitingClickListener();
        // register player interect event
        getServer().getPluginManager().registerEvents(paintingClickListener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
