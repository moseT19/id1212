package sockets.id1212.server.model;

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

    /*public static void main(String[] args) {
        HMGame game = new HMGame();
    }*/

    private void endGame(){

    }

    /*public int getRemaingTries(){
        return remainingTries;
    }*/

    private boolean gameWon(){
        boolean status = false;
        if(guessedLetters.equals(correctLetters)){
            status = true;
            currentScore++;
        }

        return status;
    }

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
