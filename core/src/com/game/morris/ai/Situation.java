package com.game.morris.ai;


import com.game.morris.Morris;
import com.game.morris.enums.MorrisColor;

import java.util.ArrayList;

public class Situation {
    private ArrayList<Situation> next;
    private int closedBranches = 0;
    private Situation fastestWin;
    private MorrisColor[][] currentSituation;
    private MorrisColor player;
    private MorrisColor winner = MorrisColor.NONE;
    private boolean pick;
    private boolean jump;

    public Situation(MorrisColor[][] board, MorrisColor currentPlayer, boolean mustPick, boolean canJump) {
        next = new ArrayList<>();
        currentSituation = board;
        player = currentPlayer;
        pick = mustPick;
        jump = canJump;
    }

    public MorrisColor getPlayer() {
        return player;
    }

    public MorrisColor[][] getCurrentSituation() {
        return currentSituation;
    }

    public boolean isPick() {
        return pick;
    }

    public boolean isJump() {
        return jump;
    }

    private MorrisColor getWinner() {
        return winner;
    }

    public void setWinner(MorrisColor winner) {
        this.winner = winner;
    }

    private void findNewPath(Morris game) {
        MorrisColor[][][] possibleMoves = game.board.nextPossibleMoves(player, pick, jump);
        MorrisColor[][] move = possibleMoves[0];
        Situation newSituation = game.simulate(this, move);
        next.add(newSituation);
        if (newSituation.getWinner() == MorrisColor.NONE) {
            newSituation.findNewPath(game);
        }
    }

    void fill(int numberOfBranches, Morris game) {
        while (closedBranches < numberOfBranches) {
            findNewPath(game);
        }
    }

    MorrisColor[][] getBest() {
        // TODO: recursive breadth-first-search for first win
        return currentSituation;
    }
}
