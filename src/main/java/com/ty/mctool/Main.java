package com.ty.mctool;

import com.ty.mctool.command.PosiCmd;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class Main extends JavaPlugin {

    public Main() {
        super();
    }

    protected Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {

        reloadConfig();
        saveDefaultConfig();

        new PosiCmd(this);
    }
}
