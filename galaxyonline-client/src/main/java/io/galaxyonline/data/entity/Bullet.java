package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import org.json.simple.JSONObject;

public class Bullet extends Entity {
    private static final double BULLET_SIZE = 25;

    public Bullet(SpaceShip shooter, Location location) {
        super(location, BULLET_SIZE, shooter.getAngle());
    }

    public Bullet() {
        super(null, 0, 0);
    }

    @Override
    public Bullet fromJSON(JSONObject json) {
        super.fromJSON(json);
        return this;
    }
}
