package io.github.uniclog.game_ecs.component;

public enum AnimationType {
    IDLE, RUN, ATTACK, DEATH, OPEN;

    public final String animationType = this.toString().toLowerCase();
}
