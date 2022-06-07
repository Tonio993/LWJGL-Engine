package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.graph.Mesh;
import engine.Renderer;
import engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.system.MemoryUtil.memFree;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    private GameItem[] gameItems;


    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float[] positions = {
                -0.5f,  0.5f, -1.05f,
                -0.5f, -0.5f, -1.05f,
                0.5f, -0.5f, -1.05f,
                0.5f,  0.5f, -1.05f
        };

        float[] colors = {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        int[] index = {0, 1, 3, 3, 1, 2};

        Mesh mesh = new Mesh(positions, colors, index);

        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0.2f, 0f, 0f);
//        gameItem.setScale(0.5f);

        gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(double interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0f);
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
