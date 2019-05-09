package RustyTemple;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Sailor
 */
public class Prey {

    private final int IMG_SIZE = 16;
    private final int SHIFT = 8;
    private final int CELL_SIZE = 32;
    private final int TIME;
    private final int MAZE_SIZE = 20; //20
    private BufferedImage biF;
    public Graphics2D g2dF;
    private Random rnd = new Random();
    private Point p, cellPoint;
    public ImageIcon image;
    private Cell[][] world;
    private Feeder[] feed;
    private int feeders;
    private int dirr;
    private int prevGoodDir = -1;
    private int noGo = -1;
    private int hp = 0; //feeder dodaje, Predator zabiera, samoczynnie obniża się wraz z upływem czasu
    private int timeCounter = 0;
    private boolean death;
    private int firstX, firstY;

    private double[][] Q; //zakładam na razie 1 tablice q i lece losowo szukać karmika
    private final double gamma = 0.9;
    private final double alpha = 0.5;
    private MatrixR mr;
    private int[][] R;

    int crtState;
    int nextState; //tymczasowo bo nie zainicjalizowane
    int prevState;
    boolean explore = true;

    public Prey(Cell[][] cc, int TIME, MatrixR mr, Feeder[] feed, int feeders) {
        this.world = cc;
        this.feed = feed;
        this.feeders = feeders;
        image = new ImageIcon(new ImageIcon(getClass().getResource("/RT/resources/chicken.png")).getImage());
        this.setPosition();
        dirr = rnd.nextInt(4);
        this.hp = 100;
        this.TIME = TIME;
        this.death = false;

        Q = new double[400][400];
        for (double[] row : Q) {
            Arrays.fill(row, 0.0); //zerujemy każdy wiersz w tablicy Q
        }
        //printQ(Q);
        this.mr = mr;
        this.R = mr.getMatrixR();

        crtState = cellPoint.y * MAZE_SIZE + cellPoint.x;
    }

    void printQ() {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i < 400; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < 400; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < 400; j++) {
                System.out.printf("%4s", Q[i][j]);
            }
            System.out.println("]");
        }
    }

    public Image getGraphics() {
        return image.getImage();
    }

    private void setPosition() {
        int xPx, yPx;
        do {
            firstX = rnd.nextInt(20); //los 0-19 
            firstY = rnd.nextInt(20); //los 0-19 
            xPx = ((firstX + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
            yPx = ((firstY + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        } while (world[firstX][firstY].ocupated() == true);
        cellPoint = new Point(firstX, firstY);
        p = new Point(xPx, yPx);
        world[firstX][firstY].visit();
    }

    public Point getPosition() {
        return p;
    }

    public void move() {

//        int nextState = this.calculateQ();
        int x, y, xS, yS;
//        x = nextState / MAZE_SIZE;
//        y = nextState - x * MAZE_SIZE;
        x = this.cellPoint.x;
        y = this.cellPoint.y;
        xS = x;
        yS = y;
        this.timeCounter += TIME;
        world[x][y].leave();
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
                p.x = ((x + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
                p.y = ((y + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;

                this.calculateHp();
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

    public void calculateHp() { //oblicza co 1 ruch
        if (meetPredator()) {
            this.hp = this.hp - 15;
            //System.out.println("ouch");
        }
        if (meetFeeder()) {
            this.hp = this.hp + 5;
        }
        this.hp = this.hp - 1;
    }

    private boolean meetPredator() {
//        if (cellPoint.x > 0 && cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y - 1].isLeft() && world[cellPoint.x - 1][cellPoint.y - 1].getPredator()) {
//            return true;
        if (cellPoint.x > 0 && !world[cellPoint.x][cellPoint.y].isLeft() && world[cellPoint.x - 1][cellPoint.y].getPredator()) {
            return true;
//        } else if (cellPoint.x > 0 && cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y + 1].isLeft() && world[cellPoint.x - 1][cellPoint.y + 1].getPredator()) {
//            return true;
        } else if (cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y].isTop() && world[cellPoint.x][cellPoint.y - 1].getPredator()) {
            return true;
        } else if (world[cellPoint.x][cellPoint.y].getPredator()) {
            return true;
        } else if (cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y].isBottom() && world[cellPoint.x][cellPoint.y + 1].getPredator()) {
            return true;
//        } else if (cellPoint.x < 19 && cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y - 1].isRight() && world[cellPoint.x + 1][cellPoint.y - 1].getPredator()) {
//            return true;
        } else if (cellPoint.x < 19 && !world[cellPoint.x][cellPoint.y].isRight() && world[cellPoint.x + 1][cellPoint.y].getPredator()) {
            return true;
//        } else if (cellPoint.x < 19 && cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y + 1].isRight() && world[cellPoint.x + 1][cellPoint.y + 1].getPredator()) {
//            return true;
        } else {
            return false;
        }
    }

    private boolean meetFeeder() {
//        if (cellPoint.x > 0 && cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y - 1].isLeft() && world[cellPoint.x - 1][cellPoint.y - 1].getFeeder()) {
//            return true;
        if (cellPoint.x > 0 && !world[cellPoint.x][cellPoint.y].isLeft() && world[cellPoint.x - 1][cellPoint.y].getFeeder()) {
            return true;
//        } else if (cellPoint.x > 0 && cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y + 1].isLeft() && world[cellPoint.x - 1][cellPoint.y + 1].getFeeder()) {
//            return true;
        } else if (cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y].isTop() && world[cellPoint.x][cellPoint.y - 1].getFeeder()) {
            return true;
        } else if (world[cellPoint.x][cellPoint.y].getFeeder()) {
            return true;
        } else if (cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y].isBottom() && world[cellPoint.x][cellPoint.y + 1].getFeeder()) {
            return true;
//        } else if (cellPoint.x < 19 && cellPoint.y > 0 && cellPoint.y > 0 && !world[cellPoint.x][cellPoint.y - 1].isRight() && world[cellPoint.x + 1][cellPoint.y - 1].getFeeder()) {
//            return true;
        } else if (cellPoint.x < 19 && !world[cellPoint.x][cellPoint.y].isRight() && world[cellPoint.x + 1][cellPoint.y].getFeeder()) {
            return true;
//        } else if (cellPoint.x < 19 && cellPoint.y < 19 && !world[cellPoint.x][cellPoint.y + 1].isRight() && world[cellPoint.x + 1][cellPoint.y + 1].getFeeder()) {
//            return true;
        } else {
            return false;
        }
    }

    public int getHp() {
        return hp;
    }

    public int getTime() {
        return timeCounter / 1000;
    }

    public boolean isDead() {
        return death;
    }

    public void killPrey() {
        death = true;
    }

    public void restart() {

        int yPx, xPx;
        prevGoodDir = -1;
        noGo = -1;
        hp = 100;
        death = false;
        timeCounter = 0;
        //world[cellPoint.x][cellPoint.y].leave();
        cellPoint.x = firstX;
        cellPoint.y = firstY;
        xPx = ((firstX + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        yPx = ((firstY + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        p.x = xPx;
        p.y = yPx;
        crtState = cellPoint.y * MAZE_SIZE + cellPoint.x;
        //world[firstX][firstY].visit();

        for (int i = 0; i < feeders; i++) {
            feed[i].setEmpty(false);
            feed[i].setPositionAgain();
        }

        this.explore = false;
    }

    void calculateQ() {
        //pobieram punkt startowy i zamieniam go na wartość dla tablicy Q
        //int crtState = cellPoint.y * MAZE_SIZE + cellPoint.x; //odwrócone
        //int nextState; //tymczasowo bo nie zainicjalizowane
        this.timeCounter += TIME;
        if (!isFinalState(crtState)) { //powtarzamy dopóki nie wejdziemy na feedera //tu while oryginalnie
            int index, antiNoExit = 0;
            do {
                antiNoExit++;
                int[] actionsFromCurrentState = possibleActionsFromState(crtState);
                int length = actionsFromCurrentState.length;
                // Pick a random action from the ones possible
                index = rnd.nextInt(length); //losowo wybiera kolejny stan spośród tych co są != -1
                nextState = actionsFromCurrentState[index];
            } while (nextState == prevState && antiNoExit < 5); //szukaj kolejnego stanu dopóki następnmy stan jest taki sam jak poprzedni i trwa to od 

            // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
            double q = Q[crtState][nextState]; //przypisujemy wartość z Q dla tego nowo wybranego losowego nextState
            double maxQ = maxQ(nextState);
            int r = R[crtState][nextState];

            double value = q + alpha * (r + gamma * maxQ - q); //ten wzór jest trochę inny niż ten co ogarnialiśmy
            Q[crtState][nextState] = value;

            this.prevState = crtState;
            crtState = nextState;
        } else {
            this.world[cellPoint.x][cellPoint.y].setFeeder(false); //usuwamy feedera w cell
            this.crtState = this.prevState;
            // printQ(Q);
            for (int i = 0; i < feeders; i++) {
                if (!feed[i].empty && feed[i].xPx == p.x && feed[i].yPx == p.y) {
                    feed[i].setEmpty(true); //ustawiamy karmik na pusty
                }
            }
            //printQ(Q);
        }
        this.cellPoint.y = nextState / MAZE_SIZE;
        this.cellPoint.x = nextState - this.cellPoint.y * MAZE_SIZE;
        p.x = ((cellPoint.x + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        p.y = ((cellPoint.y + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        this.calculateHp();
    }

    private boolean isFinalState(int state) {
        int i = state / MAZE_SIZE;
        int j = state - i * MAZE_SIZE;

        return world[j][i].getFeeder();
    }

    private int[] possibleActionsFromState(int state) { //przelatuje przez dany wiersz R w poszukiwaniu możliwych stanów
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            if (R[state][i] != -1) {
                result.add(i); //dodajemy do listy numery stanów które nie mają -1
            }
        }

        return result.stream().mapToInt(i -> i).toArray();

    }

    private double maxQ(int nextState) { //wybiera największą wartość dla możliwych ruchów z aktualnego stanu
        int[] actionsFromState = possibleActionsFromState(nextState);
        double maxValue = Double.MIN_VALUE;
        for (int nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue) {
                maxValue = value; //wybiera jawiększą wartość spośród następnych stanów dla tego losewego nextState
            }
        }
        return maxValue;
    }

    public void exploitation() {

        int goToState = crtState;

        if (!isFinalState(crtState)) {
            int[] actionsFromState = possibleActionsFromState(crtState);

            double maxValue = Double.MIN_VALUE;

            // Pick to move to the state that has the maximum Q value
            for (int nextState : actionsFromState) {
                if (nextState != prevState) {
                    double value = Q[crtState][nextState];

                    if (value > maxValue) {
                        maxValue = value;
                        goToState = nextState;
                    }
                }
            }
//            if(goToState==crtState){
//                this.explore = true;
//            }
        } else {
            //printQ(Q);
            this.explore = true;
            this.world[cellPoint.x][cellPoint.y].setFeeder(false); //usuwamy feedera w cell
            this.crtState = this.prevState;
            for (int i = 0; i < feeders; i++) {
                if (!feed[i].empty && feed[i].xPx == p.x && feed[i].yPx == p.y) {
                    feed[i].setEmpty(true); //ustawiamy karmik na pusty
                }
            }
            //printQ(Q);
        }
        this.prevState = crtState;
        this.crtState = goToState;
        this.cellPoint.y = goToState / MAZE_SIZE;
        this.cellPoint.x = goToState - this.cellPoint.y * MAZE_SIZE;
        p.x = ((cellPoint.x + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        p.y = ((cellPoint.y + 1) * CELL_SIZE) - IMG_SIZE - SHIFT;
        this.calculateHp();
    }

//    public void setQ(double[][] tab) {
//        Q = tab;
//    }
//    public void draw(Graphics2D g)
//    {
//        g.drawImage(getActualFrame(), this.x, this.y, null);
//    }
}
