package jokerhut.main.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import jokerhut.main.MainGame;

public abstract class AbstractScreen <T extends Table> implements Screen {

    protected final MainGame context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer box2DDebugRenderer;
    protected final Stage stage;
    protected final T screenUI;
    protected final SpriteBatch spriteBatch;

    public AbstractScreen (final MainGame context) {

        this.context = context;
        viewport = context.getScreenViewport();
        this.world = context.getWorld();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();
        stage = context.getStage();
        screenUI = getScreenUI(context);
        spriteBatch = context.getSpriteBatch();

    }

    protected abstract T getScreenUI (final MainGame context);


    @Override
    public void show() {
        stage.addActor(screenUI);
    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize (final int width, final int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.getRoot().removeActor(screenUI);
    }

    @Override
    public void dispose() {

    }
}
