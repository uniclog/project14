package io.github.uniclog.game_ecs.engine;

public interface ComponentListener <T extends Component> {
    void onCompAdded(Entity entity, T comp);
    void onCompRemoved(Entity entity, T comp);
    String getComponentName();
}
