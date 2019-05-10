package com.github.okocraft.mcmmorewards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import lombok.Getter;

public class ConfigManager {

    private Plugin plugin;

    @Getter FileConfiguration defaultConfig;

    @Getter private String languageFileName;
    @Getter private CustomConfig languageConfig;
    @Getter private CustomConfig rewardConfig;

    /**
     * コンストラクタ
     * @param plugin
     */
    protected ConfigManager(Plugin plugin){
        this.plugin = plugin;
        defaultConfig = plugin.getConfig();
        rewardConfig = new CustomConfig(plugin, "RewardConfig.yml");
        languageFileName = defaultConfig.getString("LanguageFile", "japanese.yml");
        languageConfig = new CustomConfig(plugin, "Languages/" + languageFileName);
        plugin.saveDefaultConfig();
        rewardConfig.saveDefaultConfig();
    }

    /**
     * 全てのコンフィグをリロードする。
     */
    protected void reloadConfig(){
        plugin.reloadConfig();
        languageFileName = defaultConfig.getString("LanguageFile", "japanese.yml");
        languageConfig.reloadConfig();
        rewardConfig.reloadConfig();

        HandlerList.unregisterAll(plugin);
        new PlayerLevelUpListener(plugin);
    }
}