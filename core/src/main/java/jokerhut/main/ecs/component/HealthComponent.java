package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable{

    public float health = 0f;
    public float deathTimer = 0f;

    @Override
    public void reset() {
        health = 0;
    }

    public void takeDamage (float damage) {
        this.health -= damage;
    }

}
