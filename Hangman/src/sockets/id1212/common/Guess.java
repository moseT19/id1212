package sockets.id1212.common;

import java.io.Serializable;

public class Guess implements Serializable{

    private Character guessedChar = null;
    private String guessedWord = null;

    public Guess(Character c){
        this.guessedChar = c;
    }

    public Guess(String s){
        this.guessedWord = s;
    }

    public Character getGuessedChar() {
        return guessedChar;
    }

    public String getGuessedWord() {
        return guessedWord;
    }
}
