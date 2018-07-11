package io.galaxyonline;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            Socket socket = IO.socket("http://127.0.0.1:8888");
            socket.on(Socket.EVENT_CONNECT, (objects) -> {
                System.out.println("client connected");
            }).on(Socket.EVENT_DISCONNECT, (objects) -> {
                System.out.println("client disconnected");
            }).on("packetevent", (objects) -> {
                System.out.println("received 1");
                System.out.println(objects);
            }).on(Socket.EVENT_MESSAGE, objects -> {
                System.out.println("received 2");
                System.out.println(objects);
            });

            socket.connect();
            socket.send("packetevent", "test1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
