package id1212.rps.rps.client.controller;


import id1212.rps.rps.client.net.OutputHandler;
import id1212.rps.rps.client.net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class ClientController {

    private final ServerConnection serverCon = new ServerConnection();

    public void connect(String host, int portNo, OutputHandler gameStateHandler){
        CompletableFuture.runAsync(()-> {
            try{
                serverCon.connect(host, portNo, gameStateHandler);
            }catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(()->gameStateHandler.handleMsg("Connected to "+ host + ":" + portNo));
    }

    public void disconnect() throws IOException{
        serverCon.disconnect();
    }

    public void confirmChoice(String choice){
        CompletableFuture.runAsync(()->serverCon.confirmChoice(choice));
    }

    public void setUsername(String s) {
        CompletableFuture.runAsync(()->serverCon.setUsername(s));
    }
}
