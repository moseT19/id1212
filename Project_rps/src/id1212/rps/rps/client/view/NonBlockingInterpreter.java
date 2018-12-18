package id1212.rps.rps.client.view;

import id1212.rps.rps.client.controller.ClientController;
import id1212.rps.rps.client.net.OutputHandler;
import id1212.rps.rps.server.net.ClientHandler;


import java.util.ArrayList;
import java.util.Scanner;

public class NonBlockingInterpreter implements Runnable {

    private final Scanner input = new Scanner(System.in);
    private boolean recCom = false;
    private ClientController clientContr;
    private ThreadSafePrint safePrint = new ThreadSafePrint();
    private ArrayList<String> args;

    public void start(){
        if(recCom)
            return;
        recCom = true;
        clientContr = new ClientController();
        args = new ArrayList<>();
        new Thread(this).start();
    }



    @Override
    public void run() {
        safePrint.println("Starting client");
        safePrint.println(helpMsg());
        while (recCom){
            try{
                String readLine = input.nextLine();
                safePrint.println(readLine);
                parseLine(readLine);
                Command cmd = getCommand();
                safePrint.println(cmd.toString());
                switch (cmd){
                    case CONNECT:
                        clientContr.connect(args.get(1), Integer.parseInt(args.get(2)), new CmdLineOutput());
                        break;
                    case QUIT:
                        recCom = false;
                        clientContr.disconnect();
                        break;
                    case USERNAME:
                        clientContr.setUsername(args.get(1));
                        System.out.println("fghjkl");
                        break;
                    case CHOICE:
                        safePrint.println("You choose " + args.get(1) + " to be your champion in battle...");
                        clientContr.confirmChoice(args.get(1));
                        break;
                    case HELP:
                        safePrint.println(helpMsg());
                        break;
                    case NO_COMMAND:
                        safePrint.println(helpMsg());
                        break;
                }
            }catch (Exception e){
                safePrint.println("Operation was not succesful. ");
            }
        }
    }

    private String helpMsg() {

        String s = "";
        s+= "To play Rock Paper Scissor write: CONNECT host(ip-address) portnumber\n";
        s+= "If you're connected, write CHOICE rock/paper/scissor to make your choice in the game. \n";
        s+= "To quit write: QUIT";

        return s;
    }

    private void parseLine(String input){

        args.clear();

        for (String substring: input.trim().split(" ")) {
            safePrint.println(substring);
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

            case "USERNAME":
                return Command.USERNAME;

            case "CHOICE":
                return Command.CHOICE;

            case "HELP":
                return Command.HELP;

            default:
                return Command.NO_COMMAND;

        }
    }

    private class CmdLineOutput implements OutputHandler{

        @Override
        public void handleMsg(String msg) {

            System.out.println("update kallas");
            System.out.println(msg);
            safePrint.println(msg);
            safePrint.println("");
        }
    }
}
