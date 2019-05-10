package com.github.okocraft.mcmmorewards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
//import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PlayerLevelUpListener implements Listener {

    Map<String, MemorySection> settings = new HashMap<>();

    protected PlayerLevelUpListener(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        settings.clear();
        McMMORewards.getInstance().getConfigManager().getRewardConfig().getConfig().getValues(false).entrySet()
                .forEach(setting -> {
                    if (setting.getValue().getClass().getSimpleName().equals("MemorySection"))
                        settings.put(setting.getKey(), (MemorySection) setting.getValue());
                });

        // HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerLevelUp(McMMOPlayerLevelUpEvent event) {

        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        int gainedLevelTotal = event.getLevelsGained();

        IntStream.rangeClosed(1, gainedLevelTotal).forEach(gainedLevel -> {

            for (String setting : settings.keySet()) {

                MemorySection config = settings.get(setting);

                int baseLevel;
                String settingSkillTypeName = config.getString("SkillType", "POWER").toUpperCase();

                if (!settingSkillTypeName.equalsIgnoreCase("POWER")
                        && !settingSkillTypeName.equalsIgnoreCase(event.getSkill().getName()))
                    continue;

                switch (settingSkillTypeName) {

                case "POWER":
                    baseLevel = ExperienceAPI.getPowerLevel(player) - gainedLevelTotal;
                    break;
                default:
                    baseLevel = event.getSkillLevel() - gainedLevelTotal;
                    break;
                }

                int newLevel = baseLevel + gainedLevel;

                if (config.isSet("RewardLevels")) {
                    List<Integer> rewardLevels = config.getIntegerList("RewardLevels");
                    if (rewardLevels.contains(Integer.valueOf(newLevel))) {
                        giveReward(config, newLevel, player);
                    }
                }

                int levelDistance = config.getInt("LevelDistance", Integer.MAX_VALUE);
                if (levelDistance != Integer.MAX_VALUE) {
                    int matchingFirstLevel = IntStream.iterate(1, x -> x + 1).map(count -> count * levelDistance)
                            .filter(eachLevel -> eachLevel >= newLevel).findFirst().orElse(Integer.MAX_VALUE);

                    if (matchingFirstLevel != newLevel)
                        continue;

                    giveReward(config, newLevel, player);
                }
            }
        });
    }

    private static void giveReward(MemorySection config, int level, Player player) {

        if (config.isSet("GlobalMessages")) {
            config.getStringList("GlobalMessages").forEach(message -> {
                String messageReplaced = replaceStringVariables(message, level, player.getName());
                Bukkit.broadcastMessage(messageReplaced);
            });
        }

        if (config.isSet("PlayerMessages")) {
            config.getStringList("PlayerMessages").forEach(message -> {
                String messageReplaced = replaceStringVariables(message, level, player.getName());
                player.sendMessage(messageReplaced);
            });
        }

        if (config.isSet("ConsoleCommands")) {
            config.getStringList("ConsoleCommands").forEach(command -> {
                String commandReplaced = replaceStringVariables(command, level, player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandReplaced);
            });
        }

        if (config.isSet("PlayerCommands")) {
            config.getStringList("PlayerCommands").forEach(command -> {
                String commandReplaced = replaceStringVariables(command, level, player.getName());
                Bukkit.dispatchCommand(player, commandReplaced);
            });
        }
    }

    private static String replaceStringVariables(String original, int level, String playerName) {
        return original.replaceAll("%level%", String.valueOf(level)).replaceAll("%player%", playerName);
    }
}