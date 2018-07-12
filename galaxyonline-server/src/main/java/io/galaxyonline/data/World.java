package io.galaxyonline.data;

import io.galaxyonline.GameServer;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;
import lombok.Getter;

import java.util.ArrayList;

public class World {
    @Getter
    private GameServer server;
    @Getter
    private ArrayList<Entity> entities;

    private static final int WORLD_WIDTH = 500;
    private static final int WORLD_HEIGHT = 500;

    public World(GameServer gameServer) {
        this.server = gameServer;
        entities = new ArrayList<>();
    }

    public void tick() {
        for (Entity entity : entities) {
            entity.tickEvent();
        }

        for (Player player : server.getPlayers()) {
            player.updatePlayerWorld();
        }
    }

    public boolean contains(Location location) {
        if (location.getWorld().equals(this)) {
            if (location.getX() > 0 && location.getX() < WORLD_WIDTH) {
                if (location.getY() > 0 && location.getY() < WORLD_WIDTH) {
                    return true;
                }
            }
        }
        return false;
    }

    public SpaceShip getPlayerShip(Player player) {
        for (Entity entity : entities) {
            if (entity instanceof SpaceShip) {
                SpaceShip ship = (SpaceShip) entity;
                if (ship.getOwner().equals(player)) return ship;
            }
        }
        return null;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entities.remove(entity);
    }

    public int getWorldWidth() {
        return WORLD_WIDTH;
    }

    public int getWorldHeight() {
        return WORLD_HEIGHT;
    }
}
