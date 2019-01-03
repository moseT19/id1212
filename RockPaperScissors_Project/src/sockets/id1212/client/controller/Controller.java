package src.sockets.id1212.client.controller;

import src.sockets.id1212.client.net.OutputHandler;
import src.sockets.id1212.client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {

    private final ServerConnection serverCon = new ServerConnection();

    /**
     * sends the connect information input by client to the serverconnection class
     * @param host the ip address of desired server
     * @param port the portnumber of server
     * @param outputHandler the outputhandler for handling the inferface output.
     */
    public void connect(String host, int port, OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            try {
                serverCon.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port));
    }


    public void makeChoice(String choice){

        CompletableFuture.runAsync(() -> {
            try {
                serverCon.makeChoice(choice);
            } catch (UncheckedIOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setUsername(String s) {

        CompletableFuture.runAsync(()->serverCon.setUsername(s));
    }


    public void disconnect() throws IOException {
        serverCon.disconnect();
    }


}
