package io.galaxyonline.data;

import com.corundumstudio.socketio.SocketIOClient;
import io.galaxyonline.GameServer;
import io.galaxyonline.data.entity.Entity;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.packet.PacketEvent;
import io.galaxyonline.util.RNGUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

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
        JSONObject playerData = new JSONObject();
        SpaceShip ship = server.getWorld().getPlayerShip(this);
        if (ship != null) {
            playerData.put("ship", ship.toJSON());
        }
        json.put("playerdata", playerData);
        session.sendEvent(PacketEvent.PLAYER_UPDATE.toString(), json.toJSONString());
    }

    public void updatePlayerWorld() {
        //SEND ALL ENTITIES TO PLAYER
        JSONObject json = new JSONObject();
        JSONObject world = new JSONObject();
        JSONObject entities = new JSONObject();
        int count = 0;
        for (Entity e : server.getWorld().getEntities()) {
            if (e instanceof SpaceShip) {
                if (((SpaceShip) e).getOwner().equals(this)) continue;
            }
            entities.put(count, e.toJSON());
            count++;
        }
        world.put("entities", entities);
        json.put("world", world);
        session.sendEvent(PacketEvent.WORLD_UPDATE.toString(), json.toJSONString());
    }

    public boolean attemptCreateShip() {
        if(server.getWorld().getPlayerShip(this) == null) {
            int x = RNGUtils.randomInt(0, server.getWorld().getWorldWidth());
            int y = RNGUtils.randomInt(0, server.getWorld().getWorldHeight());
            Location location = new Location(server.getWorld(), x, y);
            SpaceShip ship = new SpaceShip(this, location);
            server.getWorld().addEntity(ship);
        }
        return false;
    }
}
