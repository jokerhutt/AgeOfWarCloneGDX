package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import jokerhut.main.ecs.component.AnimationComponent;
import jokerhut.main.ecs.component.AnimationType;
import jokerhut.main.ecs.component.SpriteComponent;
import jokerhut.main.ecs.component.TeamComponent;

public class UnitAnimationSystem extends IteratingSystem {

    private final ComponentMapper<AnimationComponent> animMapper = ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<SpriteComponent> spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<TeamComponent> teammComponentMapper = ComponentMapper.getFor(TeamComponent.class);


    public UnitAnimationSystem() {
        super(Family.all(AnimationComponent.class, SpriteComponent.class, TeamComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent anim = animMapper.get(entity);
        SpriteComponent sprite = spriteMapper.get(entity);
        TeamComponent team = teammComponentMapper.get(entity);
        anim.current = anim.animations.get(anim.currentType);


        if (anim.current == null) return;

        anim.stateTime += deltaTime;
        TextureRegion frame;
        if (anim.currentType != AnimationType.DEAD) {
            frame = anim.current.getKeyFrame(anim.stateTime, true);
        } else {
            frame = anim.current.getKeyFrame(anim.stateTime, false);
        }
        sprite.sprite.setRegion(frame);



    }

    private boolean shouldFaceLeft (Entity entity) {
        TeamComponent team = teammComponentMapper.get(entity);
        return team.teamType == 0;
    }

}
