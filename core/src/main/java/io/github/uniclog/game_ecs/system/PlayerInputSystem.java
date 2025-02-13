package io.github.uniclog.game_ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.uniclog.game_ecs.component.*;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.EntityManager;
import io.github.uniclog.game_ecs.engine.System;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;

public class PlayerInputSystem implements System {
    @InjectedObject
    private EntityManager entityManager;

    @Override
    public void update(float delta) {
        for (Entity entity : entityManager.getEntities()) {
            // @todo мб нужно еше уточнить что это главный герой которым управляет игрок
            if (!entity.hasComponent(PlayerComponent.class)) continue;

            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            velocity.vx = 0;
            velocity.vy = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocity.vy = velocity.delta;
                entity.getComponent(AnimationComponent.class).nextAnimation(AnimationModel.SLIME, AnimationType.IDLE);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocity.vy = -velocity.delta;
                entity.getComponent(AnimationComponent.class).nextAnimation(AnimationModel.PLAYER, AnimationType.IDLE);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.vx = -velocity.delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.vx = velocity.delta;
            }
        }
    }

    @Override
    public void updateEntity(Entity entity) {
        System.super.updateEntity(entity);
    }
}
