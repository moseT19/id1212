package src.sockets.id1212.client.view;
import sockets.id1212.client.controller.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import src.sockets.id1212.client.view.ThreadSafePrint;
import src.sockets.id1212.client.net.OutputHandler;
import src.sockets.id1212.client.view.Command;
import src.sockets.id1212.common.Guess;
import src.sockets.id1212.server.model.HMGame;

import javax.xml.bind.SchemaOutputResolver;

public class NonBlockingInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;
    private Controller contr;
    private final ThreadSafePrint outMsg = new ThreadSafePrint();
    private ArrayList<String> incLine = new ArrayList<String>();
    private Guess g;

    /**
     * Initiates a thread for the client and initiates the controller.
     */
    public void start(){
        if(receivingCmds)
            return;
        receivingCmds = true;
        contr = new Controller();
        new Thread(this).start();
    }

    /**
     * The client thread waits for commands written by the client to performed desired action.
     * Depending on the commands we the client can connect to the server, quit the process or make guesses towards the game depending on status.
     */
    @Override
    public void run(){

        while(receivingCmds){
            incLine.clear();
            try {
                String s = console.nextLine();
                for(String substring: s.split(" ")){
                    incLine.add(substring);
                }

                switch (incLine.get(0).toUpperCase()){
                    case "QUIT":
                        receivingCmds = false;
                        contr.disconnect();
                        break;

                    case "CONNECT":
                        contr.connect(incLine.get(1), Integer.parseInt(incLine.get(2)), new ConsoleOutput());
                        break;

                    case "START":
                        contr.startNewGame();
                        break;

                    default:
                        if(incLine.get(0).length() == 1){
                            g = new Guess(incLine.get(0).charAt(0));
                        }
                        else {
                            g = new Guess(incLine.get(0));
                        }
                        contr.makeGuess(g);
                        break;
                }
            }catch (Exception e){
                outMsg.println("Something went wrong");
            }
        }
    }

    /**
     * handles the output recieved from the server to make it appropriate for the client interface.
     */
    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMsg.println((String) msg);
            outMsg.print(PROMPT);
        }

        @Override
        public void showGameState(ArrayList<String> al) {

                for(String s : al){
                    outMsg.println(s);
                }
        }


    }
}
