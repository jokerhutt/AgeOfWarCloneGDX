package jokerhut.main.view;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.entitymanagement.EntityManager;
import jokerhut.main.screen.GameScreen;

public class GameUI extends Table {

    private ProgressBar progressBar;
    private EntityManager entityManager;

    Table buttonsTable;
    Table progressTable;
    Table queueTable;

    public GameUI (final GameScreen gameContext, final Skin skin) {
        super(skin);
        setFillParent(true);
        top(); // Align content to the top of the screen
        left();
        padTop(10f);
        padLeft(10f);

    }

    public void initialiseGameButtons (EntityManager entityManager) {
        this.entityManager = entityManager;
        buttonsTable = new Table(getSkin());
        buttonsTable.add(new ButtonSlot("One", entityManager)).size(64, 64);
        buttonsTable.add(new ButtonSlot("Two", entityManager)).size(64, 64);
        buttonsTable.add(new ButtonSlot("Three", entityManager)).size(64, 64);
        this.add(buttonsTable).row();
    }

    public void initialiseCoins (final ECSEngine ecsEngine) {
        CoinLabel coinLabel = new CoinLabel(getSkin(), ecsEngine);
        this.add(coinLabel).row();

    }

    public void initialiseQueueMarkers () {

        queueTable = new Table(getSkin());

        for (int i = 1; i < 6; i++) {
            queueTable.add(new QueueMarker(i, entityManager)).padRight(4f);
        }

        this.add(queueTable).padBottom(10).row();
        progressTable = new Table(getSkin());
        this.progressBar = new ProgressBar(0, 2, 0.01f, false, getSkin(), "default");
        progressTable.add(progressBar);
        this.add(progressTable).fillX().left();


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (progressBar != null) {
            progressBar.setValue(entityManager.getSpawnCooldown());
        }
    }






}
