package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable{

    public float damage = 0f;
    public AttackType attackType;
    public float range = 1.5f;
    public float attackTimer = 0f;
    public boolean canAttack = true;

    @Override
    public void reset() {
        damage = 0;
    }
}
