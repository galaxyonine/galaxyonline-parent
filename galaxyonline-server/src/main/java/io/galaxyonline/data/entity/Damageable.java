package io.galaxyonline.data.entity;

import io.galaxyonline.data.Location;
import lombok.Getter;
import lombok.Setter;

public abstract class Damageable extends Entity {
    @Getter
    @Setter
    protected  double maxHealth;
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
        if(this.health < 0) this.health = 0;
    }

    public void heal(double health) {
        this.health += health;
        if(this.health > maxHealth) this.health = maxHealth;
    }
}
