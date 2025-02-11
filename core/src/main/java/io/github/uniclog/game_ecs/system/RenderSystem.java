package io.github.uniclog.game_ecs.system;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.uniclog.game_ecs.component.AnimationComponent;
import io.github.uniclog.game_ecs.component.ImageComponent;
import io.github.uniclog.game_ecs.component.TiledMapComponent;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.EntityManager;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import io.github.uniclog.game_ecs.engine.System;

import javax.swing.*;

import static io.github.uniclog.game_ecs.component.AnimationComponent.NO_ANIMATION;

public class RenderSystem implements System, EventListener {
    @InjectedObject
    private Stage stage;

    @InjectedObject
    private EntityManager entityManager;

    @InjectedObject
    private OrthographicCamera camera;

    @Override
    public void update(float delta) {
        // отображение карты
        for (Entity entity : entityManager.getEntitiesWithComponents(TiledMapComponent.class)) {
            TiledMapComponent mapComponent = entity.getComponent(TiledMapComponent.class);

            mapComponent.mapRenderer.setView((OrthographicCamera) stage.getViewport().getCamera());
            stage.getViewport().getCamera().position.set(20f, 15f, 0);
            stage.getViewport().getCamera().update();
            mapComponent.mapRenderer.render();
        }

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public boolean handle(Event event) {
        return false;
    }
}
