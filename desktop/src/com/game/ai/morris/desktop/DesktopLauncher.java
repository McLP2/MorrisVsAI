package com.game.ai.morris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.ai.morris.Morris;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Morris Vs AI";
        config.width = 1; // non default for max fullscreen resolution
        config.fullscreen = true;
        new LwjglApplication(new Morris(), config);
    }
}
