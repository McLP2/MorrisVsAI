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
    private boolean whiteCanJump = true;
    private boolean blackCanJump = true;
    private GameState gameState = GameState.OPENING;
    private int placedCounter = 0;
    private float activeX;
    private float activeY;
    private boolean saveMode = false;

    private GameBoard board;
    private AI ai;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MorrisKeyListener());
        batch = new SpriteBatch();
        whiteStone = new Texture("whiteStone.png");
        blackStone = new Texture("blackStone.png");
        font = new BitmapFont();
        ai = new AI();

        stones = new Stone[18];
        board = new GameBoard(stones);
        createStones();
    }

    private void createStones() {
        for (int i = 0; i < stones.length; i++)
            stones[i] = i % 2 == 0 ? new Stone(WHITE, whiteStone) : new Stone(BLACK, blackStone);
    }

    @Override
    public void render() {
        clear();
        board.draw();
        drawStones();
        if (saveMode) {
            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                saveMode = false;
            }
            return;
        }
        switch (gameState) {
            case OPENING:
                place();
                checkStateChange();
                break;
            case NORMAL:
                play();
                checkStateChange();
                break;
            case WIN:
                clear();
                board.draw();
                drawStones();
                write("Player " + activePlayer.toString() + " has won!");
                break;
        }

        // TODO: AI selection
        ai.drawSelection(GameBoard.size + 2 * GameBoard.margin, GameBoard.height - GameBoard.margin);
        // TODO: TreeHunter AI
        // TODO: NeuralPower AI
    }

    private void place() {
        if (newMills > 0) {
            write("Player " + activePlayer.toString() + " can remove a stone!");
            pickStone();
        } else {
            write("Player " + activePlayer.toString() + " can place a stone!");
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                int[] lastMills = board.getMills(activePlayer);

                Stone stone = stones[placedCounter];
                int x = Gdx.input.getX();
                int y = Gdx.graphics.getHeight() - Gdx.input.getY();
                stone.setPosition(x, y);
                boolean canJump;
                if (activePlayer == WHITE) {
                    canJump = whiteCanJump;
                } else {
                    canJump = blackCanJump;
                }
                int r = board.getNearestRing(stone, canJump);
                int p = board.getNearestRingPosition(stone, canJump);
                int dx = x - GameBoard.getGridCoordinatesX(r, p);
                int dy = y - GameBoard.getGridCoordinatesY(r, p);
                int rd = stone.getRadius();
                if (dx * dx + dy * dy < rd * rd) {
                    stone.setRing(r);
                    stone.setRingPosition(p);
                    stone.setActive(true);
                    placedCounter++;

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
        }
    }

    private void play() {
        if (newMills > 0) {
            pickStone();
            write("Player " + activePlayer.toString() + " can remove a stone!");
        } else {
            write("Player " + activePlayer.toString() + " can move!");
            findActive();
            moveActive();
        }
    }

    private void checkStateChange() {
        switch (gameState) {
            case OPENING:
                if (placedCounter == 18) {
                    gameState = GameState.NORMAL;
                    whiteCanJump = false;
                    blackCanJump = false;
                    checkStateChange();
                }
                break;
            case NORMAL:
                checkJumping();
                if (countMoves() == 0 && newMills == 0) {
                    switchPlayer();
                    gameState = GameState.WIN;
                }
                if (getBlackAmount() <= 2 || getWhiteAmount() <= 2) {
                    gameState = GameState.WIN;
                    if (newMills == 0) {
                        switchPlayer();
                    }
                    checkStateChange();
                }
                break;
        }
    }

    private int countMoves() {
        int counter = 0;
        for (Stone stone : stones) {
            //check every position for active, active player stones n add em up.
            if (stone.isActive() && stone.getStoneColor() == activePlayer) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 8; j++) {
                        boolean canJump;
                        if (activePlayer == WHITE) {
                            canJump = whiteCanJump;
                        } else {
                            canJump = blackCanJump;
                        }
                        if (board.isPositionLegal(stone, i, j, canJump)) {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }

    private void checkJumping() {
        int whiteCount = getWhiteAmount();
        int blackCount = getBlackAmount();
        if (whiteCount <= 3) {
            whiteCanJump = true;
        }
        if (blackCount <= 3) {
            blackCanJump = true;
        }
    }

    private int getWhiteAmount() {
        int whiteCount = 0;
        for (Stone stone : stones) {
            if (stone.isActive()) {
                if (stone.getStoneColor() == WHITE) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    private int getBlackAmount() {
        int blackCount = 0;
        for (Stone stone : stones) {
            if (stone.isActive()) {
                if (stone.getStoneColor() == BLACK) {
                    blackCount++;
                }
            }
        }
        return blackCount;
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
                        activeX = activeStone.getX();
                        activeY = activeStone.getY();
                    }
                }
            }
        } else if (activeStone != null) {
            // if near origin, reset
            float dx = activeX - activeStone.getX();
            float dy = activeY - activeStone.getY();
            float rd = activeStone.getRadius();
            if (dx * dx + dy * dy < rd * rd) {
                activeStone.setRing(activeStone.getRing());
                activeStone.setRingPosition(activeStone.getRingPosition());
                activeStone = null;
                return;
            }
            int[] lastMills = board.getMills(activePlayer);

            // if no position, reset
            boolean canJump;
            if (activePlayer == WHITE) {
                canJump = whiteCanJump;
            } else {
                canJump = blackCanJump;
            }
            int r = board.getNearestRing(activeStone, canJump);
            int p = board.getNearestRingPosition(activeStone, canJump);
            if (r == -1 || p == -1) {
                activeStone.setRing(activeStone.getRing());
                activeStone.setRingPosition(activeStone.getRingPosition());
                activeStone = null;
                return;
            }
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
                        (s_x - m_x) * (s_x - m_x) + (s_y - m_y) * (s_y - m_y) < (r * r) &&
                        !(board.stoneInMill(stone) && nonMillsExist())) {
                    stone.setActive(false);
                    newMills--;
                    saveMode = true;
                }
            }
            if (newMills == 0) {
                switchPlayer();
            }
        }
    }

    private boolean nonMillsExist() {
        for (Stone stone : stones) {
            if (stone.isActive() && stone.getStoneColor() != activePlayer && !board.stoneInMill(stone)) {
                return true;
            }
        }
        return false;
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
