package sockets.id1212.client.net;

import sockets.id1212.server.model.HMGame;

import java.util.ArrayList;

public interface OutputHandler {


    void handleMsg(String a);

    void showGameState(ArrayList<String> gameStatePrint);
}
