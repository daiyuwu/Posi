package com.ty.test.util;

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
}
