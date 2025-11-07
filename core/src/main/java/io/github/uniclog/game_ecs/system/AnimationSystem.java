package io.github.uniclog.game_ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.uniclog.game_ecs.component.AnimationComponent;
import io.github.uniclog.game_ecs.component.ImageComponent;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.EntityManager;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import io.github.uniclog.game_ecs.engine.System;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static io.github.uniclog.game_ecs.component.AnimationComponent.NO_ANIMATION;
import static java.util.Arrays.stream;

@Slf4j
public class AnimationSystem implements System {
    public static float DEFAULT_FRAME_DURATION = 1 / 8f;

    @InjectedObject
    private TextureAtlas textureAtlas;
    @InjectedObject
    private EntityManager entityManager;

    private final Map<String, Animation<TextureRegionDrawable>> cachedAnim = new HashMap<>();

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entityManager.getEntitiesWithComponents(AnimationComponent.class)) {
            var animComp = entity.getComponent(AnimationComponent.class);
            var imageComp = entity.getComponent(ImageComponent.class);

            if (animComp.getNextAnimation().equals(NO_ANIMATION)) {
                animComp.addStateTime(deltaTime);
            } else {
                animComp.setAnimation(animation(animComp.getNextAnimation(), animComp.getFrameDuration()));
                animComp.setStateTime(0f);
                animComp.setNextAnimation(NO_ANIMATION);
            }
            animComp.getAnimation().setPlayMode(animComp.getPlayMode());
            var frame = animComp.getAnimation().getKeyFrame(animComp.getStateTime());
            imageComp.getImage().setDrawable(frame);
        }
    }

    @Override
    public void render(float delta) {

    }

    private Animation<TextureRegionDrawable> animation(String nextAnimation, float frameDuration) {
        var anim = cachedAnim.get(nextAnimation);
        if (anim == null) {
            log.info("Creating animation {}", nextAnimation);
            var regions = textureAtlas.findRegions(nextAnimation);
            if (regions.isEmpty()) {
                Gdx.app.error("AnimationSystem", "No regions found for " + nextAnimation);
            }
            var regionsArray = stream(regions.items).map(TextureRegionDrawable::new)
                .filter(region -> region.getRegion() != null)
                .toArray(TextureRegionDrawable[]::new);
            anim = new Animation<>(frameDuration, regionsArray);
            cachedAnim.put(nextAnimation, anim);
            return anim;
        }
        return anim;
    }
}
