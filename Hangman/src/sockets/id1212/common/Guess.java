package src.sockets.id1212.common;

import java.io.Serializable;

/**
 * A class for the guesses in the hangman game, each guess is either a character (letter) or a string (word)
 * includes appropriate getters for information retrieval.
 */
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
