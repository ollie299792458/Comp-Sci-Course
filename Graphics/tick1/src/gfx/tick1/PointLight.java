package gfx.tick1;

public class PointLight {

    private Vector3 position;
    private Vector3 colour;
    private double intensity;

    public PointLight(Vector3 position, Vector3 colour, double intensity) {
        this.position = position;
        this.colour = colour;
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getColour() {
        return colour;
    }

    public double getIntensity() {
        return intensity;
    }

    public Vector3 getIlluminationAt(double distance) {
        return colour.scale(intensity / (Math.PI * 4 * Math.pow(distance, 2)));
    }
}
