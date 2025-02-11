package io.github.uniclog.game_ecs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.uniclog.game_ecs.Core;
import io.github.uniclog.game_ecs.component.*;
import io.github.uniclog.game_ecs.engine.WorldEngine;
import io.github.uniclog.game_ecs.engine.internal.SceneUtils;
import io.github.uniclog.game_ecs.system.AnimationSystem;
import io.github.uniclog.game_ecs.system.RenderSystem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class GameScreen implements Screen, SceneUtils {

    private final TextureAtlas textureAtlas;
    private final Stage stage;

    private final WorldEngine worldEngine;


    public GameScreen(Core core) throws IllegalAccessException {

        textureAtlas = new TextureAtlas(Gdx.files.internal("example/atlas/gameObjects.atlas"));
        stage = new Stage(new ExtendViewport(16f, 9f));

        TiledMap tiledMap = new TmxMapLoader().load("map/tileset.tmx");

        worldEngine = new WorldEngine();
        worldEngine.addSystem(AnimationSystem.class);
        worldEngine.addSystem(RenderSystem.class);
        worldEngine.addComponentListener(ImageComponent.ImageComponentListener.class);

        worldEngine.inject(Stage.class, stage);
        worldEngine.inject(TextureAtlas.class, textureAtlas);

        // добавление карты
        var entityMap = worldEngine.createEntity();
        entityMap.addComponent(new TiledMapComponent(tiledMap));
        worldEngine.addEntity(entityMap);

        // добавление сущности 1
        var entity = worldEngine.createEntity();
        entity.addComponent(ImageComponent.class);
        var image = new Image();
        image.setSize(4f, 4f);
        image.setPosition(17f, 14f);
        entity.inject(Image.class, image);

        //var animComp = new AnimationComponent(AnimationModel.PLAYER, AnimationType.IDLE);
        var animComp = new AnimationComponent(AnimationModel.PLAYER, AnimationType.IDLE);
        animComp.setFrameDuration(0.15f);
        entity.addComponent(animComp);
        worldEngine.addEntity(entity);


        // добавление сущности 2
        var entity2 = worldEngine.createEntity();
        entity2.addComponent(ImageComponent.class);
        var image2 = new Image();
        image2.setSize(2f, 2f);
        //image2.setPosition(12f, 0f);
        image2.setPosition(15f, 12f);
        entity2.inject(Image.class, image2);
        //entity2.addComponent(new AnimationComponent(AnimationModel.SLIME, AnimationType.IDLE));
        entity2.addComponent(new AnimationComponent(AnimationModel.SLIME, AnimationType.IDLE).setFrameDuration(0.15f));
        worldEngine.addEntity(entity2);

        // добавление сущности в цикле
        // var random = new Random();
        // for (var i = 0; i < 10; i++) {
        //     var entity3 = worldEngine.createEntity();
        //     entity3.addComponent(ImageComponent.class);
        //     var image3 = new Image();
        //     image3.setSize(2f, 2f);
        //     var x = random.nextFloat(14);
        //     var y = random.nextFloat(6);
        //     image3.setPosition(x, y);
        //     entity3.inject(Image.class, image3);
        //     entity3.addComponent(new AnimationComponent(AnimationModel.SLIME, AnimationType.IDLE).setFrameDuration(0.05f));
        //     worldEngine.addEntity(entity3);
        // }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldEngine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        safeDispose(stage);
        safeDispose(textureAtlas);

        worldEngine.disposeSafely();
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
