package com.ty.mctool.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;

public class Transition {

    public static int trans2Id(World.Environment env) {
        int id = -999;
        switch(env) {
            case NORMAL:
                id = 0;
                break;
            case NETHER:
                id = -1;
                break;
            case THE_END:
                id = 1;
                break;
        }
        return id;
    }

    public static String toColorMessage(String message, ChatColor chatColor) {
        return chatColor.toString() + message;
    }
}
