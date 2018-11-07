package sockets.id1212.server.controller;

import sockets.id1212.server.model.HMGame;
import sockets.id1212.server.model.Word;

public class Controller {

        private final transient Word wordGenerator;

        public Controller(){
            wordGenerator = new Word();
        }

    /**
     * generates a new game
     * @param score transfers the score onto the nexxt game
     * @return returns a new game with a new word.
     */
    public HMGame createGame(Integer score){
            return new HMGame(wordGenerator.getRandWord(), score);
        }

    /**
     * make a character guess.
     * @param game the game which the guess concerns
     * @param c the guessed character
     */
    public void guessChar(HMGame game, Character c){
            game.guessChar(c);
        }

    /**
     * make a word guess
     * @param game the game which the guess concerns
     * @param guessedWord the guessed word.
     */
        public void guessWord(HMGame game, String guessedWord) {
                game.guessWord(guessedWord);

        }
}
