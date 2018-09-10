package com.game.ai.morris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class AI {
    private ShapeRenderer renderer = new ShapeRenderer();
    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    void drawSelection(float x, float y) {
        Gdx.gl.glLineWidth(2);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.line(x, y, x + 100, y);
        renderer.end();
        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, "AI selection", x, y);
        batch.end();
    }
}
