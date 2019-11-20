package src.sockets.id1212.client.net;



import java.util.ArrayList;

public interface OutputHandler {


    void handleMsg(String a);

    void showGameState(ArrayList<String> gameStatePrint);
}
