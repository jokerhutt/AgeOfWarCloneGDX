package jokerhut.main.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable{

    public final ObjectMap <AnimationType, Animation<TextureRegion>> animations = new ObjectMap<>();

    // Currently playing animation
    public Animation<TextureRegion> current = null;

    // Timer for the current animation
    public float stateTime = 0f;

    // Optional: name of the current animation
    public AnimationType currentType = null;

    public void play(AnimationType type) {
        if (type != currentType) {
            current = animations.get(type);
            currentType = type;
            stateTime = 0f;
        }
    }

    @Override
    public void reset() {
        animations.clear();
        current = null;
        currentType = null;
        stateTime = 0f;

    }

}
