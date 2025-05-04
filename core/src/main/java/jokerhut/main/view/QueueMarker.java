package jokerhut.main.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import jokerhut.main.entitymanagement.EntityManager;
import jokerhut.main.entitymanagement.UnitQueue;

public class QueueMarker extends Image {

    private int index;
    private boolean isFilled;

    TextureRegionDrawable filledImage;
    TextureRegionDrawable emptyImage;

    UnitQueue spawnQueue;

    public QueueMarker (int index, EntityManager entityManager) {


        this.setSize(64, 64);
        this.index = index;

        Texture filledImageTex = new Texture(Gdx.files.internal("ui/queueFilled.png"));
        Texture emptyImageTex = new Texture(Gdx.files.internal("ui/queueEmpty.png"));

        filledImage = new TextureRegionDrawable(filledImageTex);
        emptyImage = new TextureRegionDrawable(emptyImageTex);

        this.setDrawable(emptyImage);

        spawnQueue = entityManager.getUnitQueue();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (spawnQueue.size() >= index) {
            System.out.println("SPAWN QUEUE SIZE: " + spawnQueue.size() + "INDEX AT: " + index);
            this.setDrawable(filledImage);
        } else {
            this.setDrawable(emptyImage);
        }
    }



}
