package RustyTemple;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

// klasa będzie sterować kolejnymi klatkami animacji
public class TempleTimer extends JComponent implements ActionListener { //world map

    private BufferedImage map;
    Timer timer;
    private Cell[][] world;
    private MatrixR mr;
    private int cellSize;
    private JPanel panel;
    private int predators, feeders;
    private Predator[] pred;
    private Feeder[] feed;
    private Prey[] prey;
    private TempleMain tMain = new TempleMain();
    private int[] times = new int[3];
    private int[] firstTimes = new int[3];
    Graphics2D g2d;
    int iteration = 1;
    private boolean stopSequence;

    private Random rnd = new Random();
    private final int IMG_SIZE = 16;
    private final int SHIFT = 8;
    private final int CELL_SIZE = 32;
    private int TIME;
    private Point p;
    int x;
    int y;
    private int printQonce = 0;
    private int clock = 0;

    TempleTimer(CellCreator cc, MapDrawer md, int cellSize, JPanel jPanel1, int predators, int feeders, int TIME) {
        this.world = cc.getWorld();
        map = md.getMap();
        //this.cellSize = cellSize;
        this.predators = predators;
        this.feeders = feeders;
        this.panel = jPanel1;
        this.TIME = TIME;
        this.stopSequence = false;
        this.setPFObjects();
        this.mr = new MatrixR(cc);
        setPrey();
//        for (int i = 0; i < 3; i++) {
//            prey[i].setQ(getFeeders(1));
//        }
        setBounds(0, 0, panel.getWidth(), panel.getHeight());
//      Timer timer = new Timer(100,this);
//      timer.start();

        this.repaint();
    }

    private void setPFObjects() {
        if (feeders > 0) {
            feed = new Feeder[feeders];
            for (int i = 0; i < feeders; i++) {
                feed[i] = new Feeder(world);
            }
        }

        if (predators > 0) {
            pred = new Predator[predators];
            for (int i = 0; i < predators; i++) {
                pred[i] = new Predator(world);
            }
        }
    }

    private void setPrey() {

        prey = new Prey[3];
        for (int i = 0; i < 3; i++) {
            prey[i] = new Prey(world, TIME, mr, feed, feeders);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.drawImage(map, 0, 0, null);
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        if (stopSequence) {
            g2d.drawString("Iteracja nr: " + iteration + " (last one)", 10, 20);
        } else {
            g2d.drawString("Iteracja nr: " + iteration, 10, 20);
        }
        for (int i = 0; i < 3; i++) {

            if (!prey[i].isDead()) {
                if (iteration < 50) { // if ((iteration % 2 == 1 || prey[i].explore) && iteration<50 )
                    prey[i].calculateQ();
                    System.out.print("Prey " + i + " rand |||");
                } else if (clock < 70) {
                    prey[i].exploitation();
                    System.out.print("Prey " + i + " QmoveQ ||");
                } else {
                    prey[i].calculateQ();
                    System.out.print("Prey " + i + " rand |||");
                }
                g.drawImage(prey[i].getGraphics(), prey[i].getPosition().x, prey[i].getPosition().y, null);
            }
            if (!prey[i].isDead() && prey[i].getHp() < 1) {
                times[i] = prey[i].getTime();
                if (iteration == 1) {
                    firstTimes[i] = prey[i].getTime();
                }
                prey[i].killPrey();

            }
            if (!stopSequence) {
                if (iteration >= 1 && prey[0].isDead() && prey[1].isDead() && prey[2].isDead()) {
                    for (int j = 0; j < 3; j++) {
                        prey[j].restart();
                    }
                    nextIteration();
                }
            } else {
                if (prey[0].isDead() && prey[1].isDead() && prey[2].isDead()) { //jeśli true to wszyscy już nie żyja 
                    timer.stop();
                    tMain.allDead(times, firstTimes);
                }

            }

        }

        for (int i = 0; i < predators; i++) {
            g.drawImage(pred[i].getGraphics(), pred[i].getPosition().x, pred[i].getPosition().y, null);
        }
        for (int i = 0; i < feeders; i++) {
            if (!feed[i].empty) { //jeśli feeser został zebrany to nie rysujemy go
                g.drawImage(feed[i].getGraphics(), feed[i].getPosition().x, feed[i].getPosition().y, null);
            }
        }
        System.out.print("\n");

    }

    public int getTIME() {
        return TIME;
    }

    public void timerStart() {
        timer = new Timer(TIME, this);
        timer.start();
    }

    public void nextIteration() {
        iteration++;
        System.out.println("Clock: "+clock);
        this.clock = 0;
    }

    public int getIteration() {
        return iteration;
    }

    public void setStop() {
        stopSequence = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
        if (iteration == 50 && printQonce < 3) {
            for (int i = 0; i < 3; i++) {
                prey[i].printQ();
                printQonce++;
            }
        }
        if (iteration > 50) {
            clock++;
        }
    }
}
