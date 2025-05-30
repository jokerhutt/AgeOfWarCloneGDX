package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class Box2DComponent implements Component, Pool.Poolable {

    public Body body;
    public float width;
    public float height;


    @Override
    public void reset () {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        //equivalent to width = 0; height = 0;
        width = height = 0;

    }




}
