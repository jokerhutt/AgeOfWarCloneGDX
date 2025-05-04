package jokerhut.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.screen.AbstractScreen;
import jokerhut.main.screen.GameScreen;
import jokerhut.main.screen.ScreenType;

import java.util.EnumMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {

    //RENDERING
    private SpriteBatch spriteBatch;
    private EnumMap<ScreenType, AbstractScreen> screenCache;
    private OrthographicCamera gameCamera;
    private FitViewport screenViewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    private AssetManager assetManager;

    private Stage stage;

    private Skin skin;
    private I18NBundle i18NBundle;

    //PHYSICS
    public static final BodyDef BODY_DEF = new BodyDef();
    public static final FixtureDef FIXTURE_DEF = new FixtureDef();

    private World world;

    private ECSEngine ecsEngine;


    //DELTA TIME
    private float accumulator;
    private static final float FIXED_TIME_STEP = 1 / 60f;

    //SCALING
    public static final float UNIT_SCALE = 1 / 32f;

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public EnumMap<ScreenType, AbstractScreen> getScreenCache() {
        return screenCache;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public FitViewport getScreenViewport() {
        return screenViewport;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Stage getStage() {
        return stage;
    }



    @Override
    public void create() {

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        spriteBatch = new SpriteBatch();

        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();


        accumulator = 0;

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
        initializeSkin();
        assetManager.load("map/aoenewmap.tmx", TiledMap.class);
        assetManager.finishLoading();
        stage = new Stage(new FitViewport(1728, 960), spriteBatch);

        gameCamera = new OrthographicCamera();
        screenViewport = new FitViewport(18, 10, gameCamera);

        ecsEngine = new ECSEngine(this);


        setScreen(new GameScreen(this));
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    @Override
    public void render () {

        super.render();
        final float deltaTime = Math.min(0.25f, Gdx.graphics.getRawDeltaTime());
        accumulator += deltaTime;

        while (accumulator >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            accumulator -= FIXED_TIME_STEP;
        }

        stage.getViewport().apply();
        stage.act();
        stage.draw();

    }

    public Box2DDebugRenderer getBox2DDebugRenderer() {
        return box2DDebugRenderer;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void dispose () {
        super.dispose();
        world.dispose();
        assetManager.dispose();
        spriteBatch.dispose();
        box2DDebugRenderer.dispose();
        stage.dispose();
    }

    public static void resetBodyAndFixtureDefinition () {
        BODY_DEF.position.set(0, 0);
        BODY_DEF.gravityScale = 1;
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.fixedRotation = false;

        FIXTURE_DEF.density = 0;
        FIXTURE_DEF.isSensor = false;
        FIXTURE_DEF.restitution = 0;
        FIXTURE_DEF.friction = 0.2f;
        FIXTURE_DEF.filter.categoryBits = 0x0001;
        FIXTURE_DEF.filter.maskBits = -1;
        FIXTURE_DEF.shape = null;

    }

    private void initializeSkin () {

        //setup markup colors
        Colors.put("Red", Color.RED);
        Colors.put("Blue", Color.BLUE);

        //generate ttf bitmaps
        final ObjectMap<String, Object> resources = new ObjectMap<>();
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        final int[] sizesToCreate = {16, 20, 26, 32}; //s m l xl
        for (int size : sizesToCreate) {
            fontParameter.size = size;
            final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getData().markupEnabled = true;
            resources.put("font_" + size, bitmapFont);
        }
        fontGenerator.dispose();

        //load skin
        final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud.atlas", resources);
        assetManager.load("ui/hud.json", Skin.class, skinParameter);
        assetManager.finishLoading();
        skin = assetManager.get("ui/hud.json", Skin.class);


    }

    public Skin getSkin() {
        return skin;
    }
}
