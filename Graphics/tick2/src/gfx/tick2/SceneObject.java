package gfx.tick2;

public abstract class SceneObject {
    // The diffuse color of the object
    protected Vector3 colour;

    // Coefficients for calculating Phong illumination
    protected double phong_kD, phong_kS, phong_n;

    // How reflective this object is
    protected double reflectivity;

    protected SceneObject() {
        colour = new Vector3(1);
        phong_kD = phong_kS = phong_n = reflectivity = 0;
    }

    public abstract RaycastHit intersectionWith(Ray ray);

    public abstract Vector3 getNormalAt(Vector3 position);

    public Vector3 getColour() {
        return colour;
    }

    public void setColour(Vector3 colour) {
        this.colour = colour;
    }

    public double getPhong_kD() {
        return phong_kD;
    }

    public double getPhong_kS() {
        return phong_kS;
    }

    public double getPhong_n() {
        return phong_n;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }
}
