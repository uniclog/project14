package io.github.uniclog.game_ecs.engine;

import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import io.github.uniclog.game_ecs.engine.component.EngineComponent;
import io.github.uniclog.game_ecs.engine.component.EngineComponents;
import io.github.uniclog.game_ecs.engine.internal.SafeDisposable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class WorldEngine implements SafeDisposable {
    /// managers
    private final Map<String, ComponentListener<?>> componentListeners;
    private final EntityManager entityManager;
    private final SystemManager systemManager;
    /// injects
    private final Map<Class<?>, Object> dependencies;
    /// engine components
    private final EngineComponents components;

    public WorldEngine() {
        components = new EngineComponents();
        dependencies = new HashMap<>();
        componentListeners = new HashMap<>();
        entityManager = new EntityManager();
        systemManager = new SystemManager();
        dependencies.put(EntityManager.class, entityManager);
        dependencies.put(SystemManager.class, systemManager);
        dependencies.put(EngineComponents.class, components);
    }

    public Entity createEntity() {
        return new Entity(entityManager.size());
    }

    public void inject(Class<?> dependencyType, Object dependency) {
        Stream.of(
                systemManager.getSystems().stream(),
                componentListeners.values().stream(),
                components.getComponents().values().stream())
            .flatMap(Function.identity())
            .forEach(object -> {
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
            });
        dependencies.put(dependencyType, dependency);
    }

    public void injectDependenciesIn(Object object) {
        dependencies.forEach((cl, dep) -> {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectedObject.class) && field.getType().equals(cl)) {
                    field.setAccessible(true);
                    try {
                        field.set(object, dep);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    public void addSystem(Class<? extends System> systemObject) {
        try {
            // добавлем систему
            System system = systemObject.getDeclaredConstructor().newInstance();
            systemManager.add(system);
            // добавляем имеющиеся зависимости
            injectDependenciesIn(system);
            system.registerAsListener();
        } catch (Exception e) {
            log.error("Error adding system: {}", e.getMessage());
        }
    }

    public void addSystem(System system) {
        // добавлем систему
        systemManager.add(system);
        // добавляем имеющиеся зависимости
        injectDependenciesIn(system);
        system.registerAsListener();
    }

    public <T extends Component> void addComponentListener(Class<? extends ComponentListener<T>> componentListenerDeclaration) {
        try {
            // добавляем обработчик для компоента
            ComponentListener<T> componentListener = componentListenerDeclaration.getDeclaredConstructor().newInstance();
            componentListeners.put(componentListener.getComponentName(), componentListener);
            // добавляем имеющиеся зависимости
            injectDependenciesIn(componentListener);
            // todo добавить исполнение пред-настройки компонентов вызвать у них onCompAdded
        } catch (Exception e) {
            log.error("Error adding component listener: {}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void addEngineComponent(EngineComponent component) {
        try {
            this.components.put(component.getClass().getName(), component);
            ComponentListener<?> compLis = componentListeners.get(component.getClass().getName());
            if (compLis != null) {
                ComponentListener<EngineComponent> listener = (ComponentListener<EngineComponent>) compLis;
                listener.onCompAdded(null, component);
            }
        } catch (Exception e) {
            log.error("Error adding component: {}, message: {}", component, e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public void addEntity(Entity entity) {
        try {
            entity.getComponents().forEach((key, value) -> {
                ComponentListener<?> compLis = componentListeners.get(key);
                if (compLis != null) {
                    ComponentListener<Component> listener = (ComponentListener<Component>) compLis;
                    listener.onCompAdded(entity, value);
                }
            });
            entityManager.add(entity);
        } catch (Exception e) {
            log.error("Error adding entity: {}, message: {}", entity, e.getMessage(), e);
        }
    }

    public void removeEntity(Entity entity) {
        entityManager.remove(entity);
    }

    public void update(float delta) {
        systemManager.update(delta);
    }

    public void render(float delta) {
        systemManager.render(delta);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void dispose() {
        // todo реализовать очистку
    }
}
