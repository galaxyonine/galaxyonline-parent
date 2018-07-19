package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import lombok.Getter;
import org.json.simple.JSONObject;

public class Bullet extends Entity {
    @Getter
    private SpaceShip shooter;

    private static final double BULLET_SIZE = 25;
    private static final double BULLET_SPEED = 1;
    private static final double BULLET_DAMAGE = 10;

    public Bullet(SpaceShip shooter, Location location) {
        super(location, BULLET_SIZE, shooter.getAngle());
    }

    @Override
    public void tickEvent() {
        setLocation(getLocation().translate(getLocation(), BULLET_SPEED, getAngle()));
        if (!getLocation().getWorld().contains(getLocation())) {
            getLocation().getWorld().removeEntity(this);
        }

        for (Entity entity : getLocation().getWorld().getEntities()) {
            if (collides(entity)) {
                collideEvent(entity);
            }
        }
    }

    @Override
    public void collideEvent(Entity entity) {
        if (entity instanceof Damageable) {
            Damageable damageable = (Damageable) entity;
            damageable.damage(BULLET_DAMAGE);
            getLocation().getWorld().removeEntity(this);
        }
    }

    @Override
    public Bullet fromJSON(JSONObject json) {
        super.fromJSON(json);
        return this;
    }
}
