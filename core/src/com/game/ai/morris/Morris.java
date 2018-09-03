package com.game.ai.morris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/*

 Position:

 0    1    2

 7         3

 6    5    4

*/

public class Morris extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture whiteStone;
    private Texture blackStone;
    private Stone[] stones;
    private Stone activeStone = null;

    private GameBoard board;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MorrisKeyListener());
        batch = new SpriteBatch();
        whiteStone = new Texture("whiteStone.png");
        blackStone = new Texture("blackStone.png");

        stones = new Stone[18];
        board = new GameBoard(stones);
        createStones();
    }

    private void createStones() {
        for (int i = 0; i < stones.length; i++) {
            if (i % 2 == 0) {
                stones[i] = new Stone(StoneColor.WHITE, whiteStone);
                stones[i].setOrigin((float) -64, (float) -64);
                stones[i].setRing(i / 6);
                stones[i].setPosition(i % 8);
            } else {
                stones[i] = new Stone(StoneColor.BLACK, blackStone);
                stones[i].setOrigin((float) -64, (float) -64);
                stones[i].setRing(i / 6);
                stones[i].setPosition(i % 8);
            }
        }
    }

    @Override
    public void render() {
        clear();
        board.draw();
        drawStones();
        findActive();
        moveActive();
        // TODO: game opening
        // TODO: winning condition
        // TODO: jumping condition

        // TODO: AI selection
        // TODO: TreeHunter AI
        // TODO: NeuralPower AI
    }

    private void moveActive() {
        if (activeStone != null) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            activeStone.setPosition(x, y);
        }
    }

    private void findActive() {
        // TODO: Active player
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (activeStone == null) {
                for (Stone stone : stones) {
                    int r = stone.getRadius();
                    float s_x = stone.getX();
                    float s_y = stone.getY();
                    float m_x = Gdx.input.getX();
                    float m_y = Gdx.graphics.getHeight() - Gdx.input.getY();
                    if ((s_x - m_x) * (s_x - m_x) + (s_y - m_y) * (s_y - m_y) < (r * r)) {
                        activeStone = stone;
                    }
                }
            }
        } else if (activeStone != null) {
            // TODO: if near origin, reset
            // TODO: add stone removal
            int r = board.getNearestRing(activeStone);
            int p = board.getNearestPosition(activeStone);
            activeStone.setRing(r);
            activeStone.setPosition(p);
            activeStone = null;
        }
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, (float) 0.8, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
                (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
    }

    private void drawStones() {
        batch.begin();
        for (Stone stone : stones) {
            stone.draw(batch);
        }
        if (activeStone != null) {
            activeStone.draw(batch); //active is on top
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        whiteStone.dispose();
        blackStone.dispose();
    }
}
