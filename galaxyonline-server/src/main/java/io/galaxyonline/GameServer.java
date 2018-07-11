package io.galaxyonline;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.galaxyonline.data.Player;
import io.galaxyonline.data.World;
import io.galaxyonline.packet.PacketEvent;
import lombok.Getter;

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
    private static final int GAME_TPS = 20;

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
                if(player.getSession().equals(session)) players.remove(i);
            }
        });

        server.addEventListener("packetevent", Object.class, (socketIOClient, string, ackRequest) -> {
            System.out.println(string);
            socketIOClient.sendEvent("packetevent", "pong");
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
}
