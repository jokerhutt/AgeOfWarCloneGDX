package jokerhut.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.MainGame;
import jokerhut.main.ecs.ECSEngine;
import jokerhut.main.entitymanagement.EntityManager;
import jokerhut.main.entitymanagement.OpponentAiManager;
import jokerhut.main.entitymanagement.UnitQueue;
import jokerhut.main.map.GameMap;
import jokerhut.main.view.GameUI;

import static jokerhut.main.MainGame.UNIT_SCALE;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen extends AbstractScreen<GameUI> {

    private final AssetManager assetManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;
    private GameMap map;
    private Texture castleTexture;
    private ECSEngine ecsEngine;
    private EntityManager entityManager;

    private UnitQueue unitQueue;
    private UnitQueue enemyQueue;

    public static final Texture knightSpriteSheet = new Texture("character/knightSheet.png");
    public static final Texture archerSpriteSheet = new Texture("character/archerSheet.png");
    public static final Texture peasantSpriteSheet = new Texture("character/peasantSheet.png");
    public static final Texture enemyKnightSpriteSheet = new Texture("character/enemyKnightSheet.png");
    public static final Texture enemyArcherSpriteSheet = new Texture("character/enemyArcherSheet.png");
    public static final Texture enemyPeasantSpriteSheet = new Texture("character/enemyPeasantSheet.png");


    public static final TextureRegion knightSheet = new TextureRegion(knightSpriteSheet, 0, 0, 192, 192);
    public static final TextureRegion archerSheet = new TextureRegion(archerSpriteSheet, 0, 0, 192, 192);
    public static final TextureRegion peasantSheet = new TextureRegion(peasantSpriteSheet, 0, 0, 192, 192);
    public static final TextureRegion enemyKnightSheet = new TextureRegion(enemyKnightSpriteSheet, 0, 0, 192, 192);
    public static final TextureRegion enemyArcherSheet = new TextureRegion(enemyArcherSpriteSheet, 0, 0, 192, 192);
    public static final TextureRegion enemyPeasantSheet = new TextureRegion(enemyPeasantSpriteSheet, 0, 0, 192, 192);


    private OpponentAiManager opponentAiManager;

    public GameScreen(final MainGame context) {
        super(context);

        this.gameCamera = context.getGameCamera();
        this.assetManager = context.getAssetManager();
        castleTexture = new Texture(Gdx.files.internal("map/castle.png"));
        this.profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        this.mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        final TiledMap tiledMap = assetManager.get("map/aoenewmap.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        this.map = new GameMap(tiledMap);

        ecsEngine = context.getEcsEngine();
        unitQueue = new UnitQueue();
        enemyQueue = new UnitQueue();
        entityManager = new EntityManager(this, map.getAlliedStartLocation(), map.getEnemyStartLocation());
        opponentAiManager = new OpponentAiManager(this, map.getAlliedStartLocation(), map.getEnemyStartLocation());

        gameCamera.position.set(9, 5, 0); // Adjust to your map's center in world units
        gameCamera.update();

        Gdx.input.setInputProcessor(context.getStage());

        screenUI.initialiseGameButtons(entityManager);
        screenUI.initialiseQueueMarkers();

    }

    public UnitQueue getEnemyQueue() {
        return enemyQueue;
    }

    public UnitQueue getUnitQueue() {
        return unitQueue;
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    @Override
    protected GameUI getScreenUI(MainGame context) {
        return new GameUI(this, context.getSkin());
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);
        mapRenderer.setView(gameCamera);
        entityManager.update(delta);
        opponentAiManager.update(delta);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(gameCamera.combined);
        spriteBatch.begin();
        renderCastles();
        ecsEngine.update(delta);
        spriteBatch.end();
        profiler.reset();
    }

    public void renderCastles () {
        Vector2 pos = map.getAlliedCastlePosition();
        Vector2 size = map.getAlliedCastleSize();
        spriteBatch.draw(castleTexture, pos.x, pos.y, size.x, size.y);
        pos = map.getEnemyCastlePosition();
        size = map.getEnemyCastleSize();
        spriteBatch.draw(castleTexture, pos.x, pos.y, size.x, size.y);

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        castleTexture.dispose();
    }



}
