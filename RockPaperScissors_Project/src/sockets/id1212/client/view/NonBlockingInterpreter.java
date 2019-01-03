package src.sockets.id1212.client.view;
import src.sockets.id1212.client.controller.Controller;

import java.util.ArrayList;
import java.util.Scanner;

import src.sockets.id1212.client.net.OutputHandler;



public class NonBlockingInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private boolean receivingCmds = false;
    private Controller contr;
    private final ThreadSafePrint outMsg = new ThreadSafePrint();
    private ArrayList<String> incLine = new ArrayList<String>();

    private ArrayList<String> args;

    /**
     * Initiates a thread for the client and initiates the controller.
     */
    public void start(){
        if(receivingCmds)
            return;
        receivingCmds = true;
        contr = new Controller();
        args = new ArrayList<>();
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
                String readLine = console.nextLine();

                parseLine(readLine);
                Command cmd = getCommand();

                switch (cmd){
                    case QUIT:
                        receivingCmds = false;
                        contr.disconnect();
                        break;
                    case CONNECT:
                        contr.connect(args.get(1), Integer.parseInt(args.get(2)), new ConsoleOutput());
                        break;
                    case CHOICE:
                        outMsg.println("you choose " + args.get(1));
                        contr.makeChoice(args.get(1));
                        break;
                    case USER:
                        contr.setUsername(args.get(1));
                        outMsg.println("trying to set username to" + args.get(1));
                        break;
                    case INVALID_CHOICE:
                        outMsg.println("Invalid choice of battle champion. You have to choose ROCK / PAPER / SCISSORS");
                        break;
                    default:
                        outMsg.println(helpMsg());
                        break;
                }
            }catch (Exception e){
                outMsg.println("Something went wrong");
            }
        }
    }


    private void parseLine(String input){

        args.clear();

        for (String substring: input.trim().split(" ")) {

            args.add(substring);
        }

    }

    private Command getCommand() {

        if(args.isEmpty())
            return null;


        String cmd = args.get(0);
        switch (cmd){
            case "CONNECT":
                return Command.CONNECT;

            case "QUIT":
                return Command.QUIT;

            case "USER":
                return Command.USER;

            case "CHOICE":
                if(!args.get(1).toUpperCase().equals("ROCK") && !args.get(1).toUpperCase().equals("PAPER") && !args.get(1).toUpperCase().equals("SCISSORS"))
                    return Command.INVALID_CHOICE;
                else
                    return Command.CHOICE;
            case "HELP":
                return Command.HELP;

            default:
                return Command.NO_COMMAND;

        }
    }

    private String helpMsg() {

        String s = "";
        s+= "To play Rock Paper Scissor write: CONNECT host(ip-address) portnumber\n";
        s+= "To set a username write USER <username>";
        s+= "If you're connected, write CHOICE rock/paper/scissor to make your choice in the game. \n";
        s+= "To quit write: QUIT";

        return s;
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

    }
}
