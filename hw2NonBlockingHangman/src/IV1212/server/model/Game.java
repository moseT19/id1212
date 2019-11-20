package IV1212.server.model;

import java.util.*;

public class Game {
    private WordSelector wordSelector;
    private String chosenWord;
    private ArrayList<Character> chosenWordChars = new ArrayList<>();
    private ArrayList<Character> hiddenwordChars = new ArrayList<>();
    private int score;
    private int remainingAttempts;

    public Game() {
        this.score = 0;
        this.wordSelector = new WordSelector();
    }

    public String startRound() {

        chosenWord = chooseWord();
        chosenWordChars.clear();
        hiddenwordChars.clear();
        System.out.println(chosenWord);
        remainingAttempts = chosenWord.length();
        for(int i = 0;i<chosenWord.length();i++) {
            chosenWordChars.add(chosenWord.charAt(i));
            hiddenwordChars.add('_');
        }
        return buildMessage();
    }

    private String chooseWord() {
        return wordSelector.chooseRandomWord().toUpperCase();
    }

    public String validateGuess(String guess){
        if (guess.length() == 1) {
            validateLetter(guess);
        } else {
            validateWord(guess);
        }
        return buildMessage();
    }

    private String buildHiddenWord(ArrayList hiddebword){
        String hw = "";
        for(int i = 0; i < hiddebword.size(); i++){
            hw += hiddebword.get(i);
        }
        return hw;
    }

    private String buildMessage() {
        return buildHiddenWord(hiddenwordChars) + " " + remainingAttempts + " " + score;
    }

    private void validateWord(String word) {
        if (word.toUpperCase().equals(chosenWord)) {
            score++;
            for(int i = 0; i < chosenWordChars.size(); i++){
                hiddenwordChars.set(i, chosenWord.charAt(i));
            }
            chosenWord = null;
            return;
        }else{
            remainingAttempts--;
            return;
        }
    }

    private void validateLetter(String letter) {
        char guessedchar = letter.toUpperCase().charAt(0);

        if (!chosenWordChars.contains(guessedchar)) {
            if (remainingAttempts <= 1) {
                if (score > 0) score--;
                for(int i = 0; i < chosenWordChars.size(); i++){
                    hiddenwordChars.set(i, chosenWord.charAt(i));
                }
                chosenWord = null;
                return;
            }
            remainingAttempts--;
            return;
        }
        for (int i = 0; i < chosenWordChars.size(); i++) {
            if(chosenWordChars.get(i).equals(guessedchar)){
                hiddenwordChars.set(i, guessedchar);
            }
        }

        if (buildHiddenWord(hiddenwordChars).equals(chosenWord)){
            validateWord(buildHiddenWord(hiddenwordChars));
        }
    }

    public String getChosenWord() {
        return chosenWord;
    }
}
