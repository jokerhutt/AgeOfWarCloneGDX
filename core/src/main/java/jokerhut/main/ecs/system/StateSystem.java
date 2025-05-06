package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.component.*;
import jokerhut.main.entitymanagement.SpawnedQueue;

public class StateSystem extends IteratingSystem {

    private final ComponentMapper<Box2DComponent> boxMapper = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<MoveComponent> moveMapper = ComponentMapper.getFor(MoveComponent.class);
    private final ComponentMapper<TeamComponent> teammComponentMapper = ComponentMapper.getFor(TeamComponent.class);
    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<AttackComponent> attackMapper = ComponentMapper.getFor(AttackComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    private SpawnedQueue spawnedAlliedQueue;
    private SpawnedQueue spawnedEnemyQueue;


    public StateSystem(final SpawnedQueue spawnedAlliedQueue, final SpawnedQueue spawnedEnemyQueue) {
        super(Family.all(AnimationComponent.class, Box2DComponent.class, MoveComponent.class, TeamComponent.class, HealthComponent.class, AttackComponent.class).get());
        this.spawnedAlliedQueue = spawnedAlliedQueue;
        this.spawnedEnemyQueue = spawnedEnemyQueue;
    }

    @Override
    protected void processEntity(Entity entity, float v) {

        AnimationComponent animationComponent = animationMapper.get(entity);
        TeamComponent teamComponent = teammComponentMapper.get(entity);
        AttackComponent attackComponent = attackMapper.get(entity);
        Box2DComponent box2DComponent = boxMapper.get(entity);

        if (animationComponent.currentType != AnimationType.DEAD) {

            if (processAttackCheck(teamComponent.teamType, box2DComponent, attackComponent)) {
                animationComponent.currentType = AnimationType.ATTACK;
            } else if (processIdleCheck(entity, teamComponent.teamType, box2DComponent)) {
                animationComponent.currentType = AnimationType.IDLE;
            } else {
                animationComponent.currentType = AnimationType.WALK;
            }

        }



    }

    private boolean processIdleCheck (Entity entity, int teamType, Box2DComponent box2DComponent) {

        Entity nextEntity;

        if (teamType == 1) {
            nextEntity = spawnedAlliedQueue.getNextOf(entity);
        } else {
            nextEntity = spawnedEnemyQueue.getNextOf(entity);
        }

        if (nextEntity != null) {

            Box2DComponent nextEntityBodyComponent = boxMapper.get(nextEntity);
            Vector2 currentPosition = box2DComponent.body.getPosition();
            Vector2 nextEntityPosition = nextEntityBodyComponent.body.getPosition();

            float distanceToNextEntity = Math.abs(nextEntityPosition.x - currentPosition.x);

            if (distanceToNextEntity <= 1.5) {
                return true;
            }

        }

        return false;


    }

    private boolean processAttackCheck (int teamType, Box2DComponent box2DComponent, AttackComponent attackComponent) {

        SpawnedQueue enemyQueue;

        if (teamType == 1) {
            enemyQueue = spawnedEnemyQueue;
        } else {
            enemyQueue = spawnedAlliedQueue;
        }

        Entity otherEntity = enemyQueue.peek();

        float unitRange = attackComponent.range;

        if (otherEntity != null) {

            Box2DComponent enemyBoxComponent = boxMapper.get(otherEntity);
            Vector2 currentPosition = box2DComponent.body.getPosition();
            Vector2 enemyPosition = enemyBoxComponent.body.getPosition();

            float distanceToEnemy = Math.abs(currentPosition.x - enemyPosition.x);

            if (distanceToEnemy <= unitRange) {
                return true;
            }

        }

        return false;



    }




}
