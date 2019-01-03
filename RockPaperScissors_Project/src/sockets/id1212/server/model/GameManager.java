package src.sockets.id1212.server.model;



public class GameManager {


    public  boolean endRound(PlayersTable players){

        boolean ended = false;
        if(players.playersReady() && players.enoughPlayers()){
            calculateRoundScore(players);
            ended = true;

        }

       return ended;
    }

    private void calculateRoundScore(PlayersTable players) {

        for(Player player1 : players.getPlayers()){
            String player1Choice = player1.getChoice();

            for (Player opponent: players.getPlayers()) {
                if(player1.getID() != opponent.getID()){
                    String opponentChoice = opponent.getChoice();

                    if(player1Choice.equals("ROCK")){
                        player1.setRoundScore(opponentChoice.equals("SCISSORS") ? 1 : 0);
                    }
                    if(player1Choice.equals("PAPER")){
                        player1.setRoundScore(opponentChoice.equals("ROCK") ? 1 : 0);
                    }
                    if(player1Choice.equals("SCISSORS")){
                        player1.setRoundScore(opponentChoice.equals("PAPER") ? 1 : 0);
                    }
                }

            }
            player1.setTotalScore(player1.getRoundScore());


        }



    }

}
