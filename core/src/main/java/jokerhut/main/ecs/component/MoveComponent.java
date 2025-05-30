package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class MoveComponent implements Component, Pool.Poolable{

    public final Vector2 velocity = new Vector2();
    public boolean moving = true;
    public float yClamp = 1f;

    @Override
    public void reset() {
        velocity.set(0, 0);
        moving = true;
    }

}
