package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Component, Pool.Poolable {
    public Sprite sprite;

    public void setRegion(TextureRegion region) {
        if (sprite == null) {
            sprite = new Sprite(region);
        } else {
            sprite.setRegion(region);
        }
    }

    @Override
    public void reset() {
        sprite = null;
    }
}
