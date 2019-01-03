package src.sockets.id1212.server.model;







import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class PlayersTable implements Serializable {

    private HashMap<Long, Player> players;

    public PlayersTable(){
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

    public boolean playersReady(){
        boolean allReady = true;

        for (Player p: players.values()) {
            if(p.getChoice() == null)
                allReady = false;
        }
       return allReady;
    }

    public boolean enoughPlayers(){
        boolean moreThanOne = false;
        if(this.players.size() > 1)
            moreThanOne = true;

        return moreThanOne;
    }

    public Player getCurrentPlayer(Long threadId){
        Player p = this.players.get(threadId);
        return p;
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }

    public void resetPlayers(){

        for (Player p: this.players.values()) {
            p.resetRoundScore();
            p.resetChoice();
        }
    }

    public void setPlayerChoice(Long threadID, String choice) {

        players.get(threadID).setChoice(choice);
    }


}