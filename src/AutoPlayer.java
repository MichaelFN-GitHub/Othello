import engine.BitboardGameState;

import java.awt.*;

import static engine.BitboardGameState.BLACK;
import static engine.BitboardGameState.WHITE;

public class AutoPlayer {

    //This class can autoplay on an online Othello game.

    private final BitboardGameState game;

    private final PlayOnline playOnline = new PlayOnline();



    public AutoPlayer(BitboardGameState game) throws AWTException {
        this.game = game;
    }

    public void play(int ourColor) throws InterruptedException {
        Thread.sleep(1000);
        while(!game.isGameOver()){
            if(game.getPlayerToMove() == ourColor){
                makeAIMove();
            }else{
                makeMoveOfOnlineOpponent();
            }
        }
    }

    //Tell AI to make a move.
    private void makeAIMove() throws InterruptedException {
        int move = AI.findNextMove(game);
        if (move != -1) {
            game.makeMove(move);
            playOnline.updateBoard(move);
            playOnline.moveAndClickOnline(move, false);
        }
    }

    public void makeMoveOfOnlineOpponent() {
        var move = playOnline.getAndWaitForPlayerMove();
        if (game.isLegalMove(move)) {
            game.makeMove(move);
        }else{
            System.out.println("Not a legal move: " + move);
        }
    }

}
