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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

class AI {
    private BitmapFont font = new BitmapFont(Gdx.files.internal("DejaVuSansLight.fnt"));
    private SpriteBatch batch = new SpriteBatch();

    private TextButton button_player;
    private TextButton button_tree;
    private TextButton button_neural;

    private Slider tree_complexity;

    private Stage stage;

    public AI(InputMultiplexer multiplexer) {
        TextureAtlas buttonTextureAtlas = new TextureAtlas(Gdx.files.internal("button.pack"));
        TextureAtlas sliderTextureAtlas = new TextureAtlas(Gdx.files.internal("slider.pack"));
        stage = new Stage();
        multiplexer.addProcessor(stage);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        Skin buttonSkin = new Skin(buttonTextureAtlas);
        buttonStyle.checked = buttonSkin.getDrawable("down");
        buttonStyle.up = buttonSkin.getDrawable("up");
        buttonStyle.over = buttonSkin.getDrawable("hover");
        buttonStyle.checkedOver = buttonSkin.getDrawable("hover");
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        button_player = new TextButton("Local Human", buttonStyle);
        button_tree = new TextButton("TreeHunter (AI)", buttonStyle);
        button_neural = new TextButton("NeuralPower (AI)", buttonStyle);

        stage.addActor(button_player);
        stage.addActor(button_tree);
        stage.addActor(button_neural);


        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Skin sliderSkin = new Skin(sliderTextureAtlas);
        sliderStyle.knob = sliderSkin.getDrawable("knob");
        sliderStyle.background = sliderSkin.getDrawable("background");
        tree_complexity = new Slider(5, 105, 5, false, sliderStyle);

        stage.addActor(tree_complexity);
    }

    void drawSelection(float x, float y) {
        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, "Select Opponent", x, y);
        batch.end();

        button_player.setX(x);
        button_player.setY(y - 100);
        button_tree.setX(x);
        button_tree.setY(y - 180);
        tree_complexity.setWidth(310);
        tree_complexity.setX(x + 10);
        tree_complexity.setY(y - 232);
        button_neural.setX(x);
        button_neural.setY(y - 300);
        stage.draw();
    }
}
