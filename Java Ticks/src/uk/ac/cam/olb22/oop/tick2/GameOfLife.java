package uk.ac.cam.olb22.oop.tick2;

/**
 * Created by oliver on 14/11/16.
 */
public class GameOfLife {

    private World mWorld;

    public GameOfLife(World w) {
        mWorld = w;
    }

    public void play() throws IOException {
        //TODO
    }

    public void print() {
        // TODO
    }

    public static void main(String args[]) throws IOException {

        try {
            World w=null;

            // TODO: initialise w as an ArrayWorld or a PackedWorld
            // based on the command line input

            GameOfLife gol = new GameOfLife(w);
            gol.play();
        }
        catch(PatternFormatException e) {
            System.out.println(e.getMessage());
        }
    }


}
