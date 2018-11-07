package sockets.id1212.server.controller;

import sockets.id1212.server.model.HMGame;
import sockets.id1212.server.model.Word;

public class Controller {

        private final transient Word wordGenerator;

        public Controller(){
            wordGenerator = new Word();
        }

        public HMGame createGame(Integer score){
            return new HMGame(wordGenerator.getRandWord(), score);
        }

        public void guessChar(HMGame game, Character c){
            game.guessChar(c);
        }

        public void guessWord(HMGame game, String guessedWord) {
                game.guessWord(guessedWord);

        }
}
