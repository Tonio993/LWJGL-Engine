package engine;

import engine.graph.Mesh;
import engine.graph.weather.Fog;
import engine.items.GameItem;
import engine.items.SkyBox;

import java.util.*;

public class Scene {

    private GameItem[] gameItems;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private Map<Mesh, List<GameItem>> meshMap;

    private Fog fog;

    public Scene() {
        this.meshMap = new HashMap<>();
        this.fog = Fog.NOFOG;
    }

    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.computeIfAbsent(mesh, k -> new ArrayList<>());
            list.add(gameItem);
        }
        this.gameItems = gameItems;
    }

    public GameItem[] getGameItems() {
        return gameItems;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

    public Set<Mesh> getMeshes() {
        return this.meshMap.keySet();
    }

    public List<GameItem> getGameItemList(Mesh mesh) {
        return this.meshMap.get(mesh);
    }

    public Fog getFog() {
        return fog;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }
}
