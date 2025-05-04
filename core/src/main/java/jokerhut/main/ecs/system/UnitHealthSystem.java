package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.ecs.component.AnimationComponent;
import jokerhut.main.ecs.component.AnimationType;
import jokerhut.main.ecs.component.HealthComponent;
import jokerhut.main.ecs.component.TeamComponent;

public class UnitHealthSystem extends IteratingSystem {

    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<TeamComponent> teammComponentMapper = ComponentMapper.getFor(TeamComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    private final ECSEngine contextEngine;
    public UnitHealthSystem (final ECSEngine contextEngine) {
        super (Family.all(HealthComponent.class, TeamComponent.class, AnimationComponent.class).get());
        this.contextEngine = contextEngine;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        HealthComponent healthComponent = healthMapper.get(entity);
        TeamComponent teamComponent = teammComponentMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        if (healthComponent.health <= 0 && animationComponent.currentType != AnimationType.DEAD) {
            contextEngine.handleEntityDeath(entity, teamComponent.teamType, animationComponent);
        }

        Animation<TextureRegion> deathAnim = animationComponent.animations.get(AnimationType.DEAD);
        float deathDuration = deathAnim.getAnimationDuration();

        if (animationComponent.currentType == AnimationType.DEAD) {
            if (healthComponent.deathTimer < deathDuration) {
                healthComponent.deathTimer+= v;
            } else {
                contextEngine.clearEntity(entity);
            }
        }


    }

}
