package IV1212.client.view;

import IV1212.client.controller.Controller;
import IV1212.client.net.CommunicationListener;
import IV1212.common.Message;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GameShell implements Runnable {

    private static final String PROMT = ">> ";
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final Scanner console = new Scanner(System.in);
    private ConsoleOutput consoleOutput = new ConsoleOutput();
    private boolean running = false;
    private Controller controller;

    public void start(){
        if (running) return;
        running = true;

        controller = new Controller();
        controller.setViewObserver(consoleOutput);

        new Thread(this).start();
    }



    @Override
    public void run() {
        outMgr.println("Welcome to Hangman");
        outMgr.println("Connect to a server with 'connect <ip> <port>'\nQuit the game with 'quit'");
        outMgr.print(PROMT);

        while (running) {
            try{
                CommandParser parsedCommand = new CommandParser(console.nextLine());
                switch (parsedCommand.getCommand()){
                    case CONNECT:
                        controller.connect(
                                parsedCommand.getArgument(0),
                                Integer.parseInt(parsedCommand.getArgument(1)));
                        break;

                    case QUIT:
                        controller.disconnect();
                        running = false;
                        break;

                    case START:
                        controller.startNewRound();
                        break;

                    case GUESS:
                        controller.submitGuess(parsedCommand.getArgument(0));

                    case NO_COMMAND:
                        outMgr.print(PROMT);

                }
            } catch (IOException e) {
                e.printStackTrace();
                outMgr.print(PROMT);
            }
        }
    }

    private class ConsoleOutput implements CommunicationListener{
        @Override
        public void print(String msg) {
            outMgr.println(msg);
            outMgr.print(PROMT);
        }

        @Override
        public void sendMsg(Message message) {

            switch (message.getMessageType()){
                case START_RESPONSE:
                    outMgr.println("You started a new game!\nYou can guess a letter or the word\n" +
                            "You have to guess:"+ prettifyGameState(message.getBody()));
                    break;
                case GUESS_RESPONSE:
                    outMgr.println(prettifyGameState(message.getBody()));
                    break;
                case END_RESPONSE:
                    outMgr.println("\" The game is ended! Here is the result:" +
                            prettifyGameState(message.getBody()) + "\nStart a new game with 'start'");
                    break;

                default:
                    outMgr.println(message.getBody());

            }
        }

        private String prettifyGameState(String gameState){
            StringTokenizer body = new StringTokenizer(gameState);
            return body.nextToken().replace("", " ").trim() +
                    " - [remaining attempts: " + body.nextToken() +
                    "; score: " + body.nextToken();
        }
    }
}
