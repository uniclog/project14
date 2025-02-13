package io.github.uniclog.game_ecs.component;

import io.github.uniclog.game_ecs.engine.Component;

public class PositionComponent implements Component {
    public float x, y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
