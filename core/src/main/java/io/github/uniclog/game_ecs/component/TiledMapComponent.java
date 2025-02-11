package io.github.uniclog.game_ecs.component;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.uniclog.game_ecs.engine.Component;

public class TiledMapComponent implements Component {
    public TiledMap tiledMap;
    public OrthogonalTiledMapRenderer mapRenderer;

    public TiledMapComponent(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f/32);
    }
}
