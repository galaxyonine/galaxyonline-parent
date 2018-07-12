package io.galaxyonline;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Implementation of a class to store all held buttons that the user is holding while playing.
 */
public class InputController {
    private GameClient client;
    private ArrayList<Integer> heldKeys;

    public InputController(GameClient client) {
        this.client = client;
        heldKeys = new ArrayList<>();
    }

    /**
     * Unregister a key from the pressed keys.
     *
     * @param key Key that has been released.
     */
    public void releaseKey(Integer key) {
        heldKeys.remove(key);
    }

    /**
     * Register a key in the pressed keys. Executes any actions bound to keys instantly.
     *
     * @param key Key that has been pressed.
     */
    public void pressKey(Integer key) {
        if (!getHeldKeys().contains(key))
            heldKeys.add(key);

        if (key == KeyEvent.VK_ESCAPE) {
            client.quitGame();
        }
        if(key == KeyEvent.VK_R) {
            client.respawn();
        }
    }

    /**
     * Get all the keys currently held by the user.
     *
     * @return Held keys.
     */
    public ArrayList<Integer> getHeldKeys() {
        return heldKeys;
    }
}
