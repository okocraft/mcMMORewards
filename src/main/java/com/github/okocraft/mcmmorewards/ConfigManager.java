package com.github.okocraft.mcmmorewards;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

    private final Plugin plugin;
    private final CustomConfig rewardConfig;

    protected ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.rewardConfig = new CustomConfig(plugin, "RewardConfig.yml");
    }

    public CustomConfig getRewardConfig() {
        return rewardConfig;
    }

    protected void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        rewardConfig.saveDefaultConfig();
        rewardConfig.reloadConfig();

        HandlerList.unregisterAll(plugin);
        new PlayerLevelUpListener(plugin);
    }
}
