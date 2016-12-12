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

            int dx = getWidth()/mWorld.getWidth();
            int dy = getHeight()/mWorld.getHeight();
            int dc = dx;
            if (dx > dy) {
                dc = dy;
            }
            for (int i = 0; i < mWorld.getWidth(); i++) {
                for (int j = 0; j< mWorld.getHeight(); j++) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(i*dc + dc/2, j*dc + dc/2, dc, dc);
                    if (mWorld.getCell(i, j)) {
                        g.setColor(Color.BLACK);
                        g.fillRect(i*dc + dc/2, j*dc + dc/2, dc, dc);
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