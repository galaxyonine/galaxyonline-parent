package io.galaxyonline;

public class Main {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer(8000);
        gameServer.start();
    }
}
