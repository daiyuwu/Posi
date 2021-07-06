package com.ty.mctool.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Message {

    public static void sendErrorMessage(Player player, String message) {
        player.sendMessage(Transition.toColorMessage(message, ChatColor.RED));
    }

    public static void sendProcessMessage(Player player, String message) {
        player.sendMessage(Transition.toColorMessage(message, ChatColor.LIGHT_PURPLE));
    }
}
