package src.sockets.id1212.server.model;

import java.io.*;
import java.util.*;


public class HMGame implements Serializable{


    private String correctWord;
    private ArrayList<Character> correctLetters;
    private ArrayList<Character> guessedLetters;
    private ArrayList<Character> wrongGuesses;
    private int remainingTries;
    private int currentScore;
    private final transient Word genWord = new Word();
    private boolean victory;

    /*public HMGame(){


        this.correctWord = genWord.getRandWord();
        this.victory = false;
        correctLetters = new ArrayList<>();
        guessedLetters = new ArrayList<>();
        wrongGuesses = new ArrayList<>();
        for(int i = 0;i<correctWord.length();i++) {
            correctLetters.add(correctWord.charAt(i));
            guessedLetters.add('_');
        }


        this.remainingTries = 11;
        this.currentScore = 0;
    }*/

    /**
     * Constructor for a game.
     * @param word the hidden word for the game
     * @param score the clients score transferred to a new game instance.
     */
    public HMGame(String word, int score){


        this.correctWord = word;
        this.victory = false;
        correctLetters = new ArrayList<>();
        guessedLetters = new ArrayList<>();
        wrongGuesses = new ArrayList<>();
        for(int i = 0;i<correctWord.length();i++) {
            correctLetters.add(correctWord.charAt(i));
            guessedLetters.add('_');
        }

        System.out.println(correctWord);
        this.remainingTries = 11;
        this.currentScore = score;
    }

    /**
     * checks if the game is won
     * @return returns true if won, false if not yet won.
     */
    private boolean gameWon(){
        boolean status = false;
        if(guessedLetters.equals(correctLetters)){
            status = true;
            currentScore++;
        }

        return status;
    }

    /**
     * checks if a clients guess of a character is correct, depending on result updates information regarding the game.
     * @param c the guessed character.
     */
    public void guessChar(Character c){
        if(!victory) {
            if(!wrongGuesses.contains(c)) {
                boolean correct = false;
                for (int i = 0; i < correctWord.length(); i++) {

                    if (correctLetters.get(i).equals(c)) {
                        this.guessedLetters.set(i, c);
                        correct = true;
                    }
                }
                if (gameWon())
                    victory = true;
                if (!correct) {
                    remainingTries--;
                    wrongGuesses.add(c);
                    if(remainingTries == 0)
                        currentScore--;
                }
            }
        }

    }

    /**
     * checks if a guessed word is the same as the hidden word, depending on result updates information regarding the game.
     * @param guessedWord the word guessed by the client.
     */
    public void guessWord(String guessedWord) {
        if(!victory) {
            boolean correct = false;
            if(correctWord.equals(guessedWord)) {
                correct = true;
                victory = true;
                currentScore++;
            }
            if (!correct) {
                remainingTries--;
                if(remainingTries == 0)
                    currentScore--;
            }
        }
    }

    public int getCurrentScore(){
        return currentScore;
    }


    /**
     * prints the current state of this game.
     * @return returns a string containing information needed to present the current state of the game.
     */
    @Override
    public String toString(){
        if(!victory) {
            StringBuilder s = new StringBuilder();


            for (Character c : guessedLetters) {
                s.append(c);
            }

            if (!wrongGuesses.isEmpty()) {
                s.append(" ");
                for (Character c : wrongGuesses) {
                    s.append(c);
                }
            }
            else
                s.append(" 0");

            s.append(" " + remainingTries);
            s.append(" " + currentScore);
            System.out.println(correctWord);
            return s.toString();
        }
        else{
            String s ="";
            s+="victory ";
            s+=currentScore;

            return s;
        }


    }


}
