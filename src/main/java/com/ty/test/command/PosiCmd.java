package com.ty.test.command;

import com.ty.test.Main;
import com.ty.test.model.PlayerPosis;
import com.ty.test.model.PosiProcesser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PosiCmd implements CommandExecutor {

    private Main main;
    private PosiProcesser posiProcesser;

    public PosiCmd(Main main) {
        this.main = main;
        this.main.getCommand("setPosi").setExecutor(this);
        this.main.getCommand("posi").setExecutor(this);
        this.main.getCommand("delPosi").setExecutor(this);
        this.main.getCommand("posiList").setExecutor(this);

        try {
            this.posiProcesser = new PosiProcesser(main);
        } catch (IOException e) {
            this.main.getLogger().warning("can't init Posi config !");
            e.printStackTrace();
        }
    }

    public boolean isLength(String[] args, int length, Player player) {
        if (args.length != length) {
            player.sendMessage("command format error.");
            return false;
        }

        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName()) {
                case "setPosi":
                    if (!isLength(args, 1, player))
                        return false;
                    setPosi(args[0], player);
                    break;
                case "posi":
                    if (!isLength(args, 1, player))
                        return false;
                    posi(args[0], player);
                    break;
                case "delPosi":
                    if (!isLength(args, 1, player))
                        return false;
                    delPosi(args[0], player);
                    break;
                case "posiList":
                    if (!isLength(args, 0, player))
                        return false;
                    posiList(player);
                    break;
            }
        } else {
            this.main.getLogger().info("test fail !");
        }
        return true;
    }

    private void posiList(Player player) {
        PlayerPosis playerPosis;
        try {
            playerPosis = posiProcesser.load();
            PlayerPosis.Player posisPlayer = playerPosis.getPlayer(player.getUniqueId());

            logging("posisPlayer: " + posisPlayer);
            if (posisPlayer == null) {
                PlayerPosis.Player newPlayer = new PlayerPosis.Player(player.getUniqueId().toString());
                playerPosis.getPlayers().add(newPlayer);
                posisPlayer = newPlayer;
            }

            PlayerPosis.Player.World world = posisPlayer.getWorld(player.getWorld().getEnvironment());


            List<PlayerPosis.Player.World.Posi> posis = world.getPosis();
            String posisStr = posis.stream().map(PlayerPosis.Player.World.Posi::getName).reduce((acc, name) -> {
                return acc + name + "\n";
            }).get();

            player.sendMessage(posisStr);
        } catch (IOException e) {
            player.sendMessage("load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void setPosi(String posiName, Player player) {
        PlayerPosis playerPosis;
        try {
            playerPosis = posiProcesser.load();
            PlayerPosis.Player posisPlayer = playerPosis.getPlayer(player.getUniqueId());

            logging("posisPlayer: " + posisPlayer);
            if (posisPlayer == null) {
                PlayerPosis.Player newPlayer = new PlayerPosis.Player(player.getUniqueId().toString());
                playerPosis.getPlayers().add(newPlayer);
                posisPlayer = newPlayer;
            }

            PlayerPosis.Player.World world = posisPlayer.getWorld(player.getWorld().getEnvironment());

            if ("9d374ed1-5a3e-464a-8fe8-a78ebb0cbb93".equals(player.getUniqueId().toString())) {
                int limit = 5;
                if (world.getPosis().size() >= limit) {
                    player.sendMessage(String.format("posi record reached the upper limit(%d)", limit));
                    return;
                }
            }

            boolean nameExists = world.getPosis().stream().anyMatch(p -> {
               return posiName.equals(p.getName());
            });

            if (nameExists) {
                player.sendMessage(posiName + " has existed.");
                return;
            }

            PlayerPosis.Player.World.Posi posi = new PlayerPosis.Player.World.Posi(posiName
                    , player.getLocation().getBlockX()
                    , player.getLocation().getBlockY()
                    , player.getLocation().getBlockZ());

            world.getPosis().add(posi);
            posiProcesser.save(playerPosis);
            player.sendMessage(posiName + " save done."); // 9d374ed1-5a3e-464a-8fe8-a78ebb0cbb93
        } catch (IOException e) {
            player.sendMessage("load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void posi(String posiName, Player player) {

        PlayerPosis playerPosis;
        try {
            playerPosis = posiProcesser.load();
            PlayerPosis.Player.World.Posi posi = playerPosis.getPosi(player.getUniqueId().toString()
                    , player.getWorld().getEnvironment()
                    , posiName);

            if (posi == null) {
                player.sendMessage("posi name not in this world !");
                return;
            }

            Location location = new Location(player.getWorld()
                    , posi.getPosiX()
                    , posi.getPosiY()
                    , posi.getPosiZ());

            player.sendMessage("don't move, ready posi to " + posiName + "...");

            BukkitScheduler bs = Bukkit.getScheduler();
            bs.runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    player.sendMessage("is posi to" + posiName + " !");
                    player.teleport(location);
                }
            }, (long) (3 * 20));
        } catch (IOException e) {
            player.sendMessage("load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void delPosi(String posiName, Player player) {

        PlayerPosis playerPosis;
        try {
            playerPosis = posiProcesser.load();
            PlayerPosis.Player posisPlayer = playerPosis.getPlayer(player.getUniqueId());
            PlayerPosis.Player.World world = posisPlayer.getWorld(player.getWorld().getEnvironment());

            boolean nameExists = world.getPosis().stream().anyMatch(p -> {
                return posiName.equals(p.getName());
            });

            if (!nameExists) {
                player.sendMessage(posiName + " has not existed.");
                return;
            }

            List<PlayerPosis.Player.World.Posi> newPosis = world.getPosis().stream().filter(p -> {
                return !posiName.equals(p.getName());
            }).collect(Collectors.toList());
            world.setPosis(newPosis);
            posiProcesser.save(playerPosis);
            player.sendMessage(posiName + " delete done.");

        } catch (IOException e) {
            player.sendMessage("load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void logging(String msg) {
        this.main.getLogger().info(msg);
    }
}
