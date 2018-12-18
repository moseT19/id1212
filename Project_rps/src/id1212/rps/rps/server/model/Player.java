package id1212.rps.rps.server.model;

public class Player {

    private String username;
    private String choice;
    private int currentScore = 0;
    private int roundsPlayed = 0;

    public Player(String username){
        this.username = username;
        this.choice = null;
    }

    public String getUsername() {
        return username;
    }

    public String getChoice() {
        return choice;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }
}
