package uk.ac.cam.olb22.oop.tick5;

import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private World mWorld = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (mWorld != null) {
            //Print generation count
            int gh = 20;
            g.setColor(Color.BLACK);
            g.drawString("Generation: "+String.valueOf(mWorld.getGenerationCount()), 5, getHeight()-5);


            //Print board
            double dx = ((double) getWidth())/((double) mWorld.getWidth());
            double dy = ((double) (getHeight()-gh))/((double) mWorld.getHeight());
            double dc = dx;
            if (dx > dy) {
                dc = dy;
            }
            for (int i = 0; i < mWorld.getWidth(); i++) {
                for (int j = 0; j< mWorld.getHeight(); j++) {
                    g.setColor(Color.LIGHT_GRAY);
                    int x = (int) Math.floor(i*dc);
                    int y = (int) Math.floor(j*dc);
                    int w = (int) Math.floor(dc);
                    g.drawRect(x, y, w, w);
                    if (mWorld.getCell(i, j)) {
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, w, w);
                    }
                }
            }
        }
        /*
        // Sample drawing statements
        g.setColor(Color.BLACK);
        g.drawRect(200, 200, 30, 30);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(140, 140, 30, 30);
        g.fillRect(260, 140, 30, 30);
        g.setColor(Color.BLACK);
        g.drawLine(150, 300, 280, 300);
        g.drawString("@@@", 135, 120);
        g.drawString("@@@", 255, 120);
        */
    }

    public void display(World w) {
        mWorld = w;
        repaint();
    }
}