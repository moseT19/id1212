package sockets.id1212.client.controller;

import sockets.id1212.client.net.OutputHandler;
import sockets.id1212.client.net.ServerConnection;
import sockets.id1212.common.Guess;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {

    private final ServerConnection serverCon = new ServerConnection();

    public void connect(String host, int port, OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            try {
                serverCon.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port));
    }

    public void startNewGame(){
        CompletableFuture.runAsync(() -> serverCon.startNewGame());
    }

    public void disconnect() throws IOException {
        serverCon.disconnect();
    }

    public void makeGuess(Guess g){

        CompletableFuture.runAsync(() -> serverCon.makeGuess(g));
    }
}
