package jokerhut.main.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import jokerhut.main.screen.ButtonSlotListener;

import static jokerhut.main.screen.GameScreen.*;

public class ButtonSlot extends Stack{

    Image background;
    Image itemIcon;
    TextureRegion iconRegion;
    TextureRegion sheet;


    public ButtonSlot (String id, ButtonSlotListener listener) {

        this.setSize(64, 64);

        switch (id) {
            case "One":
                sheet = new TextureRegion(knightSpriteSheet, 0, 0, 192, 192);
                break;
            case "Two":
                sheet = new TextureRegion(archerSpriteSheet, 0, 0, 192, 192);
                break;
            case "Three":
                sheet = new TextureRegion(peasantSpriteSheet, 0, 0, 192, 192);
                break;
        }


        Texture backgroundTexture = new Texture(Gdx.files.internal("ui/redButton.png"));
        Texture itemTexture = new Texture(Gdx.files.internal("ui/icon" + id + ".png"));

        background = new Image(new TextureRegionDrawable(backgroundTexture));
        itemIcon = new Image(new TextureRegionDrawable(sheet));
        itemIcon.setAlign(Align.center);

        this.add(background);
        this.add(itemIcon);

        this.addListener(new ClickListener() {
            @Override
                public void clicked (InputEvent event, float x, float y) {
                System.out.println("crick");
                listener.onSlotClicked(id);
            }
        });

    }



}
