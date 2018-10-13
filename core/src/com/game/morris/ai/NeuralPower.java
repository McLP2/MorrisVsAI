package com.game.morris.ai;

import com.game.morris.enums.MorrisColor;

import com.game.morris.ui.GameBoard;
import org.tensorflow.*;

import java.util.Arrays;

public class NeuralPower {
    private Session sess;
    private GameBoard gameBoard;

    public NeuralPower(GameBoard board) {
        SavedModelBundle smb = SavedModelBundle.load("", "serve");
        sess = smb.session();
        gameBoard = board;
    }

    public MorrisColor[][] nextMove(MorrisColor currentPlayer, boolean mustPick, boolean canJump) {
        // TODO: create Model, make it work, train it, ...  https://www.youtube.com/watch?v=j3MZ0brQ0QE
        MorrisColor[][][] possibleMoves = gameBoard.nextPossibleMoves(currentPlayer, mustPick, canJump);
        MorrisColor[][] currentSituation = gameBoard.toArray();

        double maxQuality = -1;
        MorrisColor[][] bestMove = currentSituation;

        // for all possible moves: feed current situation + new move
        System.out.println(Arrays.deepToString(currentSituation));
        for (MorrisColor[][] newMove : possibleMoves) {
            System.out.println(Arrays.deepToString(newMove));
            int result = 100; // Tensor result = sess.runner().run().get(0);
            if (result > maxQuality) {
                bestMove = newMove;
                maxQuality = result;
            }
        }
        // return best move
        return bestMove;
    }

}
