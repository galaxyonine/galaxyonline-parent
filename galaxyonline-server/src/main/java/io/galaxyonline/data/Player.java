package io.galaxyonline.data;

import io.galaxyonline.GameServer;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.packet.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.scalecube.socketio.Session;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.nio.charset.Charset;

public class Player {
    private GameServer server;
    @Getter
    private Session session;
    @Getter
    @Setter
    private String username;

    public Player(GameServer server, Session session, String username) {
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
        if(ship != null) {
            playerData.put("ship", ship.toJSON());
        }
        json.put("playerdata", playerData);
        session.send(Unpooled.wrappedBuffer(json.toJSONString().getBytes(Charset.forName("UTF-8"))));
    }

    public void updatePlayerWorld() {
        //SEND ALL ENTITIES TO PLAYER
        JSONObject json = new JSONObject();
        json.put("type", PacketType.WORLD_UPDATE.toString());
        JSONObject world = new JSONObject();
        int count = 0;
        for(Entity e : server.getWorld().getEntities()) {
            if(e instanceof SpaceShip) {
                if(((SpaceShip) e).getOwner().equals(this)) continue;
            }
            world.put("ent"+count, e.toJSON());
            count++;
        }
        json.put("world", world);
        session.send(Unpooled.wrappedBuffer(json.toJSONString().getBytes(Charset.forName("UTF-8"))));
    }
}
