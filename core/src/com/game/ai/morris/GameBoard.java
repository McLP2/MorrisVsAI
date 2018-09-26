package com.game.ai.morris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.badlogic.gdx.math.MathUtils.round;
import static java.lang.Math.abs;

class GameBoard {
    static final int margin = 96;
    static final int height = Gdx.graphics.getHeight();
    static final int size = height - 2 * margin;

    private ShapeRenderer renderer = new ShapeRenderer();

    private Stone[] stones;

    GameBoard(Stone[] stones) {
        this.stones = stones;
    }

    void draw() {
        drawGrid();
        drawVertices();
    }

    private void drawVertices() {
        drawVerticesFill();
        drawVerticesBorder();
    }

    private void drawVerticesFill() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 1, 1, 1);
        drawCircles();
        renderer.end();
    }

    private void drawVerticesBorder() {
        Gdx.gl.glLineWidth(3);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 1);
        drawCircles();
        renderer.end();
    }

    private void drawCircles() {
        for (int r = 0; r < 3; r++) {
            for (int p = 0; p < 8; p++) {
                renderer.circle(getGridCoordinatesX(r, p), getGridCoordinatesY(r, p), 13, 20);
            }
        }
    }

    private void drawGrid() {
        Gdx.gl.glLineWidth(5);


        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(0, 0, 0, 1);

        renderer.rect(margin, margin, size, size);
        renderer.rect(margin + size / 6f, margin + size / 6f, size * 2f / 3f, size * 2 / 3f);
        renderer.rect(margin + size / 3f, margin + size / 3f, size / 3f, size / 3f);

        renderer.line(margin + size / 2f, margin + size, margin + size / 2f, margin + size / 3f * 2);
        renderer.line(margin + size / 2f, margin, margin + size / 2f, margin + size / 3f);
        renderer.line(margin, margin + size / 2f, margin + size / 3f, margin + size / 2f);
        renderer.line(margin + size / 3f * 2, margin + size / 2f, margin + size, margin + size / 2f);

        renderer.end();
    }

    static int getGridCoordinatesX(int ring, int position) {
        int x = -1;
        switch (position) {
            case 0:
                x = 0;
                break;
            case 1:
                x = 1;
                break;
            case 2:
                x = 2;
                break;
            case 3:
                x = 2;
                break;
            case 4:
                x = 2;
                break;
            case 5:
                x = 1;
                break;
            case 6:
                x = 0;
                break;
            case 7:
                x = 0;
                break;
        }
        return margin + x * (ring + 1) * size / 6 + size / 6 * (2 - ring);
    }

    static int getGridCoordinatesY(int ring, int position) {
        int y = -1;
        switch (position) {
            case 0:
                y = 2;
                break;
            case 1:
                y = 2;
                break;
            case 2:
                y = 2;
                break;
            case 3:
                y = 1;
                break;
            case 4:
                y = 0;
                break;
            case 5:
                y = 0;
                break;
            case 6:
                y = 0;
                break;
            case 7:
                y = 1;
                break;
        }
        return margin + y * (ring + 1) * size / 6 + size / 6 * (2 - ring);
    }

    public MorrisColor[][] toArray() {
        // ring, position
        MorrisColor[][] finalArray = new MorrisColor[3][8];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                finalArray[i][j] = MorrisColor.NONE;
            }
        }
        for (Stone stone : stones) {
            if (stone.isActive()) {
                finalArray[stone.getRing()][stone.getRingPosition()] = stone.getStoneColor();
            }
        }
        return finalArray;
    }

    int getNearestRing(Stone stone, boolean canJump) {
        int x = round(stone.getX());
        int y = round(stone.getY());
        int highscore = -1;
        int highvalue = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                int gridX = getGridCoordinatesX(i, j);
                int gridY = getGridCoordinatesY(i, j);
                int squared_dist = (gridX - x) * (gridX - x) + (gridY - y) * (gridY - y);
                if ((highscore == -1 || highscore > squared_dist) && isPositionLegal(stone, i, j, canJump)) {
                    highscore = squared_dist;
                    highvalue = i;
                }
            }
        }
        return highvalue;
    }

    int getNearestRingPosition(Stone stone, boolean canJump) {
        int x = round(stone.getX());
        int y = round(stone.getY());
        int highscore = -1;
        int highvalue = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                int gridX = getGridCoordinatesX(i, j);
                int gridY = getGridCoordinatesY(i, j);
                int squared_dist = (gridX - x) * (gridX - x) + (gridY - y) * (gridY - y);
                if ((highscore == -1 || highscore > squared_dist) && isPositionLegal(stone, i, j, canJump)) {
                    highscore = squared_dist;
                    highvalue = j;
                }
            }
        }
        return highvalue;
    }

    boolean isPositionLegal(Stone stone, int ring, int position, boolean canJump) {
        boolean posEmpty = this.toArray()[ring][position] == MorrisColor.NONE;
        if (canJump) {
            return posEmpty;
        } else {
            boolean sameRing = stone.getRing() == ring;
            boolean samePosition = stone.getRingPosition() == position;
            boolean nextToPos = abs(stone.getRingPosition() - position) % 6 == 1 && sameRing;
            boolean nextToRing = abs(stone.getRing() - ring) == 1 && samePosition && position % 2 == 1;
            return posEmpty && (nextToRing || nextToPos);
        }
    }

    // TODO: (write) test & fix
    boolean stoneInMill(Stone stone) {
        MorrisColor[][] arr = toArray();
        int p = stone.getRingPosition() + 8; // for modulo
        int r = stone.getRing();
        MorrisColor c = stone.getStoneColor();
        if (p % 2 == 1 &&
                (arr[0][p % 8] == c && arr[1][p % 8] == c && arr[2][p % 8] == c) ||
                (arr[r][(p - 1) % 8] == c && arr[r][p % 8] == c && arr[r][(p + 1) % 8] == c)) {
            return true;
        } else return (arr[r][(p) % 8] == c && arr[r][(p + 1) % 8] == c && arr[r][(p + 2) % 8] == c) ||
                (arr[r][(p) % 8] == c && arr[r][(p - 1) % 8] == c && arr[r][(p - 2) % 8] == c);
    }

    int[] getMills(MorrisColor activePlayer) {
        MorrisColor[][] arr = toArray();
        int i = 0;
        int[] result = new int[0];
        for (int r = 0; r < 3; r++) {
            for (int p = 0; p < 8; p += 2) {
                if (arr[r][p] == activePlayer &&
                        arr[r][p + 1] == activePlayer &&
                        arr[r][(p + 2) % 8] == activePlayer) {
                    i++;
                    int[] new_result = new int[i];
                    if (result.length > 0) {
                        System.arraycopy(result, 0, new_result, 0, i - 1);
                    }
                    new_result[i - 1] = r * 8 + p;
                    result = new_result;
                }
            }
        }
        for (int p = 1; p < 8; p += 2) {
            if (arr[0][p] == activePlayer &&
                    arr[1][p] == activePlayer &&
                    arr[2][p] == activePlayer) {
                i++;
                int[] new_result = new int[i];
                if (result.length > 0) {
                    System.arraycopy(result, 0, new_result, 0, i - 1);
                }
                new_result[i - 1] = p;
                result = new_result;
            }
        }
        return result;
    }

    public void dispose() {
        renderer.dispose();
    }
}

