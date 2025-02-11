package io.github.uniclog.game_ecs.engine.internal;

import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;

public interface SafeDisposable extends Disposable {
    Logger getLogger();

    default void disposeSafely() {
        try {
            this.dispose();
        } catch (Exception ex) {
            getLogger().warn(ex.getMessage());
        }
    }
}
