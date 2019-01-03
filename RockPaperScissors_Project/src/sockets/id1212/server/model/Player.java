package src.sockets.id1212.server.model;


import javax.annotation.Generated;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {


    private static final AtomicInteger count = new AtomicInteger(0);

    private final int ID;
    private String username;
    private String choice;
    private int roundScore = 0;
    private int totalScore = 0;
    private int roundsPlayed = 0;

    public Player(String username){
        this.username = username;
        this.choice = null;
        this.ID = count.incrementAndGet();
    }

    public String getUsername() {
        return username;
    }

    public String getChoice() {
        return choice;
    }

    public void resetChoice(){
        this.choice = null;
    }

    public int getID() {
        return ID;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore += roundScore;
    }

    public void resetRoundScore(){
        this.roundScore = 0;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setChoice(String choice) {
        this.choice = choice.toUpperCase().trim();
    }

    public void setTotalScore(int newScore) {
        this.totalScore += newScore;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }
}
