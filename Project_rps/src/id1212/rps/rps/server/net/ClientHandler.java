package id1212.rps.rps.server.net;

import id1212.rps.rps.common.DataType;
import id1212.rps.rps.server.controller.ServerController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ClientHandler implements Runnable{

    private final Server server;
    private final Socket clientSocket;
    private final ServerController servContr;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private String username = null;
    private boolean connected = false;

    ClientHandler(Server server, Socket clientSocket, ServerController serverController){
        this.server = server;
        this.clientSocket = clientSocket;
        this.servContr = serverController;
        this.connected = true;
    }

    @Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        while (connected) {
            try {
                toClient.println("snilla rara");
                Data data = new Data(fromClient.readLine());
                switch (data.dt) {
                    case USERNAME:
                        username = data.dataBody;
                        servContr.addPlayerToPeerPool(username);
                        toClient.println("du är nu: "+username);
                        break;
                    case CHOICE:
                        servContr.setPlayerChoice(Thread.currentThread().getId(), data.dataBody);
                }
                toClient.println("hallå där i skogen");
            } catch (IOException ioe) {
                terminateClient();
                throw new UncheckedIOException(ioe);
            }
        }
    }

    void sendMsg(String msg) {
        StringJoiner joiner = new StringJoiner("-");
        joiner.add(msg);
        toClient.println(joiner.toString());
    }

    private void terminateClient(){
        try{
            clientSocket.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        connected = false;
        server.removeHandler(this);
    }

    private static class Data {
        private DataType dt;
        private String dataBody;

        private Data(String incomingFromClient){
            parse(incomingFromClient);
        }

        private void parse(String s){

                String[] parts = s.split("-");
                dt = DataType.valueOf(parts[0]);
                dataBody = parts[1];

        }
    }
}
