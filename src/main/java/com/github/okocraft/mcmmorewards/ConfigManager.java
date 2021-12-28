package com.github.okocraft.mcmmorewards;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

class ConfigManager {

    private final Plugin plugin;
    private final CustomConfig rewardConfig;

    ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.rewardConfig = new CustomConfig(plugin, "RewardConfig.yml");
    }

    CustomConfig getRewardConfig() {
        return rewardConfig;
    }

    void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        rewardConfig.saveDefaultConfig();
        rewardConfig.reloadConfig();

        HandlerList.unregisterAll(plugin);
        new PlayerLevelUpListener(plugin);
    }
}
