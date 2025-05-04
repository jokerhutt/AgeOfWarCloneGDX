package jokerhut.main.entitymanagement;

import com.badlogic.gdx.math.Vector2;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.screen.GameScreen;

import static jokerhut.main.entitymanagement.EntityManager.calculateCost;
import static jokerhut.main.screen.GameScreen.*;

public class OpponentAiManager implements UnitSpawner {

    private GameScreen gameContext;
    private ECSEngine contextEngine;
    Vector2 alliedSpawn;
    Vector2 enemySpawn;
    private UnitQueue unitQueue;
    private float spawnCooldown = 0f;
    private boolean canSpawnUnits;
    private float addFundsTimer = 0f;

    public OpponentAiManager (final GameScreen gameContext, Vector2 alliedSpawn, Vector2 enemySpawn) {
        this.alliedSpawn = alliedSpawn;
        this.enemySpawn = enemySpawn;
        this.gameContext = gameContext;
        canSpawnUnits = false;
        this.contextEngine = gameContext.getEcsEngine();
        this.unitQueue = gameContext.getEnemyQueue();

    }

    public void update (float delta) {
        if (!canSpawnUnits && spawnCooldown < 1) {
            spawnCooldown+= delta;
        } else if (spawnCooldown >= 1) {
            spawnCooldown = 0f;
            canSpawnUnits = true;
        } else if (canSpawnUnits && unitQueue.size() < 5) {
            int unitType = (int)(Math.random() * 3);

            String unitToSpawn;

            switch (unitType) {
                case 1:
                    unitToSpawn = "One";
                    break;
                case 2:
                    unitToSpawn = "Two";
                    break;
                case 3:
                    unitToSpawn = "Three";
                    break;
                default:
                    unitToSpawn = "One";
                    break;
            }

            int cost = calculateCost(unitToSpawn);
            int funds = contextEngine.getOpponentCoins();

            if (funds >= cost) {
                unitQueue.enqueue(unitToSpawn);
                contextEngine.setOpponentCoins(funds - cost);
                canSpawnUnits = false;
            }




        }

        if (canSpawnUnits && !unitQueue.isEmpty()) {
            String unitToSpawn = unitQueue.peek();
            spawnUnit(unitToSpawn);
        }

        if (addFundsTimer < 5) {
            addFundsTimer += delta;
        } else {
            int coins = contextEngine.getOpponentCoins();
            contextEngine.setOpponentCoins(coins + 1);
            addFundsTimer = 0;
            System.out.println("Enemy coins now " + contextEngine.getOpponentCoins());

        }

    }

    @Override
    public void spawnUnit (String unitId) {


        switch (unitId) {
            case "One" :
                    contextEngine.createKnight(enemySpawn, enemyKnightSheet, enemyKnightSpriteSheet, 0);
                    unitQueue.dequeue();
                break;
            case "Two" :
                    contextEngine.createArcher(enemySpawn, enemyArcherSheet, enemyArcherSpriteSheet, 0);
                    unitQueue.dequeue();

                break;
            case "Three" :
                contextEngine.createPeasant(enemySpawn, enemyPeasantSheet, enemyPeasantSpriteSheet, 0);
                    unitQueue.dequeue();
                break;
        }

        canSpawnUnits = false;

    }


}
