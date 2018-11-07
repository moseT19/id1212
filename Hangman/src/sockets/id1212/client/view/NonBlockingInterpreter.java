package sockets.id1212.client.view;
import sockets.id1212.client.controller.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import sockets.id1212.client.view.ThreadSafePrint;
import sockets.id1212.client.net.OutputHandler;
import sockets.id1212.client.view.Command;
import sockets.id1212.common.Guess;
import sockets.id1212.server.model.HMGame;

import javax.xml.bind.SchemaOutputResolver;

public class NonBlockingInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;
    private Controller contr;
    private final ThreadSafePrint outMsg = new ThreadSafePrint();
    private ArrayList<String> incLine = new ArrayList<String>();
    private Guess g;

    public void start(){
        if(receivingCmds)
            return;
        receivingCmds = true;
        contr = new Controller();
        new Thread(this).start();
    }
    
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

    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            outMsg.println((String) msg);
            outMsg.print(PROMPT);
        }

        @Override
        public void showGameState(ArrayList<String> al) {
            /*ArrayList<String> translate = new ArrayList<>();
            for(String substring : o.split(" ")){
                translate.add(substring);
            }
            if(translate.size() > 2 && Integer.parseInt(translate.get(2)) <= 0){
                outMsg.println("You've been hanged boi! ");
                outMsg.println("If you would like to start a new game write 'START'");
            }
            else if(translate.get(0).equals("victory")){
                outMsg.println("You won the game!");
                outMsg.println("Current score: "+translate.get(1));
                outMsg.println("If you want to play another game write START");
            }
            else {
                outMsg.print("Your word:");
                for (int i = 0; i < translate.get(0).length(); i++) {
                    outMsg.print(translate.get(0).charAt(i) + " ");
                }

                if (!translate.get(1).equals("0")) {
                    outMsg.println("");
                    outMsg.print("Wrong guesses:");
                    for (int i = 0; i < translate.get(1).length(); i++) {
                        outMsg.print(translate.get(1).charAt(i) + " ");
                    }
                }
                outMsg.println("");
                outMsg.println("Remaining tries: " + translate.get(2));
                outMsg.println("");
                outMsg.println("Current score: " + translate.get(3));
                //outMsg.println(o);
                //outMsg.println(o.toString());
                //outMsg.print(PROMPT);
                */

                for(String s : al){
                    outMsg.println(s);
                }
        }


    }
}
