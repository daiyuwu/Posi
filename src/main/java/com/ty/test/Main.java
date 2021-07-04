package com.ty.test;

import com.ty.test.command.PosiCmd;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        reloadConfig();
        saveDefaultConfig();

//        if (!this.getDataFolder().exists()) {
//            this.getDataFolder().mkdir();
//        }
//        this.getLogger().info("folder: " + this.getDataFolder());

        new PosiCmd(this);
    }
}
