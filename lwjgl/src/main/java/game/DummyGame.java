package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.*;
import engine.graph.light.DirectionalLight;
import engine.graph.light.PointLight;
import engine.graph.light.SceneLight;
import engine.graph.light.SpotLight;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private Camera camera;

    private GameItem[] gameItems;

    private SceneLight sceneLight;

    private Hud hud;

    private float lightAngle;

    private float spotAngle = 0;

    private float spotInc = 1;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1f;

//        Mesh mesh = OBJLoader.loadMesh("assets/models/bunny.obj");
//        Material material = new Material(new Vector4f(0.0f, 0.6f, 0.7f, 1.0f), reflectance);
        Mesh mesh = OBJLoader.loadMesh("assets/models/cube.obj");
        Texture texture = new Texture("assets/textures/block.png");
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[]{gameItem};

        sceneLight = new SceneLight();

        // Ambient light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        // Point Light
        Vector3f pointLightColor = new Vector3f(1, 1, 1);
        Vector3f pointLightPosition = new Vector3f(0, 0, 0);
        float pointLightIntensity = 1.0f;
        PointLight pointLight1 = new PointLight(pointLightColor, pointLightPosition, pointLightIntensity);
        PointLight.Attenuation pointLightAttenuation = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight1.setAttenuation(pointLightAttenuation);
        sceneLight.setPointLightList(new PointLight[]{ pointLight1 });

        // Spot Light
        Vector3f spotLightPosition = new Vector3f(0, 0, 10);
        float spotLightIntensity = 1.0f;
        PointLight spotLightPL = new PointLight(new Vector3f(1, 1, 1), spotLightPosition, spotLightIntensity);
        PointLight.Attenuation spotLightAttenuation = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        spotLightPL.setAttenuation(spotLightAttenuation);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight1 = new SpotLight(spotLightPL, coneDir, cutoff);

        spotLightPosition = new Vector3f(0, 0, 10);
        spotLightIntensity = 1.0f;
        spotLightPL = new PointLight(new Vector3f(1, 1, 1), spotLightPosition, spotLightIntensity);
        spotLightAttenuation = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        spotLightPL.setAttenuation(spotLightAttenuation);
        coneDir = new Vector3f(0, 0.005f, -1);
        cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight2 = new SpotLight(spotLightPL, coneDir, cutoff);

        sceneLight.setSpotLightList(new SpotLight[]{ spotLight1, spotLight2 });

        // Directional Light
        Vector3f directionalLightColor = new Vector3f(1, 1, 1);
        Vector3f directionalLightDirection = new Vector3f(1, 0, 0);
        float directionalLightIntensity = 1.0f;
        sceneLight.setDirectionalLight(new DirectionalLight(directionalLightColor, directionalLightDirection, directionalLightIntensity));

        hud = new Hud("DEMO");
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);

        if (window.isKeyPressed(GLFW_KEY_W)) {
           cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
           cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
           cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
           cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
           cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
           cameraInc.y = 1;
        }
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        float lightPos = spotLightList[0].getPointLight().getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            spotLightList[0].getPointLight().getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(
                cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }

        // Update spot light direction
        spotAngle += spotInc * 0.05f;
        if (spotAngle > 2) {
            spotInc = -1;
        } else if (spotAngle < -2) {
            spotInc = 1;
        }
        double spotAngleRad = Math.toRadians(spotAngle);
        SpotLight[] spotLights = sceneLight.getSpotLightList();
        Vector3f coneDir = spotLights[0].getConeDirection();
        coneDir.y = (float) Math.sin(spotAngleRad);

        // Update directional light direction, intensity and color
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 1.1f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, gameItems, sceneLight, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
        hud.cleanup();
    }
}
