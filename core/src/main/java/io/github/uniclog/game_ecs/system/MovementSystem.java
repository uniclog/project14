package io.github.uniclog.game_ecs.system;

import io.github.uniclog.game_ecs.component.PositionComponent;
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
        for (Entity entity : entityManager.getEntitiesWithComponents(PositionComponent.class, VelocityComponent.class)) {
            var pos = entity.getComponent(PositionComponent.class);
            var vel = entity.getComponent(VelocityComponent.class);
            if (pos == null || vel == null) continue;

            // Сохраняем предыдущее положение
            pos.prevX = pos.x;
            pos.prevY = pos.y;

            // Обновляем логические координаты
            pos.x += vel.vx * delta;
            pos.y += vel.vy * delta;
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
