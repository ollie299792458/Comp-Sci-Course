package gfx.tick2;

public class Vector3 {
    public final double x, y, z;

    public Vector3(double uniform) {
        this(uniform, uniform, uniform);
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private static int convertToByte(double value) {
        return (int) (255 * Math.max(0, Math.min(1, value)));
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 scale(double scalar) {
        return new Vector3(scalar * x, scalar * y, scalar * z);
    }

    // Hadamard product, scales the vector in an element-wise fashion
    public Vector3 scale(Vector3 other) {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }

    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalised() {
        double magnitude = this.magnitude();
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    public int toRGB() {
        return convertToByte(x) << 16 | convertToByte(y) << 8 | convertToByte(z) << 0;
    }

    // Calculate mirror-like reflection
    public Vector3 reflectIn(Vector3 N) {
        return N.scale(2 * this.dot(N)).subtract(this);
    }

    // Creates a random vector inside the unit sphere
    public static Vector3 randomInsideUnitSphere() {

        double r = Math.random();
        double theta = Math.random() * Math.PI;
        double phi = Math.random() * Math.PI * 2;

        double x = r * Math.sin(theta) * Math.cos(phi);
        double y = r * Math.sin(theta) * Math.sin(phi);
        double z = r * Math.cos(theta);

        return new Vector3(x, y, z);
    }

    public boolean equals(Vector3 other) {
        return x == other.x && y == other.y && z == other.z;
    }
}
