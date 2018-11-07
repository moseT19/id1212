package sockets.id1212.client.net;


import java.io.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import sockets.id1212.common.Guess;


public class ServerConnection {

    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private  BufferedReader fromServer;
    private  ObjectOutputStream toServer;
    private volatile boolean connected;

    public void connect(String host, int port, OutputHandler gameHandler)throws IOException {

        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_MINUTE);
        connected = true;
        boolean autoFlush = true;
        toServer = new ObjectOutputStream(socket.getOutputStream());
        //fromServer = new ObjectInputStream(socket.getInputStream());
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(gameHandler)).start();
    }

    public void startNewGame(){
        try{
            toServer.writeObject(new Guess("START"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void disconnect() throws IOException {
        socket.close();
        socket = null;
        connected = false;
    }

    public void makeGuess(Guess g){
        try {
            toServer.writeObject(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class Listener implements Runnable {

        private OutputHandler outputHandler;

        private Listener(OutputHandler outputHandler){
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            try {
                for (;;) {

                    outputHandler.showGameState(printGameState(fromServer.readLine()));


                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMsg("Lost connection.");
                }
            }
        }

        private ArrayList<String> printGameState(String fromServer) {
            ArrayList<String> translate = new ArrayList<>();
            ArrayList<String> al = new ArrayList<>();
            for (String substring : fromServer.split(" ")) {
                translate.add(substring);
            }
            if (translate.size() > 2 && Integer.parseInt(translate.get(2)) <= 0) {
                al.add("You've been hanged boi! ");
                al.add("If you would like to start a new game write 'START'");
            } else if (translate.get(0).equals("victory")) {
                al.add("You won the game!");
                al.add("Current score: " + translate.get(1));
                al.add("If you want to play another game write START");
            } else {
                String temp = "Your word: ";
                for (int i = 0; i < translate.get(0).length(); i++) {
                    temp += translate.get(0).charAt(i) + " ";
                }
                al.add(temp);
                if (!translate.get(1).equals("0")) {
                    String temp2 = "Wrong guesses:";
                    for (int i = 0; i < translate.get(1).length(); i++) {
                        temp2 += translate.get(1).charAt(i) + " ";
                    }
                    al.add(temp2);
                }

                al.add("Remaining tries: " + translate.get(2));

                al.add("Current score: " + translate.get(3));


            }
            return al;
        }
    }
}
