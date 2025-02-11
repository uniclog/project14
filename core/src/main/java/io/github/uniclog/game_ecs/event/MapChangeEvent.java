package io.github.uniclog.game_ecs.event;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Event;

public class MapChangeEvent extends Event {
    public final TiledMap map;

    public MapChangeEvent(TiledMap tiledMap) {
        this.map = tiledMap;
    }


}
