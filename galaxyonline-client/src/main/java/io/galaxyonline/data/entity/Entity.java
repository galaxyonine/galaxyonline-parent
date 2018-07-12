package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import io.galaxyonline.json.JSONable;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public abstract class Entity implements JSONable {
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

    public void collideEvent(Entity e) {
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", getClass().getSimpleName());
        json.put("location", location.toJSON());
        json.put("angle", angle);
        return json;
    }

    @Override
    public Entity fromJSON(JSONObject json) {
        String type = (String) json.get("type");
        if (type.equals(getClass().getSimpleName())) {
            location.fromJSON((JSONObject) json.get("location"));
            angle = (double) json.get("angle");
        }
        return this;
    }
}
