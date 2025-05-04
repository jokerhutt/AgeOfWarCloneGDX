package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.component.Box2DComponent;
import jokerhut.main.ecs.component.SpriteComponent;

public class SpriteRenderSystem extends IteratingSystem {


    private final SpriteBatch batch;
    private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<Box2DComponent> boxMapper = ComponentMapper.getFor(Box2DComponent.class);

    public SpriteRenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class, Box2DComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComp = spriteMapper.get(entity);
        Box2DComponent box = boxMapper.get(entity);

        Sprite sprite = spriteComp.sprite;
        Vector2 pos = box.body.getPosition();

        // Align sprite center with body position
        sprite.setCenter(pos.x, pos.y);
        sprite.draw(batch);
    }

}
