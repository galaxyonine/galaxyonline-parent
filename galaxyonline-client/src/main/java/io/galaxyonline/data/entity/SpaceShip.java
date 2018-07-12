package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import lombok.Getter;
import org.json.simple.JSONObject;

public class SpaceShip extends Damageable {
    @Getter
    private String owner;
    @Getter
    private Location location;

    private static final double SHIP_SIZE = 50;
    private static final double SHIP_HEALTH = 100;

    public SpaceShip(String owner, Location location) {
        super(location, SHIP_SIZE, SHIP_HEALTH);
        this.owner = owner;
    }

    public SpaceShip() {
        super(null, SHIP_SIZE, SHIP_HEALTH);
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
}

