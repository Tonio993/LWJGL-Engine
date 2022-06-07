package engine;

import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Transformation;
import org.joml.Matrix4f;
import utils.Files;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000f;

    private ShaderProgram shaderProgram;
    private Matrix4f projectionMatrix;
    private Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Files.read("shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Files.read("shaders/fragment.fs"));
        shaderProgram.link();

        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each gameItem
        for(GameItem gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale()
            );
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            // Render the mesh for this game item
            gameItem.getMesh().render();
        }

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

}
