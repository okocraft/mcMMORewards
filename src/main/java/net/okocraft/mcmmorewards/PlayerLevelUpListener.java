package net.okocraft.mcmmorewards;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

class PlayerLevelUpListener implements Listener {

    List<MemorySection> settings = new ArrayList<>();

    PlayerLevelUpListener(Plugin plugin) {
        McMMORewards.getInstance()
                .getConfigManager().getRewardConfig()
                .getConfig().getValues(false)
                .values()
                .forEach(value -> {
                    if (value instanceof MemorySection section)
                        settings.add(section);
                });

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerLevelUp(McMMOPlayerLevelUpEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        int gainedLevelTotal = event.getLevelsGained();

        int basePowerLevel = ExperienceAPI.getPowerLevel(player) - gainedLevelTotal;
        int baseSkillLevel = event.getSkillLevel() - gainedLevelTotal;

        for (int gainedLevel = 1; gainedLevel <= gainedLevelTotal; gainedLevel++) {
            for (var section : settings) {
                String settingSkillTypeName = section.getString("SkillType", "POWER").toUpperCase();
                int baseLevel;

                if (settingSkillTypeName.equalsIgnoreCase("POWER")) {
                    baseLevel = basePowerLevel;
                } else if (settingSkillTypeName.equalsIgnoreCase(event.getSkill().name())) {
                    baseLevel = baseSkillLevel;
                } else {
                    continue;
                }

                int newLevel = baseLevel + gainedLevel;

                if (section.isSet("RewardLevels")) {
                    List<Integer> rewardLevels = section.getIntegerList("RewardLevels");
                    if (rewardLevels.contains(newLevel)) {
                        giveReward(section, newLevel, player);
                    }
                }

                int levelDistance = section.getInt("LevelDistance", 0);

                if (levelDistance != 0 && newLevel % levelDistance == 0) {
                    giveReward(section, newLevel, player);
                }
            }
        }
    }

    private static void giveReward(MemorySection config, int level, Player player) {
        if (config.isSet("GlobalMessages")) {
            for (var message : config.getStringList("GlobalMessages")) {
                sendMessage(Bukkit.getServer(), replaceStringVariables(message, level, player.getName()));
            }
        }

        if (config.isSet("PlayerMessages")) {
            for (var message : config.getStringList("PlayerMessages")) {
                sendMessage(player, replaceStringVariables(message, level, player.getName()));
            }
        }

        if (config.isSet("ConsoleCommands")) {
            for (var command : config.getStringList("ConsoleCommands")) {
                String commandReplaced = replaceStringVariables(command, level, player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandReplaced);
            }
        }

        if (config.isSet("PlayerCommands")) {
            for (var command : config.getStringList("PlayerCommands")) {
                String commandReplaced = replaceStringVariables(command, level, player.getName());
                Bukkit.dispatchCommand(player, commandReplaced);
            }
        }
    }

    private static String replaceStringVariables(String original, int level, String playerName) {
        return original.replace("%level%", String.valueOf(level)).replace("%player%", playerName);
    }

    private static void sendMessage(Audience receiver, String message) {
        receiver.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }
}
