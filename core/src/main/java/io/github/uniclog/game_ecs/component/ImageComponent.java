package io.github.uniclog.game_ecs.component;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.uniclog.game_ecs.engine.Component;
import io.github.uniclog.game_ecs.engine.ComponentListener;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import lombok.Data;

@Data
public class ImageComponent implements Component, Comparable<ImageComponent> {
    @InjectedObject(String.class)
    private Image image;

    @Override
    public int compareTo(ImageComponent other) {
        var dy = Float.compare(image.getImageY(), other.image.getImageY());
        return dy != 0 ? dy : Float.compare(image.getImageY(), other.image.getImageY());
    }

    public static class ImageComponentListener implements ComponentListener<ImageComponent> {
        @InjectedObject
        private Stage stage;

        @Override
        public void onCompAdded(Entity entity, ImageComponent comp) {
            stage.addActor(comp.image);
        }

        @Override
        public void onCompRemoved(Entity entity, ImageComponent comp) {
            stage.getRoot().removeActor(comp.image);
        }

        @Override
        public String getComponentName() {
            return ImageComponent.class.getName();
        }
    }
}
