package src.sockets.id1212.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import src.sockets.id1212.server.controller.ServerController;
import src.sockets.id1212.server.model.PlayersTable;

public class Server {

    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final ServerController contr = new ServerController();
    private final List<ClientHandler> clients = new ArrayList<>();
    private int portNo = 8080;

    /**
     * Initiates our server to wait for connecting clients.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Server side");
        Server server = new Server();
        server.parseArguments(args);
        server.serve();

    }

    /**
     * the server creates a listening sockets with a specific port number that awaits connections from clients
     */
    private void serve() {
        try {
            ServerSocket lSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = lSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
    }

    /**
     * starts a clienthandler for a connecting client and creates a thread for created handler. also starts the thread.
     * @param clientSocket
     * @throws SocketException
     */
    private void startHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler handler = new ClientHandler(this, clientSocket, contr);
        synchronized (clients) {
            clients.add(handler);
            System.out.println("*New connection*");
        }
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

    private void parseArguments(String[] arguments) {
        if (arguments.length > 0) {
            try {
                portNo = Integer.parseInt(arguments[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default.");
            }
        }
    }

    public void broadcastStates() {

        synchronized (clients){
            clients.forEach(client -> client.updateGameState());
        }
    }


}
