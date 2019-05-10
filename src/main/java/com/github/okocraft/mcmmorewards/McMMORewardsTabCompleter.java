package com.github.okocraft.mcmmorewards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class McMMORewardsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> resultList = new ArrayList<>();

        List<String> arg0List = Arrays.asList("reload");

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
            case "reload":
                if (Commands.hasPermission(sender, "mcmmorewards.command.reload"))
                    return resultList;
                return StringUtil.copyPartialMatches(args[0], arg0List, resultList);

            }
        }

        if (!arg0List.contains(args[0].toLowerCase())) return resultList;

        return null;
    }

}