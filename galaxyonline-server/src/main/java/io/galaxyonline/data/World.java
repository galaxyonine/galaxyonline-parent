package io.galaxyonline.data;

import io.galaxyonline.data.entity.Entity;
import lombok.Getter;

import java.util.ArrayList;

public class World {
    @Getter
    private ArrayList<Entity> entities;

    private static final int WORLD_WIDTH = 2000;
    private static final int WORLD_HEIGHT = 2000;

    public World() {

    }

    public void tick() {
        for(Entity entity : entities) {
            entity.tickEvent();
        }
    }

    public boolean contains(Location location) {
        if(location.getWorld().equals(this)) {
            if(location.getX() > 0 && location.getX() < WORLD_WIDTH) {
                if(location.getY() > 0 && location.getY() < WORLD_WIDTH) {
                    return true;
                }
            }
        }
        return false;
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
