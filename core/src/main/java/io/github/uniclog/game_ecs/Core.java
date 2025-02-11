package io.github.uniclog.game_ecs;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.uniclog.game_ecs.screen.GameScreen;

import java.util.HashMap;
import java.util.Map;

public class Core extends Game {
    Map<Class<Screen>, Screen> screens = new HashMap<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        try {
            this.setScreen(new GameScreen(this));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
