package io.galaxyonline.data;

import io.galaxyonline.json.JSONable;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class Location implements JSONable {
    @Getter
    private World world;
    @Getter
    @Setter
    private double x;
    @Getter
    @Setter
    private double y;

    public Location(World world, double x, double y) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Location location) {
        double distX = x - location.x;
        double distY = y - location.y;
        return Math.hypot(distX, distY);
    }

    public Location translate(Location location, double distance, double angle) {
        double rad = Math.toRadians(angle);
        double newX = (location.x + distance * Math.cos(rad));
        double newY = (location.y + distance * Math.sin(rad));
        return new Location(world, newX, newY);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("x", x);
        json.put("y", y);
        return json;
    }

    @Override
    public JSONable fromJSON(JSONObject json) {
        x = (double) json.get("x");
        y = (double) json.get("y");
        return this;
    }
}
