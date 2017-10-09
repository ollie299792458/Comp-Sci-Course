package gfx.tick2;

public class Sphere extends SceneObject {

    private final double SPHERE_KD = 0.8;
    private final double SPHERE_KS = 0.8;
    private final double SPHERE_N = 10;
    private final double SPHERE_REFLECTIVITY = 0.3;

    // The world-space position of the sphere
    private Vector3 position;

    // The radius of the sphere in world units
    private double radius;

    public Sphere(Vector3 position, double radius, Vector3 colour) {
        this.position = position;
        this.radius = radius;
        this.colour = colour;

        this.phong_kD = SPHERE_KD;
        this.phong_kS = SPHERE_KS;
        this.phong_n = SPHERE_N;
        this.reflectivity = SPHERE_REFLECTIVITY;
    }

    public RaycastHit intersectionWith(Ray ray) {

        Vector3 O = ray.getOrigin();
        Vector3 D = ray.getDirection();
        Vector3 C = position;
        double r = radius;

        double a = D.dot(D);
        double b = 2 * D.dot(O.subtract(C));
        double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);
        double d = Math.pow(b, 2) - 4 * a * c;

        if (d < 0)
            return new RaycastHit();

        double s1 = (-b + Math.sqrt(d)) / 2 * a;
        double s2 = (-b - Math.sqrt(d)) / 2 * a;

        double s;
        if (s1 > 0 && s1 < s2)
            s = s1;
        else if (s2 > 0)
            s = s2;
        else
            return new RaycastHit();

        Vector3 location = ray.evaluateAt(s);
        Vector3 normal = this.getNormalAt(location);

        return new RaycastHit(this, s, location, normal);
    }

    public Vector3 getNormalAt(Vector3 position) {
        return position.subtract(this.position).normalised();
    }

}
