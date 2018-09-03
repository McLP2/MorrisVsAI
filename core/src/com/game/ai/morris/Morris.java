package com.game.ai.morris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Morris extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture whiteStone;
    private Texture blackStone;
    private Stone[] stones;

    private ShapeRenderer renderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        whiteStone = new Texture("whiteStone.png");
        blackStone = new Texture("blackStone.png");
        stones = new Stone[18];
        createStones();
    }

    private void createStones() {
        for (int i = 0; i < stones.length; i++) {
            if (i % 2 == 0) {
                stones[i] = new Stone(StoneColor.WHITE, whiteStone);
            } else {
                stones[i] = new Stone(StoneColor.BLACK, blackStone);
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, (float) 0.8, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawMesh();
        drawStones();
    }

    private void drawStones() {
        for (Stone stone : stones) {
            stone.draw(batch);
        }
    }

    private void drawMesh() {
        Gdx.gl.glLineWidth(5);

        int margin = 50;
        int height = Gdx.graphics.getHeight();
        int size = height - 2 * margin;


        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(0, 0, 0, 1);

        renderer.rect(margin, margin, size, size);
        renderer.rect(margin + size / 6, margin + size / 6, size * 2 / 3, size * 2 / 3);
        renderer.rect(margin + size / 3, margin + size / 3, size / 3, size / 3);

        renderer.line(margin + size / 2, margin + size, margin + size / 2, margin + size / 3 * 2);
        renderer.line(margin + size / 2, margin, margin + size / 2, margin + size / 3);
        renderer.line(margin, margin + size / 2, margin + size / 3, margin + size / 2);
        renderer.line(margin + size / 3 * 2, margin + size / 2, margin + size, margin + size / 2);

        renderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        whiteStone.dispose();
        blackStone.dispose();
    }
}
