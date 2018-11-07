package sockets.id1212.server.net;
import sockets.id1212.common.Guess;
import sockets.id1212.server.model.HMGame;

import java.io.*;

import java.net.*;
import java.util.*;
import sockets.id1212.server.controller.Controller;




public class ClientHandler implements Runnable {

    private final HMServer server;
    private final Socket clientSocket;
    private  ObjectInputStream fromClient;
    private PrintWriter toClient;
    private  HMGame gameIteration;
    private boolean connected;
    private final Controller controller;
    private int currentScore;

    /**
     * The constructor for our Clienthandler.
     * @param server Is our server responsible for "hosting" our clients.
     * @param clientSocket is the socket initiated on the client side.
     * @param controller is the controller which performs certain actions regarding the game.
     */
    ClientHandler(HMServer server, Socket clientSocket, Controller controller){
        this.server = server;
        this.clientSocket = clientSocket;
        this.controller = controller;
        connected = true;
        this.currentScore = 0;
        this.gameIteration = this.controller.createGame(this.currentScore);

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
            toClient.println(gameIteration.toString());
            toClient.flush();
        }catch (IOException ioe){
             ioe.printStackTrace();
        }

        while(connected){
            try{


                Guess guess = (Guess)fromClient.readObject();
                if(guess.getGuessedChar() != null){
                        controller.guessChar(this.gameIteration, guess.getGuessedChar());
                }
                else if(guess.getGuessedWord().equals("START")){
                        gameIteration = controller.createGame(currentScore);
                }
                else{
                    controller.guessWord(this.gameIteration, guess.getGuessedWord());
                }

                toClient.println(gameIteration.toString());

            }catch (Exception e){

            }
        }

    }
}
