package com.game.morris.ai;

import com.game.morris.Morris;
import com.game.morris.enums.MorrisColor;
import com.game.morris.ui.GameBoard;

import java.util.ArrayList;

public class TreeHunter {
    private Morris game;


    public TreeHunter(Morris currentGame) {
        game = currentGame;
    }

    public MorrisColor[][] nextMove(MorrisColor currentPlayer, boolean mustPick, boolean canJump, int intelligence) {
        MorrisColor[][] currentSituation = game.getCurrentSituation();
        Situation root = new Situation(currentSituation, currentPlayer, mustPick, canJump);
        root.fill(intelligence, game);
        return root.getBest();
    }
}
