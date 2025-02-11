package io.github.uniclog.game_ecs.component;

public enum AnimationModel {
    PLAYER, SLIME, NONE;

    public final String modelName = this.toString().toLowerCase();
}
