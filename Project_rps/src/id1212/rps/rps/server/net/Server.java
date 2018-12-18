package id1212.rps.rps.server.net;

import id1212.rps.rps.server.controller.ServerController;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT = 1800000;
    private final ServerController servContr = new ServerController();
    private final List<ClientHandler> clients = new ArrayList<>();
    private int portNumber = 8080;

    public static void main(String[] args) {
        System.out.println("server started");
        Server server = new Server();
        server.parseArguments(args);
        server.serve();
    }

    void updatePlayerStates(){

    }

    private void serve() {

        try{
            ServerSocket lSocket = new ServerSocket(portNumber);
            while(true){
                Socket clientSocket = lSocket.accept();
                startHandler(clientSocket);

            }
        }catch (IOException e){
            System.err.println("Server failed. ");
        }

    }

    private void startHandler(Socket clientSocket) throws SocketException{
        System.out.println("a new client connected");
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT);
        ClientHandler handler = new ClientHandler(this, clientSocket, servContr);
        synchronized (clients){
            clients.add(handler);
        }
        Thread handlerThread = new Thread();
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();

    }

    private void parseArguments(String[] arguments){
        if(arguments.length > 0){
            try{
                this.portNumber = Integer.parseInt(arguments[1]);
            }catch (NumberFormatException e){
                System.err.println("Invalid port number, will use default 8080. ");
            }

        }
    }

    void removeHandler(ClientHandler clientHandler) {
        synchronized (clients) {
            clients.remove(clientHandler);
        }
    }

    public void printShit(String s){
        System.out.println(s);
    }
}
