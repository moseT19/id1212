package id1212.rps.rps.client.startup;

import id1212.rps.rps.client.view.NonBlockingInterpreter;

public class Client {

    public static void main(String[] args) {
        new NonBlockingInterpreter().start();
    }
}
