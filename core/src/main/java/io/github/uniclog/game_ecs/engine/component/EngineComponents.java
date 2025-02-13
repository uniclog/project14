package io.github.uniclog.game_ecs.engine.component;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EngineComponents {
    private Map<String, EngineComponent> components;

    public EngineComponents() {
        this.components = new HashMap<>();
    }

    public void put(String name, EngineComponent component) {
        components.put(name, component);
    }

    public <T extends EngineComponent> T getComponent(Class<T> componentClass) {
        Object component = components.get(componentClass.getName());
        if (component == null) {
            return null;
        }
        try {
            return componentClass.cast(component);
        } catch (ClassCastException e) {
            return null;
        }
    }
}
