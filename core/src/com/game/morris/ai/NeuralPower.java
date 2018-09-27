package com.game.morris.ai;

import com.game.morris.Morris;
import com.game.morris.enums.MorrisColor;

import org.tensorflow.*;

import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NeuralPower {
    public static MorrisColor[][] nextMove(MorrisColor[][] currentSituation, MorrisColor[][][] possibleMoves) {
        // TODO: create Model, make it work, train it, ...  https://www.youtube.com/watch?v=j3MZ0brQ0QE
        SavedModelBundle smb = SavedModelBundle.load("", "serve");
        Session sess = smb.session();
        // for all possible moves: feed current situation + new move
        for (MorrisColor[][] newMove : possibleMoves) {
            System.out.println(Arrays.deepToString(newMove));
        }
        // save predictions and find best
        Tensor result = sess.runner().run().get(0);
        // return best move
        if (result.equals(Tensor.create(new long[]{0, 0}))) return null;
        return new MorrisColor[0][];
    }

}
