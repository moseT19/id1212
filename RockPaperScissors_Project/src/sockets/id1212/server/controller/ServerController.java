package src.sockets.id1212.server.controller;

import src.sockets.id1212.server.model.GameManager;
import src.sockets.id1212.server.model.Player;
import src.sockets.id1212.server.model.PlayersTable;

import java.util.ArrayList;

public class ServerController {

    PlayersTable players = new PlayersTable();
    GameManager gameManager = new GameManager();


    public void addPlayerToPeerPool(String username){
        players.addPlayerToPeerPool(username);
    }

    public ArrayList<String> showPlayers(){
        return players.showPlayers();
    }

    public void setPlayerChoice(Long threadId, String choice){
        players.setPlayerChoice(threadId, choice);
    }

    public boolean endRound(){
        boolean state = gameManager.endRound(players);
        return state;
    }

    public Player getPlayer(Long threadId){
        return players.getCurrentPlayer(threadId);
    }

    public void resetAllPlayers() {
        players.resetPlayers();
    }
}
