package io.galaxyonline.data;

import io.scalecube.socketio.Session;
import lombok.Getter;
import lombok.Setter;

public class Player {
    @Getter
    private Session session;
    @Getter
    @Setter
    private String username;

    public Player(Session session, String username) {
        this.session = session;
        this.username = username;
    }

    public void updatePlayerData() {
        //SEND ALL DATA TO PLAYER
    }

    public void updatePlayerWorld() {
        //SEND ALL ENTITIES TO PLAYER
    }
}
