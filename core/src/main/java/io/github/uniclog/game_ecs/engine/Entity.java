package io.github.uniclog.game_ecs.engine;

import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Entity {
    private final int id;
    private final Map<String, Component> components;

    public Entity(int id) {
        this.id = id;
        this.components = new HashMap<>();
    }

    public <T extends Component> void addComponent(T component) {
        this.components.put(component.getClass().getName(), component);
    }

    public void addComponent(Class<? extends Component> componentObject) {
        try {
            Component component = componentObject.getDeclaredConstructor().newInstance();
            components.put(componentObject.getName(), component);
        } catch (Exception e) {
            // todo добавить вывод ошибки
            log.info(e.getMessage());
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass.getName()));
    }

    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        return components.containsKey(componentClass.getName());
    }

    public Map<String, Component> getComponents() {
        return components;
    }

    public int getId() {
        return id;
    }

    public void inject(Class<?> dependencyType, Object dependency) {
        for (Component object : components.values()) {
            Class<?> clazz = object.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectedObject.class) && field.getType().equals(dependencyType)) {
                    field.setAccessible(true);
                    try {
                        field.set(object, dependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        //dependencies.put(dependencyType, dependency);
    }
}
