package io.galaxyonline;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.galaxyonline.data.Player;
import io.galaxyonline.data.World;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.packet.PacketEvent;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class GameServer {
    @Getter
    private SocketIOServer server;
    private int port;

    @Getter
    private ArrayList<Player> players;
    @Getter
    private World world;

    private Thread gameThread;
    private static final int GAME_TPS = 60;

    public GameServer(int port) {
        this.port = port;
        world = new World(this);
        players = new ArrayList<>();
    }

    public void start() {
        startServer(port);
        startGameThread();
    }

    public void stop() {
        if (gameThread != null && gameThread.isAlive()) gameThread.interrupt();
        if (server != null) server.stop();
    }

    private void startServer(int port) {
        if (server != null) server.stop();

        Configuration config = new Configuration();
        config.setPort(port);
        server = new SocketIOServer(config);

        server.addConnectListener((session) -> {
            System.out.println("Connected " + session.getRemoteAddress());
            Player player = new Player(this, session, "Anonymous");
            players.add(player);
        });

        server.addDisconnectListener((session) -> {
            System.out.println("Disconnected " + session.getRemoteAddress());
            for(int i = players.size() - 1; i >= 0; i--) {
                Player player = players.get(i);
                if(player.getSession().equals(session)) {
                    players.remove(i);
                    SpaceShip ship = world.getPlayerShip(player);
                    if(ship != null) world.removeEntity(ship);
                }
            }
        });

        server.addEventListener(PacketEvent.PLAYER_SPAWN.toString(), Object.class, (session, string, ackRequest) -> {
            System.out.println(PacketEvent.PLAYER_SPAWN);
            System.out.println(string);
            Player player = getPlayerFromSession(session);
            player.attemptCreateShip();
            player.updatePlayerData();
        });

        server.addEventListener(PacketEvent.PLAYER_MOVE.toString(), Object.class, (session, string, ackRequest) -> {
            System.out.println(PacketEvent.PLAYER_MOVE);
            System.out.println(string);
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse((String) string);
                Player player = getPlayerFromSession(session);
                if(player != null) {
                    SpaceShip ship = world.getPlayerShip(player);
                    if(ship != null) {
                        ship.getLocation().fromJSON(json);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        server.addEventListener(PacketEvent.PLAYER_SHOOT.toString(), Object.class, (session, string, ackRequest) -> {
            System.out.println(PacketEvent.PLAYER_SHOOT);
            System.out.println(string);
            Player player = getPlayerFromSession(session);
            if(player != null) {
                SpaceShip ship = world.getPlayerShip(player);
                if (ship != null) {
                    ship.shoot();
                }
            }
        });

        System.out.println("Server Starting");
        server.start();
        System.out.println("Server Started");
    }

    private void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) gameThread.interrupt();
        gameThread = new Thread(() -> {
            try {
                int tickDelay = 1000 / GAME_TPS;
                while (true) {
                    long tickStart = System.currentTimeMillis();
                    world.tick();
                    long tickEnd = System.currentTimeMillis();
                    long sleepTime = tickDelay - (tickEnd - tickStart);
                    if (sleepTime > 0) Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        gameThread.start();
    }

    public Player getPlayerFromSession(SocketIOClient session) {
        for(Player player : players) {
            if(session.equals(player.getSession())) return player;
        }
        return null;
    }
}
