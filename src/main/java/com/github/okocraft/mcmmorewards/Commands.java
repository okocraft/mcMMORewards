package com.github.okocraft.mcmmorewards;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0)
            return help(sender);
        String operation = args[0];

        switch (operation) {

        case "reload":
            if (!hasPermission(sender, "mcmmorewards.command." + operation))
                return false;
            McMMORewards.getInstance().getConfigManager().reloadConfig();
            sender.sendMessage("コンフィグを再読込しました。");
            return true;

        default:
            return help(sender);
        }
    }

    private boolean help(CommandSender sender) {
        sender.sendMessage("§c[]=====[] §amcMMORewards help §c[]=====[]");
        if (sender.hasPermission("mcmmorewards.command.reload"))
            sender.sendMessage("§3/mmorewards reload §e- §areloads config.");
        return true;
    }

    protected static boolean errorOccured(CommandSender sender, String message) {
        sender.sendMessage(message);
        return false;
    }

    protected static boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission))
            return errorOccured(sender, ChatColor.RED + permission + "の権限がありません");

        return true;
    }

}