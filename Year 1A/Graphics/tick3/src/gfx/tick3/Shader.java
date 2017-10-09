package gfx.tick3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/**
 * Shader class loads and compiles shaders so they can be attached to a ShaderProgram.
 */
public class Shader {
    private int shader;

    public Shader(int type, String filename) {
        // Read the shader source code from file
        String shaderSource = null;
        try {
            List<String> shaderSourceLines = Files.readAllLines(Paths.get(filename));
            shaderSource = String.join("\n", shaderSourceLines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader: " + filename);
        }

        // Create and compile the shader
        shader = glCreateShader(type);
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);

        // Check in case there was an error during compilation
        String error = glGetShaderInfoLog(shader);
        if (!error.isEmpty()) {
            System.out.println(error);
            throw new RuntimeException("shader compilation failed: consult the log above");
        }
    }

    public int getHandle() {
        return shader;
    }
}
