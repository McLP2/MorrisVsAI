package com.game.ai.morris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

class AI {
    private ShapeRenderer renderer = new ShapeRenderer();
    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();

    private TextButton button;
    private Stage stage;

    public AI(InputMultiplexer multiplexer) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("button.pack"));
        stage = new Stage();
        multiplexer.addProcessor(stage);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        Skin skin = new Skin(atlas);
        style.checked = skin.getDrawable("down");
        style.up = skin.getDrawable("up");
        style.down = skin.getDrawable("down");
        style.over = skin.getDrawable("hover");
        style.font = font;
        button = new TextButton("Button 1", style);
        button.setX(50);
        button.setY(100);
        stage.addActor(button);
    }

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
        stage.draw();
    }
}
