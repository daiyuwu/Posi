package com.ty.mctool.command;

import com.ty.mctool.Main;
import com.ty.mctool.model.PlayerPosis;
import com.ty.mctool.model.PosiProcesser;
import com.ty.mctool.util.Message;
import com.ty.mctool.util.Transition;
import net.md_5.bungee.api.ChatColor;
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
            Message.sendErrorMessage(player, "command format error.");
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

            if (posisPlayer == null) {
                PlayerPosis.Player newPlayer = new PlayerPosis.Player(player.getUniqueId().toString());
                playerPosis.getPlayers().add(newPlayer);
                posisPlayer = newPlayer;
            }

            PlayerPosis.Player.World world = posisPlayer.getWorld(player.getWorld().getEnvironment());


            List<PlayerPosis.Player.World.Posi> posis = world.getPosis();
            String posisStr = Transition.toColorMessage("-- posi LIST --\n", ChatColor.BLUE);
            posisStr += Transition.toColorMessage(posis.stream().map(PlayerPosis.Player.World.Posi::getName).reduce("", (acc, name) -> {
                return acc + name + "\n";
            }), ChatColor.GOLD);

            player.sendMessage(posisStr);

        } catch (IOException e) {
            Message.sendErrorMessage(player, "load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void setPosi(String posiName, Player player) {
        PlayerPosis playerPosis;
        try {
            playerPosis = posiProcesser.load();
            PlayerPosis.Player posisPlayer = playerPosis.getPlayer(player.getUniqueId());

            if (posisPlayer == null) {
                PlayerPosis.Player newPlayer = new PlayerPosis.Player(player.getUniqueId().toString());
                playerPosis.getPlayers().add(newPlayer);
                posisPlayer = newPlayer;
            }

            PlayerPosis.Player.World world = posisPlayer.getWorld(player.getWorld().getEnvironment());

            if ("9d374ed1-5a3e-464a-8fe8-a78ebb0cbb93".equals(player.getUniqueId().toString())) {
                int limit = 5;
                if (world.getPosis().size() >= limit) {
                    Message.sendErrorMessage(player, String.format("posi record reached the upper limit(%d)", limit));
                    return;
                }
            }

            boolean nameExists = world.getPosis().stream().anyMatch(p -> {
               return posiName.equals(p.getName());
            });

            if (nameExists) {
                Message.sendErrorMessage(player, posiName + " has existed.");
                return;
            }

            PlayerPosis.Player.World.Posi posi = new PlayerPosis.Player.World.Posi(posiName
                    , player.getLocation().getBlockX()
                    , player.getLocation().getBlockY()
                    , player.getLocation().getBlockZ());

            world.getPosis().add(posi);
            posiProcesser.save(playerPosis);
            Message.sendProcessMessage(player, posiName + " save done.");
        } catch (IOException e) {
            Message.sendErrorMessage(player, "load record error !");
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
                Message.sendErrorMessage(player, "posi name not in this world !");
                return;
            }

            Location location = new Location(player.getWorld()
                    , posi.getPosiX()
                    , posi.getPosiY()
                    , posi.getPosiZ());

            Message.sendProcessMessage(player, "don't move, ready posi to " + posiName + "...");

            BukkitScheduler bs = Bukkit.getScheduler();
            bs.runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    Message.sendProcessMessage(player, "is posi to" + posiName + " !");
                    player.teleport(location);
                }
            }, (long) (3 * 20));
        } catch (IOException e) {
            Message.sendErrorMessage(player, "load record error !");
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
                Message.sendErrorMessage(player, posiName + " has not existed.");
                return;
            }

            List<PlayerPosis.Player.World.Posi> newPosis = world.getPosis().stream().filter(p -> {
                return !posiName.equals(p.getName());
            }).collect(Collectors.toList());
            world.setPosis(newPosis);
            posiProcesser.save(playerPosis);
            Message.sendProcessMessage(player, posiName + " delete done.");

        } catch (IOException e) {
            Message.sendErrorMessage(player, "load record error !");
            this.main.getLogger().warning("player load record error !");
//            e.printStackTrace();
        }
    }

    private void logging(String msg) {
        this.main.getLogger().info(msg);
    }
}
