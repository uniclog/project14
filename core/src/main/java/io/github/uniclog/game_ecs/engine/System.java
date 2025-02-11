package io.github.uniclog.game_ecs.engine;

public interface System {
    void update(float delta);

    // todo продумать реализацию
    default void updateEntity(Entity entity) {
    }
}
