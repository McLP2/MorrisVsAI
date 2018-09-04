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
    private int ringPosition;

    Stone(StoneColor color, Texture texture) {
        super(texture);
        float scale = Gdx.graphics.getHeight() / (float) 1536;
        this.setOriginCenter();
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

    public void setRingPosition(int ringPosition) {
        this.ringPosition = ringPosition;
        setPosition(
                GameBoard.getGridCoordinatesX(ring, ringPosition),
                GameBoard.getGridCoordinatesY(ring, ringPosition)
        );
    }

    public void setRing(int ring) {
        this.ring = ring;
        setPosition(
                GameBoard.getGridCoordinatesX(ring, ringPosition),
                GameBoard.getGridCoordinatesY(ring, ringPosition)
        );
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    @Override
    public float getX() {
        return super.getX() + getWidth() / 2;
    }

    @Override
    public float getY() {
        return super.getY() + getHeight() / 2;
    }

    public int getRingPosition() {
        return ringPosition;
    }

    public int getRing() {
        return ring;
    }
}
