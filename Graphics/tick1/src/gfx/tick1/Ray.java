package gfx.tick1;

public class Ray {

    private Vector3 origin, direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public Vector3 evaluateAt(double distance) {
        return origin.add(direction.scale(distance));
    }
}
