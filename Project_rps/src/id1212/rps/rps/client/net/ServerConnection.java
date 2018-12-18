package id1212.rps.rps.client.net;

import id1212.rps.rps.common.DataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.util.StringJoiner;

public class ServerConnection {

    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_MINUTE = 60000;
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private volatile boolean connected;

    public void connect(String host, int portNo, OutputHandler gameStateHandler) throws IOException{

        this.socket = new Socket();
        socket.connect(new InetSocketAddress(host, portNo), TIMEOUT_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(gameStateHandler)).start();
    }

    public void disconnect() throws IOException{
        socket.close();
        socket = null;
        connected = false;
    }

    public void confirmChoice(String choice) {
        sendData(DataType.CHOICE.toString(), choice);
    }

    public void setUsername(String username) {
        sendData(DataType.USERNAME.toString(), username);
    }

    private void sendData(String... parts){
        StringJoiner joiner = new StringJoiner("-");
        for (String p: parts) {
            joiner.add(p);
        }
        toServer.println(joiner.toString());

    }

    private class Listener implements Runnable{
        private final OutputHandler gameStateHandler;

        private Listener(OutputHandler gameStateHandler){
            this.gameStateHandler = gameStateHandler;
        }

        @Override
        public void run() {
            try {
                for (;;) {
                    gameStateHandler.handleMsg(extractMsgBody(fromServer.readLine()));
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    gameStateHandler.handleMsg("Lost connection.");
                }
            }

        }

        private String extractMsgBody(String entireMsg) {
            String[] msgParts = entireMsg.split("-");

            return msgParts[1];
        }


    }


}
