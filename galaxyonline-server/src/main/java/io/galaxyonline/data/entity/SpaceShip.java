package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import io.galaxyonline.data.Player;
import lombok.Getter;
import org.json.simple.JSONObject;

public class SpaceShip extends Damageable {
    @Getter
    private Player owner;

    private static final double SHIP_SIZE = 50;
    private static final double SHIP_HEALTH = 100;

    public SpaceShip(Player owner, Location location) {
        super(location, SHIP_SIZE, SHIP_HEALTH);
        this.owner = owner;
    }

    public void shoot() {
        Bullet bullet = new Bullet(this, getLocation());
        getLocation().getWorld().addEntity(bullet);
    }

    @Override
    public void damageEvent() {
        if (owner != null) {
            owner.updatePlayerData();
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("owner", owner.getUsername());
        return json;
    }

    @Override
    public SpaceShip fromJSON(JSONObject json) {
        super.fromJSON(json);
        return this;
    }
}
