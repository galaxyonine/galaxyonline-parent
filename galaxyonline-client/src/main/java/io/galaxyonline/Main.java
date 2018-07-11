package io.galaxyonline;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Main {
    public static void main(String[] args) {
        try {
            Socket socket = IO.socket("http://localhost:8000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("client connected");
                    socket.emit("foo", "hi");
                    socket.disconnect();
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {


                @Override
                public void call(Object... args) {
                    System.out.println("client disconnected");
                }

            });
            socket.connect();
            socket.send("test");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
