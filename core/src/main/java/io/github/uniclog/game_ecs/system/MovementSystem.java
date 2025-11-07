package io.github.uniclog.game_ecs.system;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.uniclog.game_ecs.component.ImageComponent;
import io.github.uniclog.game_ecs.component.VelocityComponent;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.EntityManager;
import io.github.uniclog.game_ecs.engine.System;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;

public class MovementSystem implements System {
    @InjectedObject
    private EntityManager entityManager;

    @Override
    public void update(float delta) {
        for (Entity entity : entityManager.getEntities()) {
            ImageComponent imageComponent = entity.getComponent(ImageComponent.class);
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

            if (imageComponent == null || velocity == null) continue;

            Image image = imageComponent.getImage();


            image.setPosition(
                image.getX() + velocity.vx * delta,
                image.getY() + velocity.vy * delta
            );
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void updateEntity(Entity entity) {
        System.super.updateEntity(entity);
    }
}
