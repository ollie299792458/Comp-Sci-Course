package gfx.tick1;

public class RaycastHit {

    // The distance the ray travelled before hitting an object
    private double distance;

    // The object that was hit by the ray
    private Sphere objectHit;

    // The location that the ray hit the object
    private Vector3 location;

    // The normal of the object at the location hit by the ray
    private Vector3 normal;

    public RaycastHit() {
        this.distance = Double.POSITIVE_INFINITY;
    }

    public RaycastHit(Sphere objectHit, double distance, Vector3 location, Vector3 normal) {
        this.distance = distance;
        this.objectHit = objectHit;
        this.location = location;
        this.normal = normal;
    }

    public Sphere getObjectHit() {
        return objectHit;
    }

    public Vector3 getLocation() {
        return location;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public double getDistance() {
        return distance;
    }
}
