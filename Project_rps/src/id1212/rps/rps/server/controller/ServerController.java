package id1212.rps.rps.server.controller;

import id1212.rps.rps.server.model.Player;


import java.util.ArrayList;
import java.util.HashMap;

public class ServerController {

    private HashMap<Long, Player> players;

    public ServerController(){
        players = new HashMap<>();
    }

    public void addPlayerToPeerPool(String username){
        Player newPlayer = new Player(username);
        Long id = Thread.currentThread().getId();
        players.put(id, newPlayer);
    }

    public ArrayList<String> showPlayers(){
        ArrayList<String> currentPlayers = new ArrayList<>();
        currentPlayers.add("Players currently in the game: ");
        players.forEach((i, p)-> {
            currentPlayers.add(p.getUsername());
        });
        return currentPlayers;
    }


    public void setPlayerChoice(Long threadID, String choice) {
        players.get(threadID).setChoice(choice);
    }
}
