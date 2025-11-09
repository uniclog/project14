package io.github.uniclog.game_ecs.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.github.uniclog.game_ecs.Core;
import io.github.uniclog.game_ecs.component.*;
import io.github.uniclog.game_ecs.engine.WorldEngine;
import io.github.uniclog.game_ecs.engine.component.TiledMapEngineComponent;
import io.github.uniclog.game_ecs.engine.internal.SceneUtils;
import io.github.uniclog.game_ecs.system.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Random;

@Slf4j
public class Screen2 implements Screen, SceneUtils {
    private static final int UPS = 60; // частота апдейтов логики
    private static final float DELTA_UPDATE = 1f / UPS;

    private final TextureAtlas textureAtlas;
    private final Stage stage;
    private final WorldEngine worldEngine;
    private final OrthographicCamera camera;
    private final TiledMap tiledMap;

    private Thread logicThread;
    private volatile boolean running = true;

    public Screen2(Core core) throws IllegalAccessException {
        textureAtlas = new TextureAtlas(Gdx.files.internal("example/atlas/gameObjects.atlas"));
        stage = new Stage(new ExtendViewport(16f, 9f));
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        camera.zoom = 0.7f;
        tiledMap = new TmxMapLoader().load("map/tileset.tmx");
        worldEngine = new WorldEngine();
    }

    @Override
    public void show() {
        // DI-компоненты
        worldEngine.inject(Stage.class, stage);
        worldEngine.inject(TextureAtlas.class, textureAtlas);
        worldEngine.inject(OrthographicCamera.class, camera);

        // Слушатели
        worldEngine.addComponentListener(ImageComponent.ImageComponentListener.class);
        worldEngine.addComponentListener(TiledMapEngineComponent.TiledMapEngineComponentListener.class);

        // Системы
        worldEngine.addSystem(AnimationSystem.class);
        worldEngine.addSystem(RenderSystem.class);
        worldEngine.addSystem(MovementSystem.class);
        worldEngine.addSystem(PlayerInputSystem.class);

        // Карта
        worldEngine.addEngineComponent(new TiledMapEngineComponent(tiledMap));

        // Игрок
        var player = worldEngine.createEntity();
        player.addComponent(ImageComponent.class);
        var image = new com.badlogic.gdx.scenes.scene2d.ui.Image();
        image.setSize(2f, 2f);
        player.addComponent(new PositionComponent(17f, 14f));
        //image.setPosition(17f, 14f);
        player.inject(com.badlogic.gdx.scenes.scene2d.ui.Image.class, image);
        player.addComponent(new AnimationComponent(AnimationModel.PLAYER, AnimationType.IDLE).setFrameDuration(0.15f));
        player.addComponent(new VelocityComponent(0f, 0f, 3f));
        player.addComponent(PlayerComponent.class);
        worldEngine.addEntity(player);

        /*// Прочие сущности
        var random = new Random();
        for (var i = 0; i < 40; i++) {
            var slime = worldEngine.createEntity();
            slime.addComponent(ImageComponent.class);
            var img = new com.badlogic.gdx.scenes.scene2d.ui.Image();
            img.setSize(2f, 2f);
            img.setPosition(random.nextFloat(20), random.nextFloat(4));
            slime.inject(com.badlogic.gdx.scenes.scene2d.ui.Image.class, img);
            slime.addComponent(new AnimationComponent(AnimationModel.SLIME, AnimationType.IDLE).setFrameDuration(0.15f));
            worldEngine.addEntity(slime);
        }*/

        startLogicThread(); // запускаем апдейт-поток
    }

    private void startLogicThread() {
        logicThread = new Thread(() -> {
            long updateInterval = (long)(1_000_000_000 / UPS);
            long lastUpdate = System.nanoTime();

            while (running) {
                long now = System.nanoTime();
                if (now - lastUpdate >= updateInterval) {
                    try {
                        worldEngine.update(DELTA_UPDATE); // логика
                    } catch (Exception e) {
                        log.error("Error during update: ", e);
                    }
                    lastUpdate += updateInterval;
                } else {
                    long sleepNanos = updateInterval - (now - lastUpdate);
                    if (sleepNanos > 0) {
                        try {
                            Thread.sleep(sleepNanos / 1_000_000, (int)(sleepNanos % 1_000_000));
                        } catch (InterruptedException ignored) {}
                    }
                }
            }
        }, "LogicThread");

        logicThread.setDaemon(true);
        logicThread.start();
    }

    @Override
    public void render(float delta) {
        // Только рендер
        //Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldEngine.render(delta);
        // stage.act(delta); // если нужно обновление UI
        stage.draw(); // рендер актёров (UI, изображения)
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
        running = false;
        try {
            logicThread.join(500);
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void dispose() {
        running = false;
        safeDispose(stage);
        safeDispose(textureAtlas);
        worldEngine.disposeSafely();
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void safeDispose(Disposable object) {
        SceneUtils.super.safeDispose(object);
    }
}
