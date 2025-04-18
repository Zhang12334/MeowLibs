package com.meow.meowlibs;

import org.bukkit.plugin.java.JavaPlugin;

public class MeowLibs extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("   __  ___               __   _ __     ");
        getLogger().info("  /  |/  /__ ___ _    __/ /  (_) /  ___");
        getLogger().info(" / /|_/ / -_) _ \\ |/|/ / /__/ / _ \\(_-<");
        getLogger().info("/_/  /_/\\__/\\___/__,__/____/_/_.__/___/");
        getLogger().info("                                       ");
        getLogger().info(String.format("MeowLibs v%s enabled", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        getLogger().info("MeowLibs disabled");
    }
}