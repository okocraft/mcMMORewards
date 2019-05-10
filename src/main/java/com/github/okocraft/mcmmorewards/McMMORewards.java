package com.github.okocraft.mcmmorewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class McMMORewards extends JavaPlugin {

	private static McMMORewards instance;
	@Getter
	private ConfigManager configManager;

	@Override
	public void onEnable() {

		configManager = new ConfigManager(this);

		getCommand("mcmmorewards").setExecutor(new Commands());
		getCommand("mcmmorewards").setTabCompleter(new McMMORewardsTabCompleter());

		new PlayerLevelUpListener(this);
	}

	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(Player::closeInventory);
		HandlerList.unregisterAll(this);
	}

	public static McMMORewards getInstance() {
		if (instance == null)
			instance = (McMMORewards) Bukkit.getServer().getPluginManager().getPlugin("McMMORewards");
		return instance;
	}
}
