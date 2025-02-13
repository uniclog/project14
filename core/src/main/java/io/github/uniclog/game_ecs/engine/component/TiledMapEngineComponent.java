package io.github.uniclog.game_ecs.engine.component;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.uniclog.game_ecs.engine.ComponentListener;
import io.github.uniclog.game_ecs.engine.Entity;
import io.github.uniclog.game_ecs.engine.annotation.InjectedObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TiledMapEngineComponent extends Event implements EngineComponent {
    public TiledMap tiledMap;
    public OrthogonalTiledMapRenderer renderer;

    public TiledMapEngineComponent(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        // this.renderer = new OrthogonalTiledMapRenderer(tiledMap, 1f/32);
    }

    public List<TiledMapTileLayer> getTiledMapTileLayers() {
        List<TiledMapTileLayer> layers = new ArrayList<>();
        for (MapLayer layer : tiledMap.getLayers()) {
            layers.addAll(new ArrayList<>(getTiledMapTileLayers(layer)));
        }
        return layers;
    }

    private List<TiledMapTileLayer> getTiledMapTileLayers(MapLayer layer) {
        if (layer instanceof TiledMapTileLayer tml) {
            return List.of(tml);
        }
        if (layer instanceof MapGroupLayer mgl) {
            for (MapLayer ml : mgl.getLayers()) {
                return getTiledMapTileLayers(ml);
            }
        }
        return List.of();
    }

    public static class TiledMapEngineComponentListener implements ComponentListener<TiledMapEngineComponent> {
        @InjectedObject
        private Stage stage;

        @Override
        public void onCompAdded(Entity entity, TiledMapEngineComponent comp) {
            comp.setRenderer(new OrthogonalTiledMapRenderer(null, 1 / 32f, stage.getBatch()));
            stage.getRoot().fire(comp);
        }

        @Override
        public void onCompRemoved(Entity entity, TiledMapEngineComponent comp) {

        }

        @Override
        public String getComponentName() {
            // todo как то это нужно оптимизировать (не хочется к каждому слушателю добавлять )
            return TiledMapEngineComponent.class.getName();
        }
    }
}
