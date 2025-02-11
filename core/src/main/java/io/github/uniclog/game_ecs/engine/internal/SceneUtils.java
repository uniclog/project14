package io.github.uniclog.game_ecs.engine.internal;

import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;

public interface SceneUtils {

    Logger getLogger();

    default void safeDispose(Disposable object) {
        try {
            object.dispose();
        } catch (Exception ex) {
            getLogger().warn(ex.getMessage());
        }
    }
}
