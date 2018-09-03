package com.game.ai.morris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Stone extends Sprite {
    private StoneColor stoneColor;
    private boolean active;

    Stone(StoneColor color, Texture texture) {
        super(texture);
        this.stoneColor = color;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    public boolean isActive() {
        return active;
    }
}
