package io.galaxyonline;

public class MainClient {
    public static void main(String[] args) {
        new GameClient("127.0.0.1", 8888).start();
    }
}
