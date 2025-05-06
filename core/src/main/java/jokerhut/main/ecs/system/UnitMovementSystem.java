package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.component.*;
import jokerhut.main.entitymanagement.SpawnedQueue;

public class UnitMovementSystem extends IteratingSystem {

    private final ComponentMapper<Box2DComponent> boxMapper = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<MoveComponent> moveMapper = ComponentMapper.getFor(MoveComponent.class);
    private final ComponentMapper<TeamComponent> teammComponentMapper = ComponentMapper.getFor(TeamComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    private SpawnedQueue spawnedAlliedQueue;
    private SpawnedQueue spawnedEnemyQueue;

    public UnitMovementSystem(final SpawnedQueue spawnedAlliedQueue, final SpawnedQueue spawnedEnemyQueue) {
        super(Family.all(Box2DComponent.class, MoveComponent.class, TeamComponent.class, AnimationComponent.class).get());
        this.spawnedAlliedQueue = spawnedAlliedQueue;
        this.spawnedEnemyQueue = spawnedEnemyQueue;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        handleMovement(entity);
    }

    private void handleMovement (Entity entity) {

        AnimationComponent animationComponent = animationMapper.get(entity);
        AnimationType state = animationComponent.currentType;
        Box2DComponent box2DComponent = boxMapper.get(entity);
        MoveComponent moveComponent = moveMapper.get(entity);
        TeamComponent teamComponent = teammComponentMapper.get(entity);

        float velocityX = 0;

        switch (state) {

            case IDLE, DEAD, ATTACK -> velocityX = 0;
            case WALK -> velocityX = 0.5f;

        }

        if (teamComponent.teamType == 0) {
            velocityX *= -1;
        }

        Vector2 position = box2DComponent.body.getPosition();
        box2DComponent.body.setLinearVelocity(velocityX, moveComponent.yClamp);

        box2DComponent.body.setTransform(position.x, moveComponent.yClamp, box2DComponent.body.getAngle());




    }

}
