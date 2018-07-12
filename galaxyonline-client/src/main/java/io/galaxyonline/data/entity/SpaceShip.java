package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import io.galaxyonline.data.World;
import lombok.Getter;
import org.json.simple.JSONObject;

public class SpaceShip extends Damageable {
    @Getter
    private String owner;

    private static final double SHIP_SIZE = 50;
    private static final double SHIP_HEALTH = 100;
    private static final double SHIP_SPEED = 5;

    public SpaceShip(String owner, Location location) {
        super(location, SHIP_SIZE, SHIP_HEALTH);
        this.owner = owner;
    }

    public SpaceShip(World world) {
        super(new Location(world,0, 0), SHIP_SIZE, SHIP_HEALTH);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("owner", owner);
        return json;
    }

    @Override
    public SpaceShip fromJSON(JSONObject json) {
        super.fromJSON(json);
        return this;
    }

    public double getShipSpeed() {
        return SHIP_SPEED;
    }
}

