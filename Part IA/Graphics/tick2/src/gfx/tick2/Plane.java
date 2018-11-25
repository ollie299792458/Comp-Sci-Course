package gfx.tick2;

public class Plane extends SceneObject
{
	private final double PLANE_KD = 0.9;
	private final double PLANE_KS = 0.0;
	private final double PLANE_N = 0.0;
	private final double PLANE_REFLECTIVITY = 0.1;

	// A point in the plane
	private Vector3 point;

	// The normal of the plane
	private Vector3 normal;

	public Plane(Vector3 point, Vector3 normal, Vector3 colour)
	{
		this.point = point;
		this.normal = normal;
		this.colour = colour;

		this.phong_kD = PLANE_KD;
		this.phong_kS = PLANE_KS;
		this.phong_n = PLANE_N;
		this.reflectivity = PLANE_REFLECTIVITY;
	}

	public RaycastHit intersectionWith(Ray ray)
	{
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		Vector3 Q = this.point;
		Vector3 N = this.normal;

		// DONE: Calculate ray parameter s at intersection
        Vector3 QtakeO = Q.subtract(O);
		double top = QtakeO.dot(N);
        double bottom = D.dot(N);
        double s = top/bottom;

		// DONE: If s < 0, return empty RaycastHit, otherwise return RaycastHit describing point of intersection
        RaycastHit result;
        if (s<0) {
            result = new RaycastHit();
        } else {
            result = new RaycastHit(this, s, ray.evaluateAt(s), N);
        }

		return result;
	}

	public Vector3 getNormalAt(Vector3 position)
	{
		return normal;
	}
}
