package com.ty.test.model;

import com.google.gson.Gson;
import com.ty.test.Main;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class PosiProcesser {

    private Main main;

//    private String folderName = "posis";
    private String dataFileName = "data.json";
//    private final File folder;
    private final File conf;
    Gson gson = new Gson();

    public PosiProcesser(Main main) throws IOException {

        this.main = main;

//        this.folder = new File(main.getDataFolder(), folderName);
        this.conf = new File(main.getDataFolder(), dataFileName);
//        if (!this.folder.exists())
//            this.folder.mkdir();
//        this.conf = new File(this.folder, "data.json");
//        if (!this.conf.exists())
//            this.conf.createNewFile();
        main.saveResource(dataFileName, false);

//        testLoad();
    }

    public PlayerPosis load() throws FileNotFoundException {
//        Reader  reader = new InputStreamReader(new FileInputStream(new File(main.getDataFolder(), "posis/data.json")));
        Reader  reader = new InputStreamReader(new FileInputStream(this.conf));
        PlayerPosis playerPosis = gson.fromJson(reader, PlayerPosis.class);

        return playerPosis;
    }

    public void save(PlayerPosis playerPosis) throws IOException {
//        Yaml yaml = new Yaml();
//        FileWriter writer = new FileWriter(this.conf.getPath());
//        yaml.dump(playerPosis, writer);
        String playerPosisJson = gson.toJson(playerPosis);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.conf));
        writer.write(playerPosisJson);

        writer.close();
    }

//    public void testLoad() throws IOException {
//        testLoadByGson();
//    }
//
//    public void testLoadByGson() throws FileNotFoundException {
//        Gson gson = new Gson();
//        Reader  reader = new InputStreamReader(new FileInputStream(new File(main.getDataFolder(), "posis/data.json")));
//        PlayerPosis target = gson.fromJson(reader, PlayerPosis.class);
//        logging("target: " + target);
//    }

    public void testLoadBySneak() throws IOException {
//        this.main.getLogger().info(this.config.toPath().toUri());
//        InputStream inputStream = new FileInputStream(this.config);
        InputStream inputStream = new FileInputStream(new File(main.getDataFolder(), "data.json"));

//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        Customer target = mapper.readValue(new File("posis/data.json"), Customer.class);
//        InputStream inputStream = main.getResource("posis/data.json");

//        Yaml yaml = new Yaml(new Customer());
        final Yaml yaml = new Yaml(new Constructor(Customer.class));
//        Yaml yaml = new Yaml();

//        PlayerPosis playerPosis = yaml.load(Files.newInputStream(this.config.toPath()));
//        this.main.getLogger().info("file path: " + this.config.getAbsolutePath());
        Object target = yaml.load(inputStream);
//        Customer target = (Customer) yaml.load(inputStream);

        this.main.getLogger().info("target: " + target);

//        main.saveDefaultConfig();
//        FileConfiguration config = main.getResource("posis/data.json");
//        logging("path: " + config.getCurrentPath());
    }

//    public void testLoadByFastJson() throws IOException {
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        Customer target = mapper.readValue(new File(main.getDataFolder(), "posis/data.json"), Customer.class);
//        logging("target: " + target);
//    }

//    public PlayerPosis load() throws IOException {
////        this.main.getLogger().info(this.config.toPath().toUri());
//        InputStream inputStream = new FileInputStream(this.config);
//
////        Yaml yaml = new Yaml(new Constructor(PlayerPosis.class));
////        PlayerPosis playerPosis = yaml.load(Files.newInputStream(this.config.toPath()));
//        this.main.getLogger().info("file path: " + this.config.getAbsolutePath());
////        PlayerPosis playerPosis = yaml.load(new FileInputStream(this.config));
////        this.main.getLogger().info("playerPosis: " + playerPosis);
////
//        return playerPosis;
//    }

//    public File getConf() {
//        return conf;
//    }

    private void logging(String msg) {
        this.main.getLogger().info(msg);
    }
}
