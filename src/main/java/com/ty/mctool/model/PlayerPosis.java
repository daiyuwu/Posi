package com.ty.mctool.model;

import org.bukkit.World;

import java.util.*;

import static com.google.common.collect.MoreCollectors.onlyElement;

public class PlayerPosis {

    List<Player> players = new ArrayList<>();

    public Player getPlayer(UUID playerId) {
        Player player;

        if (players.size() == 0)
            return null;
        Optional<Player> playerOpt = players.stream().filter(p -> {
            return playerId.toString().equals(p.id);
        }).findFirst();

        player = playerOpt.orElse(null);

        return player;
    }

    public Player.World.Posi getPosi(String playerId, World.Environment env, String posiName) {

        if (players.size() == 0)
            return null;

        Player player = players.stream().filter(p -> {
            return playerId.toString().equals(p.id);
        }).collect(onlyElement());

        Player.World world;

        switch(env) {
            case NORMAL:
                world = player.getNormal();
                break;
            case NETHER:
                world = player.getNether();
                break;
            case THE_END:
                world = player.getTheEnd();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + env);
        }

        Player.World.Posi posi = world.getPosis().stream().filter(p -> {
            return p.getName().equals(posiName);
        }).findFirst().orElse(null);

        return posi;
    }

    public static class Player {

        public Player() {}
        public Player(String id) {
            this.id = id;
        }

        private String id;
        private World normal = new World();
        private World nether = new World();
        private World theEnd = new World();

        public World getWorld(org.bukkit.World.Environment env) {
            World world = normal;
            switch(env) {
                case NORMAL:
                    world = normal;
                    break;
                case NETHER:
                    world = nether;
                    break;
                case THE_END:
                    world = theEnd;
                    break;
            }
            return world;
        }

        public static class World {

            private int worldId;
            List<Posi> posis = new ArrayList<>();

            public World() {}
            public World(int worldId) {
                this.worldId = worldId;
            }

            public static class Posi {

                String name;
                int worldId;
                int posiX;
                int posiY;
                int posiZ;

                public Posi() {}

                public Posi(String name, int posiX, int posiY, int posiZ) {
                    this.name = name;
                    this.posiX = posiX;
                    this.posiY = posiY;
                    this.posiZ = posiZ;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPosiX() {
                    return posiX;
                }

                public void setPosiX(int posiX) {
                    this.posiX = posiX;
                }

                public int getPosiY() {
                    return posiY;
                }

                public void setPosiY(int posiY) {
                    this.posiY = posiY;
                }

                public int getPosiZ() {
                    return posiZ;
                }

                public void setPosiZ(int posiZ) {
                    this.posiZ = posiZ;
                }
            }

            public int getWorldId() {
                return worldId;
            }

            public void setWorldId(int worldId) {
                this.worldId = worldId;
            }

            public List<Posi> getPosis() {
                return posis;
            }

            public void setPosis(List<Posi> posis) {
                this.posis = posis;
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public World getNormal() {
            return normal;
        }

        public void setNormal(World normal) {
            this.normal = normal;
        }

        public World getNether() {
            return nether;
        }

        public void setNether(World nether) {
            this.nether = nether;
        }

        public World getTheEnd() {
            return theEnd;
        }

        public void setTheEnd(World theEnd) {
            this.theEnd = theEnd;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
