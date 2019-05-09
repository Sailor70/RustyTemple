package RustyTemple;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Sailor
 */
public class Predator {

    private final int IMG_SIZE = 16;
    private final int SHIFT = 8;
    private final int CELL_SIZE = 32;
    private BufferedImage biF;
    public Graphics2D g2dF;
    private Random rnd = new Random();
    private Point p, cellPoint;
    public ImageIcon image;
    private Cell[][] world;
    private int dirr;
    private int prevGoodDir = -1;
    private int noGo = -1;
    private int toMove;
    private final int FREQUENCY = 2;

    public Predator(Cell[][] cc) {
        this.world = cc;
        image = new ImageIcon(new ImageIcon(getClass().getResource("/RT/resources/fox.png")).getImage());
        this.setPosition();
        dirr = rnd.nextInt(4);
        this.toMove = 0;

    }

    public Image getGraphics() {
        return image.getImage();
    }

    private void setPosition() {
        int x, y, xPx, yPx;
        do {
            x = rnd.nextInt(20); //los 0-19 
            y = rnd.nextInt(20); //los 0-19 
            xPx = ((x + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
            yPx = ((y + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        } while (world[x][y].ocupated() == true || world[x][y].getFeeder() == true);
        cellPoint = new Point(x, y);
        p = new Point(xPx, yPx);
        world[x][y].visit();
        world[x][y].setPredator(true);
    }

    public Point getPosition() {
        return p;
    }

    public void move() {
        if (toMove == 0) {
            int x, y, xS, yS;
            x = this.cellPoint.x;
            y = this.cellPoint.y;
            xS = x;
            yS = y;
            world[x][y].leave();
            world[x][y].setPredator(false);
            boolean pos = false;
            boolean noGoException;
            do {
                noGoException = true;
                switch (dirr) {

                    case 0: //left
                        if (world[x][y].isLeft() == false && x >= 1) {
                            x -= 1;
                            dirr = 0;
                            prevGoodDir = 0;
                            getNoGo();
                        } else {
                            do {
                                dirr = rnd.nextInt(4);
                                if (dirr != noGo) {
                                    noGoException = false;
                                }

                            } while (noGoException);

                        }
                        break;
                    case 1: //top
                        if (world[x][y].isTop() == false && y >= 1) {
                            y -= 1;
                            dirr = 1;
                            prevGoodDir = 1;
                            getNoGo();
                        } else {
                            do {
                                dirr = rnd.nextInt(4);
                                if (dirr != noGo) {
                                    noGoException = false;
                                }
                            } while (noGoException);
                        }
                        break;
                    case 2:  //right
                        if (world[x][y].isRight() == false && x <= 18) {
                            x += 1;
                            dirr = 2;
                            prevGoodDir = 2;
                            getNoGo();
                        } else {
                            do {
                                dirr = rnd.nextInt(4);
                                if (dirr != noGo) {
                                    noGoException = false;
                                }
                            } while (noGoException);
                        }
                        break;
                    case 3: //bottom
                        if (world[x][y].isBottom() == false && y <= 18) {
                            y += 1;
                            dirr = 3;
                            prevGoodDir = 3;
                            getNoGo();
                        } else {
                            do {
                                dirr = rnd.nextInt(4);
                                if (dirr != noGo) {
                                    noGoException = false;
                                }
                            } while (noGoException);
                        }
                        break;
                }
                if (world[x][y].ocupated() == false && (x != xS || y != yS)) {
                    pos = true;
                    cellPoint.x = x;
                    cellPoint.y = y;
                    world[x][y].ocupated();
                    world[x][y].setPredator(true);
                    p.x = ((x + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
                    p.y = ((y + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
                } else {
                    x = xS; //jeśli się nie udało ruszyć to ustawiamy na wartości początkowe i jeszcze raz while
                    y = yS;

                    do {
                        dirr = rnd.nextInt(4);
                        if (dirr != noGo) {
                            noGoException = false;
                        }
                    } while (noGoException);
                }
            } while (!pos);
        }
        if (toMove == FREQUENCY) {
            toMove = 0;
        } else {
            toMove++;
        }
    }

    private void getNoGo() {

        switch (prevGoodDir) {
            case 0:
                noGo = 2;
                break;
            case 1:
                noGo = 3;
                break;
            case 2:
                noGo = 0;
                break;
            case 3:
                noGo = 1;
                break;
        }
    }

//    public void draw(Graphics2D g)
//    {
//        g.drawImage(getActualFrame(), this.x, this.y, null);
//    }
}
