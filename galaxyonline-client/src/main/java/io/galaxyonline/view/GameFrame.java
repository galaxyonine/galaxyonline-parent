package io.galaxyonline.view;

import io.galaxyonline.GameClient;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    private GameClient client;
    @Getter
    private GamePanel gamePanel;

    public GameFrame(GameClient client) {
        this.client = client;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gamePanel = new GamePanel(client.getWorld());
        setContentPane(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addKeyListener(keyAdapter());
    }

    private KeyAdapter keyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                client.getInputController().pressKey(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                client.getInputController().releaseKey(e.getKeyCode());
            }
        };
    }
}
