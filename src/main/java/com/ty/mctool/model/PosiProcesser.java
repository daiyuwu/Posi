package com.ty.mctool.model;

import com.google.gson.Gson;
import com.ty.mctool.Main;

import java.io.*;

public class PosiProcesser {

    private Main main;

    private String dataFileName = "data.json";
    private final File conf;
    Gson gson = new Gson();

    public PosiProcesser(Main main) throws IOException {

        this.main = main;

        this.conf = new File(main.getDataFolder(), dataFileName);
        main.saveResource(dataFileName, false);
    }

    public PlayerPosis load() throws FileNotFoundException {
        Reader  reader = new InputStreamReader(new FileInputStream(this.conf));
        PlayerPosis playerPosis = gson.fromJson(reader, PlayerPosis.class);

        return playerPosis;
    }

    public void save(PlayerPosis playerPosis) throws IOException {
        String playerPosisJson = gson.toJson(playerPosis);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.conf));
        writer.write(playerPosisJson);

        writer.close();
    }
}
