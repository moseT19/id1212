package src.sockets.id1212.client.net;


import java.io.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

import src.sockets.id1212.common.Data;
import src.sockets.id1212.common.DataType;


public class ServerConnection {

    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private  BufferedReader fromServer;
    private  ObjectOutputStream toServer;
    private volatile boolean connected;


    /**
     * Connect is a function for establishing a connection with the server.
     * @param host the ip address of desired server.
     * @param port the port of said server.
     * @param gameHandler a handler that handles information from server and makes it appropriate for the client interface.
     * @throws IOException
     */
    public void connect(String host, int port, OutputHandler gameHandler)throws IOException {

        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new ObjectOutputStream(socket.getOutputStream());
        //fromServer = new ObjectInputStream(socket.getInputStream());
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(gameHandler)).start();
    }



    /**
     * a function for signalling the server that the client wishes to end the connection.
     * @throws IOException
     */
    public void disconnect() throws IOException {
        socket.close();
        socket = null;
        connected = false;
    }



    public void makeChoice(String choice){
        sendData(DataType.CHOICE.toString(), choice);
    }

    public void setUsername(String username) {

        sendData(DataType.USER.toString(), username);
    }

    /**
     * The function creates a Data object from the parameters, this object is then sent to the server for processing.
     * @param parts contains two strings, one for the datatype (choice or user) and the second is the actual choice or username.
     */

    private void sendData(String... parts){
        StringJoiner joiner = new StringJoiner("-");
        for (String p: parts) {
            joiner.add(p);
        }
        Data data = new Data(joiner.toString());
        try{
            toServer.writeObject(data);
            toServer.flush();
            toServer.reset();
        }catch (IOException ioe){
            throw new UncheckedIOException(ioe);
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

                    outputHandler.handleMsg(fromServer.readLine());

                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMsg("Lost connection.");
                }
            }
        }

    }
}
