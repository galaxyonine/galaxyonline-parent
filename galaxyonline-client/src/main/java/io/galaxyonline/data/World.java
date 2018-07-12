package io.galaxyonline.data;

import io.galaxyonline.GameClient;
import io.galaxyonline.data.entity.Bullet;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.json.JSONable;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class World implements JSONable {
    @Getter
    private GameClient client;
    @Getter
    private ArrayList<Entity> entities;
    @Getter
    @Setter
    private SpaceShip playerShip;

    private static final int WORLD_WIDTH = 500;
    private static final int WORLD_HEIGHT = 500;

    public World(GameClient gameClient) {
        this.client = gameClient;
        entities = new ArrayList<>();
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

    public int getWorldWidth() {
        return WORLD_WIDTH;
    }

    public int getWorldHeight() {
        return WORLD_HEIGHT;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public JSONable fromJSON(JSONObject json) {
        ArrayList<Entity> entities = new ArrayList<>();
        JSONObject entitiesJSON = (JSONObject) json.get("entities");
        for (Object key : entitiesJSON.keySet()) {
            String keyS = (String) key;
            JSONObject entityJSON = (JSONObject) entitiesJSON.get(keyS);
            switch ((String) entityJSON.get("type")) {
                case "Bullet":
                    Bullet bullet = new Bullet(this).fromJSON(entityJSON);
                    entities.add(bullet);
                    break;
                case "SpaceShip":
                    SpaceShip spaceShip = new SpaceShip(this).fromJSON(entityJSON);
                    entities.add(spaceShip);
                    break;
            }
        }
        this.entities = entities;
        return this;
    }
}
