package gfx.tick1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tick1 {
    public static final String DEFAULT_INPUT = "basic_scene.xml";
    public static final String DEFAULT_OUTPUT = "output.png";

    // Width and height of the image to be rendered
    private static final int WIDTH_PX = 640;
    private static final int HEIGHT_PX = 480;

    public static void usageError() {
        System.err.println("USAGE: <tick1> --input INPUT --output OUTPUT");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException {
        // We should have an even number of arguments
        if (args.length % 2 != 0)
            usageError();

        //args = new String[]{"--input", "tick1.xml", "--output", "output.png"};

        // Parse the input and output filenames from the arguments
        String input = DEFAULT_INPUT, output = DEFAULT_OUTPUT;
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
            case "-i":
            case "--input":
                input = args[i + 1];
                break;
            case "-o":
            case "--output":
                output = args[i + 1];
                break;
            default:
                System.err.println("Unknown option: " + args[i]);
                usageError();
            }
        }

        Scene scene = new SceneLoader(input).getScene();
        BufferedImage image = new Renderer(WIDTH_PX, HEIGHT_PX).render(scene);
        File save = new File(output);
        ImageIO.write(image, "png", save);
    }
}