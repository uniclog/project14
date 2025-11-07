package io.github.uniclog.game_ecs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.uniclog.game_ecs.Core;
import io.github.uniclog.game_ecs.component.*;
import io.github.uniclog.game_ecs.engine.WorldEngine;
import io.github.uniclog.game_ecs.engine.component.TiledMapEngineComponent;
import io.github.uniclog.game_ecs.engine.internal.SceneUtils;
import io.github.uniclog.game_ecs.system.AnimationSystem;
import io.github.uniclog.game_ecs.system.MovementSystem;
import io.github.uniclog.game_ecs.system.PlayerInputSystem;
import io.github.uniclog.game_ecs.system.RenderSystem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class GameScreen2 implements Screen, SceneUtils {

    private final TextureAtlas textureAtlas;
    private final Stage stage;

    private final WorldEngine worldEngine;
    private final OrthographicCamera camera;
    private TiledMap tiledMap;

    SpriteBatch batch;
    BitmapFont font;

    private final float STEP = 1f / 60f; // фиксированные UPS = 60
    private float accumulator = 0;

    private int updates = 0;   // счётчик UPS
    private int ups = 0;       // UPS в секунду
    private float timer = 0;

    public GameScreen2(Core core) throws IllegalAccessException {

        textureAtlas = new TextureAtlas(Gdx.files.internal("example/atlas/gameObjects.atlas"));
        stage = new Stage(new ExtendViewport(16f, 9f));
        camera = ((OrthographicCamera) stage.getViewport().getCamera());
        camera.zoom = 0.7f;

        tiledMap = new TmxMapLoader().load("map/tileset.tmx");

        worldEngine = new WorldEngine();
    }

    @Override
    public void show() {

        worldEngine.inject(Stage.class, stage);
        worldEngine.inject(TextureAtlas.class, textureAtlas);
        worldEngine.inject(OrthographicCamera.class, camera);

        worldEngine.addComponentListener(ImageComponent.ImageComponentListener.class);
        worldEngine.addComponentListener(TiledMapEngineComponent.TiledMapEngineComponentListener.class);

        worldEngine.addSystem(AnimationSystem.class);
        worldEngine.addSystem(RenderSystem.class);
        worldEngine.addSystem(MovementSystem.class);
        worldEngine.addSystem(PlayerInputSystem.class);

        worldEngine.addEngineComponent(new TiledMapEngineComponent(tiledMap));



        // добавление сущности игрока
        var player = worldEngine.createEntity();
        // add image component
        player.addComponent(ImageComponent.class);
        var image = new Image();
        image.setSize(2f, 2f);
        image.setPosition(17f, 14f);
        player.inject(Image.class, image);
        // add animation component
        var animComp = new AnimationComponent(AnimationModel.PLAYER, AnimationType.IDLE);
        animComp.setFrameDuration(0.15f);
        player.addComponent(animComp);
        // add move actions
        player.addComponent(new VelocityComponent(0f, 0f, 3f));
        player.addComponent(PlayerComponent.class);
        worldEngine.addEntity(player);


        // добавление сущности 2
        var entity2 = worldEngine.createEntity();
        entity2.addComponent(ImageComponent.class);
        var image2 = new Image();
        image2.setSize(1.5f, 1.5f);
        image2.setPosition(15f, 12f);
        entity2.inject(Image.class, image2);
        entity2.addComponent(new AnimationComponent(AnimationModel.SLIME, AnimationType.IDLE).setFrameDuration(0.15f));
        worldEngine.addEntity(entity2);
    }

    @Override
    public void render(float delta) {
        accumulator += delta;
        timer += delta;

        // фиксированные UPS
        while (accumulator >= STEP) {
            update(STEP);
            accumulator -= STEP;
        }

        // считаем UPS раз в секунду
        if (timer >= 1f) {
            ups = updates;
            updates = 0;
            timer -= 1f;
        }

        draw();
    }

    private void update(float dt) {
        updates++;
        worldEngine.update(dt);
        // здесь твоя игровая логика
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ScreenUtils.clear(0, 0, 0.2f, 1);

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 460);
        font.draw(batch, "UPS: " + ups, 20, 430);
        batch.end();
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
