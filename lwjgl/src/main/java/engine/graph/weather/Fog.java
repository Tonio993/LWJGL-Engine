package engine.graph.weather;

import org.joml.Vector3f;

public class Fog {

    private boolean activeFog;

    private Vector3f color;

    private float density;

    public static Fog NOFOG = new Fog();

    public Fog() {
        activeFog = false;
        this.color = new Vector3f(0, 0, 0);
        this.density = 0;
    }

    public Fog(boolean activeFog, Vector3f color, float density) {
        this.color = color;
        this.density = density;
        this.activeFog = activeFog;
    }

    public boolean isActiveFog() {
        return activeFog;
    }

    public void setActiveFog(boolean activeFog) {
        this.activeFog = activeFog;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

}