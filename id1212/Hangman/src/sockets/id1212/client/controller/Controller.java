package sockets.id1212.client.controller;

import sockets.id1212.client.net.OutputHandler;
import sockets.id1212.client.net.ServerConnection;
import sockets.id1212.common.Guess;
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

    /**
     * instructs the serverconnection instance to start a new game
     */
    public void startNewGame(){
        CompletableFuture.runAsync(() -> serverCon.startNewGame());
    }

    /**
     * instructs the server connection instance to disconnect fro the server and end the process
     * @throws IOException
     */
    public void disconnect() throws IOException {
        serverCon.disconnect();
    }

    /**
     * instructs the serverconnection instance to send a guess over to the server.
     * @param g the guess
     */
    public void makeGuess(Guess g){

        CompletableFuture.runAsync(() -> serverCon.makeGuess(g));
    }
}
