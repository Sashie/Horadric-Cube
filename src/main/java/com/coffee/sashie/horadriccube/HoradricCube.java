package com.coffee.sashie.horadriccube;

import ch.njol.skript.Skript;
import com.coffee.sashie.horadriccube.utils.versions.*;
import com.coffee.sashie.horadriccube.utils.yaml.SkriptYamlConstructor;
import com.coffee.sashie.horadriccube.utils.yaml.SkriptYamlRepresenter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class HoradricCube extends JavaPlugin {

    public static String PROJECT_PREFIX = "Horadric Cube";
    private static HoradricCube plugin;
    public static HoradricCube getInstance() { return plugin; }
    public static Server getThisServer() { return Bukkit.getServer(); }

    private int serverVersion;
    private SkriptAdapter adapter;

    private static SkriptYamlRepresenter representer;
    private static SkriptYamlConstructor constructor;

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

            serverVersion = Skript.getMinecraftVersion().getMinor();

            if (Skript.getVersion().getMajor() >= 3 || (Skript.getVersion().getMajor() >= 2 && Skript.getVersion().getMinor() >= 8))
                adapter = new V2_8();
            if (Skript.getVersion().getMajor() >= 2 && Skript.getVersion().getMinor() >= 7)
                adapter = new V2_7();
            else if (Skript.getVersion().getMajor() == 2 && Skript.getVersion().getMinor() >= 6)
                adapter = new V2_6();
            else if (Skript.getVersion().getMajor() == 2 && Skript.getVersion().getMinor() >= 4)
                adapter = new V2_4();
            else
                adapter = new V2_3();

            representer = new SkriptYamlRepresenter();
            constructor = new SkriptYamlConstructor();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SkriptYamlRepresenter getRepresenter() {
        return representer;
    }

    public SkriptYamlConstructor getConstructor() {
        return constructor;
    }

    public int getServerVersion() {
        return serverVersion;
    }

    public SkriptAdapter getSkriptAdapter() {
        return adapter;
    }
}
