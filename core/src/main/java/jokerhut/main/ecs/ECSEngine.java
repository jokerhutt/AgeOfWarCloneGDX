package jokerhut.main.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import jokerhut.main.MainGame;
import jokerhut.main.ecs.component.*;
import jokerhut.main.ecs.system.*;
import jokerhut.main.entitymanagement.EntityManager;
import jokerhut.main.entitymanagement.OpponentAiManager;
import jokerhut.main.entitymanagement.SpawnedQueue;
import jokerhut.main.entitymanagement.UnitQueue;
import jokerhut.main.screen.GameScreen;

import static jokerhut.main.MainGame.resetBodyAndFixtureDefinition;
import static jokerhut.main.screen.GameScreen.*;

public class ECSEngine extends PooledEngine {

    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private SpawnedQueue spawnedEnemyQueue;
    private SpawnedQueue spawnedAlliedQueue;
    private int opponentCoins;
    private int playerCoins;

    private EntityManager playerTeamManager;
    private OpponentAiManager enemyTeamManager;


    public int getPlayerCoins() {
        return playerCoins;
    }

    public int getOpponentCoins() {
        return opponentCoins;
    }

    public void setOpponentCoins(int opponentCoins) {
        this.opponentCoins = opponentCoins;
    }

    public void setPlayerCoins(int playerCoins) {
        this.playerCoins = playerCoins;
    }

    public ECSEngine (final MainGame context) {
        super();
        this.world = context.getWorld();
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        spawnedAlliedQueue = new SpawnedQueue();
        spawnedEnemyQueue = new SpawnedQueue();
        opponentCoins = 60;
        playerCoins = 60;
        this.addSystem(new UnitMovementSystem(spawnedAlliedQueue, spawnedEnemyQueue));
        this.addSystem(new UnitAttackSystem(spawnedAlliedQueue, spawnedEnemyQueue));
        this.addSystem(new UnitHealthSystem(this));
        this.addSystem(new UnitAnimationSystem());
        this.addSystem(new SpriteRenderSystem(context.getSpriteBatch()));

    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void addBox2DComponent (Entity entity, Vector2 spawnPos) {
        Box2DComponent box = createComponent(Box2DComponent.class);
        box.width = 2f;
        box.height = 2f;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(spawnPos);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(box.width / 2f, box.height / 2f);
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;

        body.createFixture(fixtureDef);
        shape.dispose();

        box.body = body;

        entity.add(box);
    }

    public void addMoveComponent (Entity entity) {
        MoveComponent move = createComponent(MoveComponent.class);
        move.velocity.set(0.5f, 0f); // move right
        move.yClamp = 1f;
        entity.add(move);
    }

    public void addTeamComponent (Entity entity, int team) {
        TeamComponent teamComp = createComponent(TeamComponent.class);
        teamComp.teamType = team;
        entity.add(teamComp);
    }

    public void addSpriteComponent (Entity entity, TextureRegion region) {
        SpriteComponent sprite = createComponent(SpriteComponent.class);
        sprite.setRegion(region);
        sprite.sprite.setSize(2, 2);
        entity.add(sprite);
    }

    public void addAnimationComponent (Entity entity, Texture texture, int teamType) {
        AnimationComponent anim = createComponent(AnimationComponent.class);
        TextureRegion[][] frames = TextureRegion.split(texture, 192, 192);
        TextureRegion[] walkFrames = new TextureRegion[] { frames[1][0], frames[1][1], frames[1][2], frames[1][3], frames[1][4], frames[1][5] };

        Texture deathTexture = new Texture("character/Dead.png");
        TextureRegion[][] dFrames = TextureRegion.split(deathTexture, 128, 128);
        TextureRegion[] deathFrames = new TextureRegion[] {
            dFrames[0][0], dFrames[0][1], dFrames[0][2], dFrames[0][3], dFrames[0][4], dFrames[0][5], dFrames[0][6],
            dFrames[1][0], dFrames[1][1], dFrames[1][2], dFrames[1][3], dFrames[1][4], dFrames[1][5], dFrames[1][6]
        };

        TextureRegion[] attackFrames = new TextureRegion[] {
            frames[3][0], frames[3][1], frames[3][2], frames[3][3], frames[3][4], frames[3][5]
        };

        TextureRegion[] idleFrames = new TextureRegion[] {
            frames[0][0], frames[0][1], frames[0][2], frames[0][3], frames[0][4], frames[0][5]
        };

        if (teamType == 0) {
            for (TextureRegion frame : walkFrames) {
                frame.flip(true, false);
            }
            for (TextureRegion frame : attackFrames) {
                frame.flip(true, false);
            }
            for (TextureRegion frame : idleFrames) {
                frame.flip(true, false);
            }
            for (TextureRegion frame : deathFrames) {
                frame.flip(true, false);
            }
        }

        Animation<TextureRegion> walkAnim = new Animation<>(0.1f, walkFrames);
        Animation<TextureRegion> attackAnim = new Animation<>(0.1f, attackFrames);
        Animation<TextureRegion> idleAnim = new Animation<>(0.1f, idleFrames);
        Animation<TextureRegion> deathAnim = new Animation<>(0.1f, deathFrames);
        deathAnim.setPlayMode(Animation.PlayMode.NORMAL);

        anim.animations.put(AnimationType.WALK, walkAnim);
        anim.animations.put(AnimationType.ATTACK, attackAnim);
        anim.animations.put(AnimationType.IDLE, idleAnim);
        anim.animations.put(AnimationType.DEAD, deathAnim);

        anim.current = walkAnim;
        anim.currentType = AnimationType.WALK;
        anim.play(AnimationType.WALK);
        entity.add(anim);
    }

    public void addHealthComponent (Entity entity, float health) {
        HealthComponent healthComponent = createComponent(HealthComponent.class);
        healthComponent.health = health;
        entity.add(healthComponent);
    }

    public void addAttackComponent (Entity entity, float damage, AttackType attackType, float range) {
        AttackComponent attackComponent = createComponent(AttackComponent.class);
        attackComponent.damage = damage;
        attackComponent.attackType = attackType;
        attackComponent.range = range;
        entity.add(attackComponent);
    }

    public void createPeasant (Vector2 spawnPos, TextureRegion region, Texture texture, int teamType) {
        resetBodyAndFixtureDefinition();
        Entity unit = this.createEntity();
        addBox2DComponent(unit, spawnPos);
        addMoveComponent(unit);
        addSpriteComponent(unit, region);
        addAnimationComponent(unit, texture, teamType);
        addTeamComponent(unit, teamType);
        addHealthComponent(unit, 3);
        addAttackComponent(unit, 0.5f, AttackType.MELEE, 1f);
        addToSpawnedQueue(unit, teamType);
        addEntity(unit);
    }


    public void createArcher(Vector2 spawnPos, TextureRegion region, Texture texture,  int teamType) {

            resetBodyAndFixtureDefinition();
            Entity unit = this.createEntity();
            addBox2DComponent(unit, spawnPos);
            addMoveComponent(unit);
            addSpriteComponent(unit, region);
            addAnimationComponent(unit, texture, teamType);
            addTeamComponent(unit, teamType);
            addHealthComponent(unit, 2);
            addAttackComponent(unit, 0.2f, AttackType.RANGED, 2.5f);
            addToSpawnedQueue(unit, teamType);

            addEntity(unit);


    }

    public void createKnight(Vector2 spawnPos, TextureRegion region, Texture texture, int teamType) {
        int cost = 5;


            resetBodyAndFixtureDefinition();

            Entity unit = this.createEntity();

            addBox2DComponent(unit, spawnPos);
            addAnimationComponent(unit, texture, teamType);
            addSpriteComponent(unit, region);
            addMoveComponent(unit);
            addTeamComponent(unit, teamType);
            addHealthComponent(unit, 6);
            addAttackComponent(unit, 1f, AttackType.MELEE, 1f);
            addToSpawnedQueue(unit, teamType);
            addEntity(unit);

    }

    public void addToSpawnedQueue (Entity unit, int teamType) {
        if (teamType == 1) {
            spawnedAlliedQueue.enqueue(unit);
        } else {
            spawnedEnemyQueue.enqueue(unit);
        }
    }

    public void handleEntityDeath (Entity unit, int teamType, AnimationComponent animationComponent) {
        if (teamType == 1) {
            spawnedAlliedQueue.dequeue();
            animationComponent.currentType = AnimationType.DEAD;
            animationComponent.stateTime = 0f;
            playerCoins += 1;

        } else {
            spawnedEnemyQueue.dequeue();
            animationComponent.currentType = AnimationType.DEAD;
            animationComponent.stateTime = 0f;
            playerCoins += 1;

        }
    }

    public void clearEntity (Entity unit) {
        removeEntity(unit);
    }

}
