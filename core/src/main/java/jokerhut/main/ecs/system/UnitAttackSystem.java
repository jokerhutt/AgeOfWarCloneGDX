package jokerhut.main.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.component.*;
import jokerhut.main.entitymanagement.SpawnedQueue;
import jokerhut.main.entitymanagement.UnitQueue;
import jokerhut.main.screen.GameScreen;

public class UnitAttackSystem extends IteratingSystem {

    private final ComponentMapper<Box2DComponent> boxMapper = ComponentMapper.getFor(Box2DComponent.class);
    private final ComponentMapper<MoveComponent> moveMapper = ComponentMapper.getFor(MoveComponent.class);
    private final ComponentMapper<TeamComponent> teammComponentMapper = ComponentMapper.getFor(TeamComponent.class);
    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    private final ComponentMapper<AttackComponent> attackMapper = ComponentMapper.getFor(AttackComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    private SpawnedQueue spawnedAlliedQueue;
    private SpawnedQueue spawnedEnemyQueue;


    public UnitAttackSystem(final SpawnedQueue spawnedAlliedQueue, final SpawnedQueue spawnedEnemyQueue) {
        super(Family.all(AnimationComponent.class, Box2DComponent.class, MoveComponent.class, TeamComponent.class, HealthComponent.class, AttackComponent.class).get());
        this.spawnedAlliedQueue = spawnedAlliedQueue;
        this.spawnedEnemyQueue = spawnedEnemyQueue;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        SpawnedQueue queue;
        SpawnedQueue enemyQueue;
        int scalar = 1;
        AttackComponent attack = attackMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        TeamComponent team = teammComponentMapper.get(entity);

        if (!attack.canAttack) {
            attack.attackTimer += deltaTime;
            if (attack.attackTimer >= 0.5f) {
                attack.canAttack = true;
                attack.attackTimer = 0f;
            }
        }

        if (animationComponent.currentType == AnimationType.ATTACK) {

            if (team.teamType == 1) {
                enemyQueue = spawnedEnemyQueue;
            } else {
                enemyQueue = spawnedAlliedQueue;
            }

            Entity otherEntity = enemyQueue.peek();

            if (otherEntity != null && attack.canAttack) {
                HealthComponent otherEntityHealthComponent = healthMapper.get(otherEntity);
                attackEnemy(attack, otherEntityHealthComponent);
            }

        }
    }

    private void attackEnemy (AttackComponent initiatorAttack, HealthComponent targetHealth) {
        if (initiatorAttack.canAttack) {
            targetHealth.takeDamage(initiatorAttack.damage);
            initiatorAttack.canAttack = false;
        }
    }



}
