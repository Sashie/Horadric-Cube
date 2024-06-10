package com.coffee.sashie.horadriccube;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoradricCube extends JavaPlugin {

    public static String PROJECT_PREFIX = "Horadric Cube";
    static HoradricCube plugin;
    static HoradricCube getInstance() { return plugin; }
    static Server getThisServer() { return Bukkit.getServer(); }

    @Override
    public void onEnable() {
        plugin = this;
        var bukkitName = Bukkit.getServer().getName().toLowerCase();
        if (bukkitName.equals("craftbukkit")) {
            System.out.println("\033[0;31m-------------------------SPIGOT DETECTED------------------------------");
            System.out.printf("%s is no longer supported! Please change your server file to Paper/Purpur", bukkitName);
            System.out.println("---------------------------------------------------------------------\033[0m");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            /* Logic for configuration ...
            * For now ill implements only loading of elements, while the elements will be loaded on demand.
             */
            Elements.printEnabledElements();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
