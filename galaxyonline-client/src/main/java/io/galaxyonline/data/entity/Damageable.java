package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public abstract class Damageable extends Entity {
    @Getter
    @Setter
    protected double maxHealth;
    @Getter
    @Setter
    protected double health;

    Damageable(Location location, double hitboxRadius, double maxHealth) {
        super(location, hitboxRadius, maxHealth);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public Damageable fromJSON(JSONObject json) {
        super.fromJSON(json);
        health = (double) json.get("health");
        maxHealth = (double) json.get("maxhealth");
        return this;
    }
}
