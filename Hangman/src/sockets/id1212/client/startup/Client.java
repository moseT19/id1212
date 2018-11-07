package sockets.id1212.client.startup;

import sockets.id1212.client.view.NonBlockingInterpreter;

public class Client {

    public static void main(String[] args) {
        System.out.println("client side");
        new NonBlockingInterpreter().start();
    }
}
