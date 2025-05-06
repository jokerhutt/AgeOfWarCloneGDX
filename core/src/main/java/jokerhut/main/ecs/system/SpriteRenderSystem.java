package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.component.*;

public class SpriteRenderSystem extends IteratingSystem {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final SpriteBatch batch;
    private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<Box2DComponent> boxMapper = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    public SpriteRenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class, HealthComponent.class, Box2DComponent.class, AnimationComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComp = spriteMapper.get(entity);
        Box2DComponent box = boxMapper.get(entity);
        HealthComponent healthComponent = healthMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);

        Sprite sprite = spriteComp.sprite;
        Vector2 pos = box.body.getPosition();

        sprite.setCenter(pos.x, pos.y);
        sprite.draw(batch);

        processHealthBars(healthComponent, pos, animationComponent);




    }

    private void processHealthBars (HealthComponent healthComponent, Vector2 pos, AnimationComponent animationComponent) {
        if (healthComponent.health < healthComponent.maxHealth && animationComponent.currentType != AnimationType.DEAD) {
            float width = 0.5f;
            float height = 0.1f;
            float x = pos.x - width / 2;
            float y = pos.y + 0.5f;

            float healthPercent = healthComponent.health / healthComponent.maxHealth;

            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix()); // FIX HERE
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(x, y, width * healthPercent, height);
            shapeRenderer.end();
            batch.begin();
        }
    }

}
