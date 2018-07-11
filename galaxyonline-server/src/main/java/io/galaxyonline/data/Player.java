package io.galaxyonline.data;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import io.galaxyonline.GameServer;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.packet.PacketType;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.nio.charset.Charset;

public class Player {
    private GameServer server;
    @Getter
    private SocketIOClient session;
    @Getter
    @Setter
    private String username;

    public Player(GameServer server, SocketIOClient session, String username) {
        this.server = server;
        this.session = session;
        this.username = username;
    }

    public void updatePlayerData() {
        //SEND ALL DATA TO PLAYER
        JSONObject json = new JSONObject();
        json.put("type", PacketType.PLAYER_UPDATE.toString());
        JSONObject playerData = new JSONObject();
        SpaceShip ship = server.getWorld().getPlayerShip(this);
        if (ship != null) {
            playerData.put("ship", ship.toJSON());
        }
        json.put("playerdata", playerData);
        session.sendEvent(PacketType.PLAYER_UPDATE.toString(), json.toJSONString());
    }

    public void updatePlayerWorld() {
        //SEND ALL ENTITIES TO PLAYER
        JSONObject json = new JSONObject();
        JSONObject world = new JSONObject();
        int count = 0;
        for (Entity e : server.getWorld().getEntities()) {
            if (e instanceof SpaceShip) {
                if (((SpaceShip) e).getOwner().equals(this)) continue;
            }
            world.put("ent" + count, e.toJSON());
            count++;
        }
        json.put("world", world);
        session.sendEvent(PacketType.WORLD_UPDATE.toString(), json.toJSONString());
    }
}
