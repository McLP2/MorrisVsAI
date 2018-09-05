package com.game.ai.morris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.game.ai.morris.MorrisColor.BLACK;
import static com.game.ai.morris.MorrisColor.WHITE;


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
    private BitmapFont font;

    private Stone[] stones;
    private Stone activeStone = null;
    private MorrisColor activePlayer = WHITE;
    private int newMills;

    private GameBoard board;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MorrisKeyListener());
        batch = new SpriteBatch();
        whiteStone = new Texture("whiteStone.png");
        blackStone = new Texture("blackStone.png");
        font = new BitmapFont();

        stones = new Stone[18];
        board = new GameBoard(stones);
        createStones();
    }

    private void createStones() {
        for (int i = 0; i < stones.length; i++) {
            if (i % 2 == 0) {
                stones[i] = new Stone(WHITE, whiteStone);
                stones[i].setRing(i / 6);
                stones[i].setRingPosition(i % 8);
            } else {
                stones[i] = new Stone(BLACK, blackStone);
                stones[i].setRing(i / 6);
                stones[i].setRingPosition(i % 8);
            }
        }
    }

    @Override
    public void render() {
        clear();
        board.draw();
        drawStones();
        if (newMills > 0) {
            pickStone();
            write("Player " + activePlayer.toString() + " can remove a stone!");
        } else {
            write("Player " + activePlayer.toString() + " can move!");
            findActive();
            moveActive();
        }
        // TODO: game opening
        // TODO: winning condition
        // TODO: jumping condition

        // TODO: AI selection
        // TODO: TreeHunter AI
        // TODO: NeuralPower AI
    }

    private void write(String string) {
        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, string, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    private void moveActive() {
        if (activeStone != null) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            activeStone.setPosition(x, y);
        }
    }

    private void findActive() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (activeStone == null) {
                for (Stone stone : stones) {
                    int r = stone.getRadius();
                    float s_x = stone.getX();
                    float s_y = stone.getY();
                    float m_x = Gdx.input.getX();
                    float m_y = Gdx.graphics.getHeight() - Gdx.input.getY();
                    if (stone.isActive() &&
                            stone.getStoneColor() == activePlayer &&
                            (s_x - m_x) * (s_x - m_x) + (s_y - m_y) * (s_y - m_y) < (r * r)) {
                        activeStone = stone;
                    }
                }
            }
        } else if (activeStone != null) {
            // TODO: if near origin, reset
            int[] lastMills = board.getMills(activePlayer);

            int r = board.getNearestRing(activeStone);
            int p = board.getNearestRingPosition(activeStone);
            activeStone.setRing(r);
            activeStone.setRingPosition(p);
            activeStone = null;

            newMills = 0;
            for (int i : board.getMills(activePlayer)) {
                newMills++;
                for (int j : lastMills) {
                    if (i == j) newMills--;
                }
            }

            if (newMills == 0) {
                switchPlayer();
            }
        }
    }

    private void pickStone() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            for (Stone stone : stones) {
                int r = stone.getRadius();
                float s_x = stone.getX();
                float s_y = stone.getY();
                float m_x = Gdx.input.getX();
                float m_y = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (stone.isActive() && stone.getStoneColor() != activePlayer &&
                        (s_x - m_x) * (s_x - m_x) + (s_y - m_y) * (s_y - m_y) < (r * r)) {
                    stone.setActive(false);
                    newMills--;
                    System.out.println(newMills);
                }
            }
            if (newMills == 0) {
                switchPlayer();
            }
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

    private void switchPlayer() {
        activePlayer = activePlayer == WHITE ? BLACK : WHITE;
    }

    @Override
    public void dispose() {
        batch.dispose();
        whiteStone.dispose();
        blackStone.dispose();
        font.dispose();
    }
}
