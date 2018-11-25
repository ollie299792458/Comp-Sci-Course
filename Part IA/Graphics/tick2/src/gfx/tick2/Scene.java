package gfx.tick2;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    // A list of 3D objects to be rendered (just spheres for now)
    private List<SceneObject> objects;

    // A list of point light sources
    private List<PointLight> pointLights;

    // The color of the ambient light in the scene
    private Vector3 ambientLight;

    public Scene() {
        objects = new LinkedList<SceneObject>();
        pointLights = new LinkedList<PointLight>();
        ambientLight = new Vector3(1);
    }

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public RaycastHit findClosestIntersection(Ray ray) {

        RaycastHit closestHit = new RaycastHit();

        for (SceneObject object : objects) {
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
        return pointLights.get(0);
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    public void addPointLight(PointLight pointLight) {
        pointLights.add(pointLight);
    }

}
