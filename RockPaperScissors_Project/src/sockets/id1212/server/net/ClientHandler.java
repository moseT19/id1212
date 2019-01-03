package src.sockets.id1212.server.net;
import src.sockets.id1212.common.Data;

import src.sockets.id1212.server.controller.ServerController;
import src.sockets.id1212.server.model.Player;


import java.io.*;

import java.net.*;
import java.util.*;



public class ClientHandler implements Runnable {

    private final Server server;
    private final Socket clientSocket;
    private  ObjectInputStream fromClient;
    private PrintWriter toClient;
    private int choiceIndicator = 0;
    private boolean connected;
    private final ServerController servContr;
    private String username = "grumpy old chicken";
    private Long threadId;


    /**
     * The constructor for our Clienthandler.
     * @param server Is our server responsible for "hosting" our clients.
     * @param clientSocket is the socket initiated on the client side.
     * @param controller is the controller which performs certain actions regarding the game.
     */
    ClientHandler(Server server, Socket clientSocket, ServerController controller){
        this.server = server;
        this.clientSocket = clientSocket;
        this.servContr = controller;
        connected = true;


    }


    /**
     * The Clienthandler run() is responsible for establishing the server side connection between client and server.
     * The first try clause establishes the connection with our streams.
     * The second try clause recieves information from client and acts accordingly and vice versa.
     */
    @Override
    public void run() {

        try {
            boolean autoFlush = true;
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
            threadId = Thread.currentThread().getId();

        }catch (IOException ioe){
             ioe.printStackTrace();
        }

        while(connected){
            try{



                Data incData = (Data)fromClient.readObject();
                switch (incData.getDt()) {
                    case USER:
                        username = incData.getDataBody();
                        servContr.addPlayerToPeerPool(username);
                        ArrayList<String> sp = servContr.showPlayers();

                        toClient.print("listan: ");
                        for (String s: sp) {
                            toClient.println(s);
                        }

                        break;
                    case CHOICE:
                        choiceIndicator = 1;
                        servContr.setPlayerChoice(threadId, incData.getDataBody());
                        break;
                }

                boolean state = servContr.endRound();
                if(!state){
                    if(choiceIndicator == 0)
                        toClient.println("Make your choice!");

                    toClient.println("Waiting for players...");
                }
                else{


                    server.broadcastStates();
                    servContr.resetAllPlayers();
                    choiceIndicator = 0;

                }


            }catch (Exception e){

            }
        }
    }

    public void updateGameState(){

            Player p = servContr.getPlayer(threadId);

            String state = "Round Played with " +p.getChoice()+"! \n Round score: " + p.getRoundScore() + " \t Total score: " + p.getTotalScore() + "\n Choose again to play again!";

            toClient.println(state);

    }
}
