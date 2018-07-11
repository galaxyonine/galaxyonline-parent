package io.galaxyonline;

import io.galaxyonline.data.Player;
import io.galaxyonline.data.World;
import io.netty.buffer.ByteBuf;
import io.scalecube.socketio.Session;
import io.scalecube.socketio.SocketIOListener;
import io.scalecube.socketio.SocketIOServer;
import lombok.Getter;

import java.util.ArrayList;

public class GameServer {
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
        if (server != null && !server.isStopped()) server.stop();
    }

    private void startServer(int port) {
        if (server != null && !server.isStopped()) server.stop();
        GameServer gameServer = this;
        server = SocketIOServer.newInstance(port);
        server.setListener(new SocketIOListener() {
            public void onConnect(Session session) {
                System.out.println("Connected: " + session);
                Player player = new Player(gameServer, session, "Anonymous");
                players.add(player);
            }

            public void onDisconnect(Session session) {
                System.out.println("Disconnected: " + session);
                for (Player player : players) {
                    if (player.getSession().equals(session)) players.remove(player);
                }
            }

            @Override
            public void onMessage(Session session, ByteBuf byteBuf) {
                System.out.println(byteBuf.toString());
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
}
