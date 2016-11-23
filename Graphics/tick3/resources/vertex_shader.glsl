#version 130

in vec3 position;		// vertex position in local space
in vec3 normal;			// vertex normal in local space

out vec3 frag_normal;	// fragment normal in world space
out float frag_height;
out vec3 cam_dir;

uniform mat4 mvp_matrix; // model-view-projection matrix
uniform vec3 cp_vector;

void main()
{
    // Typicaly normal is transformed by the model matrix
    // Since the model matrix is identity in our case, we do not modify normals
    frag_normal = normal;
    frag_height = position.y;
    cam_dir = position - cp_vector;

    // The position is projected to the screen coordinates using mvp_matrix
	gl_Position = mvp_matrix * vec4(position, 1.0);
}