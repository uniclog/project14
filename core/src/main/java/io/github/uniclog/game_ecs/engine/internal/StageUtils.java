package io.github.uniclog.game_ecs.engine.internal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

import java.util.function.Consumer;

public class StageUtils {
    public static void use(Batch batch, Matrix4 projectionMatrix, Consumer<Batch> action) {
        batch.setProjectionMatrix(projectionMatrix);
        batch.begin();
        try {
            action.accept(batch);
        } finally {
            batch.end();
        }
    }
}
