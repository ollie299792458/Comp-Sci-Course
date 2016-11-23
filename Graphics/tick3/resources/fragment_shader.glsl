#version 130

in vec3 frag_normal;	// fragment normal in world space
in float frag_height;
in vec3 cam_dir;

out vec3 colour;

void main()
{
	const vec3 I_a = vec3(0.2, 0.2, 0.2);       // Ambient light intensity (and colour)

	const float k_d = 0.8;                      // Diffuse light factor
	const float k_s = 0.5;

    // Diffuse light colour
    vec3 C_diff;

    vec3 C_spec = vec3(0.99);
    vec3 spec_comp;

    float n = 2;

    if (frag_height > 1.5) {
        C_diff = vec3(0.9);
        spec_comp = C_spec*k_s*I_a*pow(max(dot(frag_normal,cam_dir), 0),n);
    } else {
        C_diff = vec3(0.560, 0.525, 0.478);
        spec_comp = vec3(0);
    }

	const vec3 I = vec3(0.941, 0.968, 1);   // Light intensity (and colour)
	vec3 L = normalize(vec3(2, 1.5, -0.5)); // The light direction as a unit vector
	vec3 N = frag_normal;                   // Normal in world coordinates

    // DONE: calculate reflected colour instead of abs(frag_normal) below
    colour = spec_comp + C_diff*I_a+C_diff*k_d*I*max(dot(N,L), 0);

}