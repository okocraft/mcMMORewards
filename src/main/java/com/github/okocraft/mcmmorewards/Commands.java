package com.github.okocraft.mcmmorewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (0 < args.length && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("mcmmorewards.command.reload")) {
                McMMORewards.getInstance().getConfigManager().reloadConfig();
                sender.sendMessage("コンフィグを再読込しました。");
            } else {
                sender.sendMessage(Component.text("mcmmorewards.command.reload の権限がありません", NamedTextColor.RED));
            }
        } else {
            sender.sendMessage("§c[]=====[] §amcMMORewards help §c[]=====[]");
            sender.sendMessage("§3/mmorewards reload §e- §areloads config.");
        }

        return true;
    }
}
