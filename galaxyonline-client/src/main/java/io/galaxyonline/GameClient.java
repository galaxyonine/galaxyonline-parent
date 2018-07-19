package io.galaxyonline;

import io.galaxyonline.data.Location;
import io.galaxyonline.data.World;
import io.galaxyonline.data.entity.SpaceShip;
import io.galaxyonline.packet.PacketEvent;
import io.galaxyonline.view.GameFrame;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.KeyEvent;

public class GameClient {
    @Getter
    private GameFrame gameFrame;
    @Getter
    private InputController inputController;
    @Getter
    private World world;

    private String ip;
    private int port;
    private Socket socket;

    private Thread gameThread;
    private static final int GAME_TPS = 60;

    public GameClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        world = new World(this);
        inputController = new InputController(this);
        gameFrame = new GameFrame(this);
        connect();
        startGameThread();
    }

    private void connect() {
        if (socket != null && socket.connected()) socket.close();
        try {
            socket = IO.socket("http://" + ip + ":" + port);
            socket.on(Socket.EVENT_CONNECT, (objects) -> {
                System.out.println("client connected");
            }).on(Socket.EVENT_DISCONNECT, (objects) -> {
                System.out.println("client disconnected");
            }).on(PacketEvent.WORLD_UPDATE.toString(), (objects) -> {
                System.out.println(PacketEvent.WORLD_UPDATE);
                for (Object o : objects) {
                    System.out.println("Received: " + o);
                }
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse((String) objects[0]);
                    world.fromJSON((JSONObject) json.get("world"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).on(PacketEvent.PLAYER_UPDATE.toString(), (objects) -> {
                System.out.println(PacketEvent.PLAYER_UPDATE);
                for (Object o : objects) {
                    System.out.println("Received: " + o);
                }
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse((String) objects[0]);
                    JSONObject playerData = (JSONObject) json.get("playerdata");
                    world.setPlayerShip(new SpaceShip(world).fromJSON((JSONObject) playerData.get("ship")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });

            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) gameThread.interrupt();
        gameThread = new Thread(() -> {
            try {
                int tickDelay = 1000 / GAME_TPS;
                while (true) {
                    long tickStart = System.currentTimeMillis();
                    tickGame();
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

    private void tickGame() {
        tickPlayer();
        gameFrame.repaint();
    }

    private void tickPlayer() {
        SpaceShip spaceShip = world.getPlayerShip();
        if (spaceShip != null) {
            Location location = spaceShip.getLocation();
            if (inputController.getHeldKeys().contains(KeyEvent.VK_W)) {
                movePlayer(location.getX(), location.getY() - spaceShip.getShipSpeed(), spaceShip.getAngle());
            }
            if (inputController.getHeldKeys().contains(KeyEvent.VK_A)) {
                movePlayer(location.getX() - spaceShip.getShipSpeed(), location.getY(), spaceShip.getAngle());
            }
            if (inputController.getHeldKeys().contains(KeyEvent.VK_S)) {
                movePlayer(location.getX(), location.getY() + spaceShip.getShipSpeed(), spaceShip.getAngle());
            }
            if (inputController.getHeldKeys().contains(KeyEvent.VK_D)) {
                movePlayer(location.getX() + spaceShip.getShipSpeed(), location.getY(), spaceShip.getAngle());
            }
        }
    }

    public void movePlayer(double x, double y, double angle) {
        SpaceShip spaceShip = world.getPlayerShip();
        if (spaceShip != null) {
            spaceShip.getLocation().setXY(x, y);
            spaceShip.setAngle(angle);
            sendMovePacket();
        }
    }

    private void sendMovePacket() {
        SpaceShip spaceShip = world.getPlayerShip();
        if (spaceShip != null) {
            JSONObject json = world.getPlayerShip().getLocation().toJSON();
            socket.emit(PacketEvent.PLAYER_MOVE.toString(), json.toJSONString());
        }
    }

    public void quitGame() {

    }

    public void respawn() {
        socket.emit(PacketEvent.PLAYER_SPAWN.toString(), "respawn");
    }
}
