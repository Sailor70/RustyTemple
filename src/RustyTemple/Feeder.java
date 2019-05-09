/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustyTemple;

import java.awt.Image;
import java.awt.Point;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Sailor
 */
public class Feeder {

    private final int IMG_SIZE = 16;
    private final int SHIFT = 8;
    private final int CELL_SIZE = 32;
    private final int Q_SIZE = 400;
    int xPx;
    int yPx;
    private int xCell, yCell;
    private Random rnd = new Random();
    private Point p;
    public ImageIcon image;
    private Cell[][] world;
    public boolean empty = false;

    public Feeder(Cell[][] world) {
        this.world = world;
        image = new ImageIcon(new ImageIcon(getClass().getResource("/RT/resources/seed.png")).getImage());
        this.setPosition();
    }

    public Image getGraphics() {
        return image.getImage();
    }

    public void setEmpty(boolean emp) {
        this.empty = emp;
    }

    private void setPosition() {
        int x, y;
        do {
            xCell = rnd.nextInt(20) + 1; //los 1-20 
            yCell = rnd.nextInt(20) + 1; //los 1-20 
            xPx = (xCell * CELL_SIZE) - IMG_SIZE - SHIFT;
            yPx = (yCell * CELL_SIZE) - IMG_SIZE - SHIFT;
        } while (world[xCell - 1][yCell - 1].ocupated() == true);
        p = new Point(xPx, yPx);
        world[xCell - 1][yCell - 1].setFeeder(true);
    }

    public void setPositionAgain() {
        world[xCell-1][yCell-1].setFeeder(true);
    }

    public Point getPosition() {
        return p;
    }
}
