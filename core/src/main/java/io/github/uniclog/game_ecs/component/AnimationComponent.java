package io.github.uniclog.game_ecs.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.uniclog.game_ecs.engine.Component;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.github.uniclog.game_ecs.system.AnimationSystem.DEFAULT_FRAME_DURATION;
import static java.lang.String.format;

@Data
@NoArgsConstructor
public class AnimationComponent implements Component {
    public final static String NO_ANIMATION = "";

    private String atlasKey = "";
    private Float stateTime = 0f;
    private Animation.PlayMode playMode = Animation.PlayMode.LOOP;
    private AnimationModel model = AnimationModel.NONE;

    private Animation<TextureRegionDrawable> animation;
    private String nextAnimation = "";
    private Float frameDuration = DEFAULT_FRAME_DURATION;

    public AnimationComponent(AnimationModel model, AnimationType type) {
        nextAnimation(model, type);
    }

    // player/idle_00
    // player/idle_01
    // player/idle_02
    public AnimationComponent nextAnimation(AnimationModel animationModel, AnimationType type) {
        this.model = animationModel;
        this.atlasKey = animationModel.modelName;
        this.nextAnimation = format("%s/%s", atlasKey, type.animationType);
        return this;
    }

    public void addStateTime(Float stateTime) {
        this.stateTime += stateTime;
    }

    public AnimationComponent setFrameDuration(Float frameDuration) {
        this.frameDuration = frameDuration;
        return this;
    }
}
