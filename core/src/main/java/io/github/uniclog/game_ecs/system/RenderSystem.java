package io.github.uniclog.game_ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.uniclog.game_ecs.component.ImageComponent;
import io.github.uniclog.game_ecs.component.PlayerComponent;
import io.github.uniclog.game_ecs.component.PositionComponent;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.EntityManager;
import io.github.uniclog.game_ecs.engine.System;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import io.github.uniclog.game_ecs.engine.component.EngineComponents;
import io.github.uniclog.game_ecs.engine.component.TiledMapEngineComponent;

import java.util.ArrayList;
import java.util.List;

import static io.github.uniclog.game_ecs.engine.internal.StageUtils.use;

public class RenderSystem implements System, EventListener {
    private final List<TiledMapTileLayer> bgdLayers = new ArrayList<>();
    private final List<TiledMapTileLayer> fgdLayers = new ArrayList<>();

    @InjectedObject
    private Stage stage;

    @InjectedObject
    private EntityManager entityManager;

    @InjectedObject
    private OrthographicCamera camera;

    @InjectedObject
    private EngineComponents components;

    /*@Override
    public void render(float delta) {
        var players = entityManager.getEntitiesWithComponents(PlayerComponent.class);
        Entity player = null;
        if (!players.isEmpty()) {
            player = players.get(0);
        }
        if (player != null) {
            Image playerImg = player.getComponent(ImageComponent.class).getImage();
            float leap = 0.1f;
            camera.position.lerp(
                new Vector3(playerImg.getX() + playerImg.getWidth() / 2, playerImg.getY() + playerImg.getHeight() / 2, 0),
                leap
            );
        } else {
            camera.position.set(20f, 15f, 0);
        }
        camera.update();
        ///  ----------------------------------------------------------------------------------------------------

        stage.getViewport().apply();
        AnimatedTiledMapTile.updateAnimationBaseTime();

        var renderer = components.getComponent(TiledMapEngineComponent.class).getRenderer();
        renderer.setView(camera);
        if (!bgdLayers.isEmpty()) {
            use(stage.getBatch(), camera.combined, batch -> {
                for (TiledMapTileLayer layer : bgdLayers) {
                    renderer.renderTileLayer(layer);
                }
            });
        }

        stage.act(delta);
        stage.draw();

        if (!fgdLayers.isEmpty()) {
            use(stage.getBatch(), camera.combined, batch -> {
                for (TiledMapTileLayer layer : fgdLayers) {
                    renderer.renderTileLayer(layer);
                }
            });
        }
    }*/
    @Override
    public void render(float alpha) {
        // игрок
        var players = entityManager.getEntitiesWithComponents(PlayerComponent.class);
        Entity player = players.isEmpty() ? null : players.get(0);

        // интерполяция позиции и камеры
        if (player != null) {
            var pos = player.getComponent(PositionComponent.class);
            var imgComp = player.getComponent(ImageComponent.class);
            var image = imgComp.getImage();

            // интерполяция позиции
            float interpX = pos.prevX + (pos.x - pos.prevX) * alpha;
            float interpY = pos.prevY + (pos.y - pos.prevY) * alpha;

            image.setPosition(interpX, interpY);

            // плавное движение камеры
            float leap = 0.5f;
            camera.position.lerp(new Vector3(interpX + image.getWidth() / 2, interpY + image.getHeight() / 2, 0), leap);
        }

        camera.update();
        stage.getViewport().apply();

        AnimatedTiledMapTile.updateAnimationBaseTime();
        var renderer = components.getComponent(TiledMapEngineComponent.class).getRenderer();
        renderer.setView(camera);

        // фон
        if (!bgdLayers.isEmpty()) {
            use(stage.getBatch(), camera.combined, batch -> {
                for (var layer : bgdLayers) {
                    renderer.renderTileLayer(layer);
                }
            });
        }

        // акторы (спрайты)
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // передний план
        if (!fgdLayers.isEmpty()) {
            use(stage.getBatch(), camera.combined, batch -> {
                for (var layer : fgdLayers) {
                    renderer.renderTileLayer(layer);
                }
            });
        }
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void registerAsListener() {
        stage.addListener(this);
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof TiledMapEngineComponent mce) {
            bgdLayers.clear();
            fgdLayers.clear();

            mce.getTiledMapTileLayers().forEach(layer -> {
                if (layer.getName().startsWith("fgd_")) {
                    fgdLayers.add(layer);
                }
                if (layer.getName().startsWith("bgd_")) {
                    bgdLayers.add(layer);
                }
                if (layer.getName().startsWith("ground")) {
                    bgdLayers.add(layer);
                }
                if (layer.getName().startsWith("tree1")) {
                    bgdLayers.add(layer);
                }
            });
            return true;
        }
        return false;
    }
}
