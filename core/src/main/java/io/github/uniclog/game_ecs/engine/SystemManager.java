package io.github.uniclog.game_ecs.engine;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SystemManager {
    private final List<System> systems;

    public SystemManager() {
        systems = new ArrayList<>();
    }

    public void add(System system) {
        systems.add(system);
    }

    public void removeSystem(System system) {
        systems.remove(system);
    }

    public void update(float delta) {
        for (System system : systems) {
            system.update(delta);
        }
    }

    public void render(float delta) {
        for (System system : systems) {
            system.render(delta);
        }
    }
}
