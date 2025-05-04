package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TeamComponent implements Component, Pool.Poolable {

    public int teamType;
    public void reset () {

    }

}
