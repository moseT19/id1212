package src.sockets.id1212.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.StringJoiner;
import src.sockets.id1212.server.controller.Controller;
import src.sockets.id1212.server.model.HMGame;
import src.sockets.id1212.server.model.Word;

public class HMServer {

    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final Controller contr = new Controller();
    //private final List<ClientHandler> clients = new ArrayList<>();
    private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
    private int portNo = 8080;
    private Selector selector;
    private ServerSocketChannel listeningSocketChannel;


    /**
     * Initiates our server to wait for connecting clieants.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Server side");
        HMServer server = new HMServer();
        server.parseArguments(args);
        server.serve();

    }

    /**
     * the server creates a listening sockets with a specific port number that awaits connections from clients
     */
    private void serve() {
        try {
            initSelector();
            initListeningSocketChannel();
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        startHandler(key);
                    } else if (key.isReadable()) {
                        recieveFromClient(key);
                    } else if (key.isWritable()) {
                        sendToClient(key);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Server failure.");
        }
    }

    private void initSelector() throws IOException{
        selector = Selector.open();
    }

    private void initListeningSocketChannel(){

        try {
            listeningSocketChannel = ServerSocketChannel.open();
            listeningSocketChannel.configureBlocking(false);
            listeningSocketChannel.bind(new InetSocketAddress(portNo));
            listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        l
    }


    private void startHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        ClientHandler handler = new ClientHandler(this, clientChannel,contr);
        clientChannel.register(selector, SelectionKey.OP_WRITE, new Client(handler, 0));
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
    }

    private void recieveFromClient(SelectionKey key) throws IOException {

    }

    private class Client {
        private final ClientHandler handler;
        private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
        private int score;

        private Client(ClientHandler handler, int score) {
            this.handler = handler;
            this.score = score;
        }

        private void queueMsgToSend(ByteBuffer msg) {
            synchronized (messagesToSend) {
                messagesToSend.add(msg.duplicate());
            }
        }

        private void sendAll() throws IOException, MessageException {
            ByteBuffer msg = null;
            synchronized (messagesToSend) {
                while ((msg = messagesToSend.peek()) != null) {
                    handler.sendMsg(msg);
                    messagesToSend.remove();
                }
            }
        }
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
}
