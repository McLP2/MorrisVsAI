package com.game.morris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.morris.Morris;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Morris Vs AiSelection";
        config.width = 800;
        config.height = 620;
        config.fullscreen = true;
        config.samples = 8;
        new LwjglApplication(new Morris(), config);
    }
}
