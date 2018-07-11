package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import io.galaxyonline.json.JSONable;
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

    Damageable(Location location, double angle, double hitboxRadius, double maxHealth) {
        super(location, hitboxRadius, angle);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void damage(double health) {
        this.health -= health;
        if (this.health < 0) this.health = 0;
    }

    public void heal(double health) {
        this.health += health;
        if (this.health > maxHealth) this.health = maxHealth;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("health", health);
        json.put("maxhealth", maxHealth);
        return json;
    }

    @Override
    public JSONable fromJSON(JSONObject json) {
        health = (int) json.get("health");
        maxHealth = (int) json.get("maxhealth");
        return super.fromJSON(json);
    }
}
