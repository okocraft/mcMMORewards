package net.okocraft.mcmmorewards;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class McMMORewards extends JavaPlugin {

    private static McMMORewards instance;

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.reloadConfig();

        var command = getCommand("mcmmorewards");

        if (command != null) {
            command.setExecutor(new Commands());
            command.setTabCompleter(new McMMORewardsTabCompleter());
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static McMMORewards getInstance() {
        if (instance == null)
            instance = (McMMORewards) Bukkit.getServer().getPluginManager().getPlugin("McMMORewards");
        return instance;
    }
}
