package jokerhut.main.entitymanagement;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.MainGame;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.screen.ButtonSlotListener;
import jokerhut.main.screen.GameScreen;

import static jokerhut.main.screen.GameScreen.*;

public class EntityManager implements ButtonSlotListener, UnitSpawner {

    Vector2 alliedSpawn;
    Vector2 enemySpawn;
    ECSEngine contextEngine;
    private UnitQueue unitQueue;
    private float addFundsTimer = 0f;
    private boolean spawningUnit;

    private float spawnCooldown = 0f;
    private boolean canSpawnUnits;

    public EntityManager(final GameScreen context, Vector2 alliedSpawn, Vector2 enemySpawn) {
        this.alliedSpawn = alliedSpawn;
        this.enemySpawn = enemySpawn;
        canSpawnUnits = true;
        contextEngine = context.getEcsEngine();
        this.unitQueue = context.getUnitQueue();
        this.spawningUnit = false;

    }

    public static int calculateCost (String slotId) {
        int cost = 100;

        switch (slotId) {
            case "One":
                cost = 10;
                break;
            case "Two":
                cost = 5;
                break;
            case "Three":
                cost = 2;
                break;
        }

        return cost;

    }


    @Override
    public void onSlotClicked(String slotId) {
        System.out.println("clicked");
        int funds = contextEngine.getPlayerCoins();
        int cost = calculateCost(slotId);


        if (unitQueue.size() < 5 && cost <= funds) {
            canSpawnUnits = false;
            unitQueue.enqueue(slotId);
            contextEngine.setPlayerCoins(funds - cost);
        }



    }

    public void update (float delta) {
        if (!canSpawnUnits && spawnCooldown < 2 && !unitQueue.isEmpty()) {
            spawnCooldown+= delta;
        } else if (spawnCooldown >= 2) {
            spawnCooldown = 0f;
            canSpawnUnits = true;
        } else if (canSpawnUnits && !unitQueue.isEmpty()) {
            String unitToSpawn = unitQueue.peek();
            spawnUnit(unitToSpawn);
        }

        if (addFundsTimer < 5) {
            addFundsTimer += delta;
        } else {
            int coins = contextEngine.getPlayerCoins();
            contextEngine.setPlayerCoins(coins + 1);
            addFundsTimer = 0;
            System.out.println("PLayer coins now " + contextEngine.getPlayerCoins());
        }




    }

    public float getSpawnCooldown() {
        return spawnCooldown;
    }

    public UnitQueue getUnitQueue() {
        return unitQueue;
    }

    @Override
    public void spawnUnit (String unitId) {

        switch (unitId) {
            case "One" :
                    contextEngine.createKnight(alliedSpawn, knightSheet, knightSpriteSheet, 1);
                    unitQueue.dequeue();
                break;
            case "Two" :
                    contextEngine.createArcher(alliedSpawn, archerSheet, archerSpriteSheet, 1);
                    unitQueue.dequeue();
                break;
            case "Three" :
                    contextEngine.createPeasant(alliedSpawn, peasantSheet, peasantSpriteSheet, 1);
                    unitQueue.dequeue();
                break;
        }
        canSpawnUnits = false;

    }

}
