package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import lombok.Getter;
import lombok.Setter;

public abstract class Entity {
    @Getter
    @Setter
    private Location location;
    @Getter
    @Setter
    private double angle;
    @Getter
    private double hitboxRadius;

    Entity(Location location, double hitboxRadius) {
        this.location = location;
        this.hitboxRadius = hitboxRadius;
        this.angle = 0;
    }

    Entity(Location location, double hitboxRadius, double angle) {
        this.location = location;
        this.hitboxRadius = hitboxRadius;
        this.angle = angle;
    }

    public boolean collides(Entity e) {
        return location.distance(e.getLocation()) < (hitboxRadius + e.getHitboxRadius());
    }

    public void tickEvent() {
    }

    public void collideEvent(Entity e){
    }
}
