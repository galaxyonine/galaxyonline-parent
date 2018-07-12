package io.galaxyonline;

public class MainServer {
    public static void main(String[] args) {
//        BasicConfigurator.configure();
        GameServer gameServer = new GameServer(8888);
        gameServer.start();
    }
}
