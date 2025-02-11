package io.github.uniclog.game_ecs.engine;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntityManager {
    private final List<Entity> entities = new ArrayList<>();

    public int size() {
        return entities.size();
    }

    public boolean add(Entity entity) {
        return entities.add(entity);
    }

    public void remove(Entity entity) {
        entities.remove(entity);
    }

    @SafeVarargs
    public final List<Entity> getEntitiesWithComponents(Class<? extends Component>... componentClasses) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            boolean hasAllComponents = true;
            for (Class<? extends Component> componentClass : componentClasses) {
                if (!entity.hasComponent(componentClass)) {
                    hasAllComponents = false;
                    break;
                }
            }
            if (hasAllComponents) {
                result.add(entity);
            }
        }
        return result;
    }
}
