package io.galaxyonline;

import org.apache.log4j.BasicConfigurator;

public class Main {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer(8888);
        gameServer.start();
    }
}
