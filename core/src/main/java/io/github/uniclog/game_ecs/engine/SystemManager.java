package io.github.uniclog.game_ecs.engine;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    private List<System> systems;

    public SystemManager() {
        systems = new ArrayList<>();
    }

    public List<System> getSystems() {
        return systems;
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
}
