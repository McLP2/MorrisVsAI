package com.game.ai.morris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static com.badlogic.gdx.math.MathUtils.round;

class Stone extends Sprite {
    private StoneColor stoneColor;
    private boolean active;
    private int radius;
    private int ring;
    private int position;

    Stone(StoneColor color, Texture texture) {
        super(texture);
        float scale = Gdx.graphics.getHeight() / (float) 1536;
        this.setScale(scale);
        stoneColor = color;
        radius = round(scale * 64);
        active = true;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    private boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void draw(Batch batch) {
        if (isActive()) {
            super.draw(batch);
        }
    }

    public void setPosition(int position) {
        this.position = position;
        super.setX(GameBoard.getGridCoordinatesX(ring, position));
        super.setY(GameBoard.getGridCoordinatesY(ring, position));
    }

    public void setRing(int ring) {
        this.ring = ring;
        super.setX(GameBoard.getGridCoordinatesX(ring, position));
        super.setY(GameBoard.getGridCoordinatesY(ring, position));
    }

    public int getPosition() {
        return position;
    }

    public int getRing() {
        return ring;
    }
}
