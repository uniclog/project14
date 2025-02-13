package io.github.uniclog.game_ecs.component;

import io.github.uniclog.game_ecs.engine.Component;

public class VelocityComponent implements Component {
    public float vx, vy;
    public float delta;

    public VelocityComponent(float vx, float vy, float delta) {
        this.vx = vx;
        this.vy = vy;
        this.delta = delta;
    }
}
