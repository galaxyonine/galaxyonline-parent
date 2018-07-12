package io.galaxyonline.view;

import io.galaxyonline.data.World;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private World world;

    public GamePanel(World world) {
        this.world = world;
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(world.getPlayerShip() != null) {
            SpaceShip e = world.getPlayerShip();
            drawCircle(g, (int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getHitboxRadius(), Color.BLACK);
        }

        for(Entity e : world.getEntities()) {
            drawCircle(g, (int) e.getLocation().getX(), (int) e.getLocation().getY(), (int) e.getHitboxRadius(), Color.BLACK);
        }
        System.out.println(world.getEntities().size());
    }

    private void drawCircle(Graphics g, int x, int y, int radius, Color color) {
        g.setColor(color);
        int x1 = x - radius;
        int y1 = y - radius;
        g.drawOval(x1, y1, radius * 2, radius * 2);
    }
}
