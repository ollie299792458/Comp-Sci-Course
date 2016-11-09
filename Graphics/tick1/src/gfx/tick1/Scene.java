package gfx.tick1;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    // A list of 3D objects to be rendered (just spheres for now)
    private List<Sphere> objects;

    // A point light source to provide illumination
    private PointLight pointLight;

    // The color of the ambient light in the scene
    private Vector3 ambientLight;

    public Scene() {
        objects = new LinkedList<Sphere>();
        ambientLight = new Vector3(1);
    }

    public void addObject(Sphere sphere) {
        objects.add(sphere);
    }

    public RaycastHit findClosestIntersection(Ray ray) {

        RaycastHit closestHit = new RaycastHit();

        for (Sphere object : objects) {
            RaycastHit trialHit = object.intersectionWith(ray);
            if (trialHit.getDistance() < closestHit.getDistance()) {
                closestHit = trialHit;
            }
        }

        return closestHit;
    }

    public Vector3 getAmbientLighting() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3 ambientLight) {
        this.ambientLight = ambientLight;
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

}
