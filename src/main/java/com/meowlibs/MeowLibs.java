package com.meowlibs;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MeowLibs extends JavaPlugin {

    // bstats
    int pluginId = 25529;
    Metrics metrics = new Metrics(this, pluginId);    

    @Override
    public void onEnable() {
        getLogger().info("   __  ___               __   _ __     ");
        getLogger().info("  /  |/  /__ ___ _    __/ /  (_) /  ___");
        getLogger().info(" / /|_/ / -_) _ \\ |/|/ / /__/ / _ \\(_-<");
        getLogger().info("/_/  /_/\\__/\\___/__,__/____/_/_.__/___/");
        getLogger().info("                                       ");
        getLogger().info(String.format("MeowLibs v%s enabled", getDescription().getVersion()));

        // 创建 CheckUpdate 实例
        CheckUpdate updateChecker = new CheckUpdate(
            getLogger(), // log记录器
            getDescription() // 插件版本信息
        );   

        // 异步执行更新检查
        new BukkitRunnable() {
            @Override
            public void run() {
                updateChecker.checkUpdate();
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MeowLibs disabled");
    }
}