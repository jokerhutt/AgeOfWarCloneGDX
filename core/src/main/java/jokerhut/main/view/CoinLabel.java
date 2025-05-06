package jokerhut.main.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import jokerhut.main.ecs.ECSEngine;

public class CoinLabel extends Label {

    private int coins;
    private ECSEngine contextEngine;

    public CoinLabel(Skin skin, ECSEngine contextEngine) {
        super("Coins: 0", skin, "default");
        this.coins = contextEngine.getPlayerCoins();
        this.contextEngine = contextEngine;
    }


    @Override
    public void act (float delta) {
        super.act(delta);
        this.coins = contextEngine.getPlayerCoins();
        setText("Coins: " + coins);
    }


}
