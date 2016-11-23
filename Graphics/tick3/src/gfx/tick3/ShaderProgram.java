package gfx.tick3;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


/**
 * Combines vertex and fragment Shaders into a single program that can be used for rendering.
 */
public class ShaderProgram {
    private int program;

    public ShaderProgram(Shader vertex_shader, Shader fragment_shader, String output) {
        program = glCreateProgram();
        glAttachShader(program, vertex_shader.getHandle());
        glAttachShader(program, fragment_shader.getHandle());
        glBindFragDataLocation(program, 0, output);
        glLinkProgram(program);
        glUseProgram(program);
    }

    public int getHandle() {
        return program;
    }
}
