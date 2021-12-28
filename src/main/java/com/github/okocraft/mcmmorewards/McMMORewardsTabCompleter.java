package com.github.okocraft.mcmmorewards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class McMMORewardsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && args[0].length() < 7 &&
                args[0].toLowerCase(Locale.ENGLISH).startsWith("reload") &&
                sender.hasPermission("mcmmorewards.command.reload")) {
            return List.of("reload");
        } else {
            return Collections.emptyList();
        }
    }
}
