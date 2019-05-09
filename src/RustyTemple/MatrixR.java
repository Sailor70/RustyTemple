/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustyTemple;

/**
 *
 * @author Sailor
 */
public class MatrixR {

    private Cell[][] world;
    private static int mazeSize = 20;
    private static int rSize = mazeSize * mazeSize; //400
    private int[][] r;
    private final int p = 1; //passage - możliwe przejście - wartość 0
    private final int np = -1; //no passage - brak przejścia - wartość -1
    private final int feed = 100; //feeder - przejście do kormika - wartość 100
    private final int pred = -80; //feeder - przejście do kormika - wartość 100

    MatrixR(CellCreator cc) {

        this.world = cc.getWorld();
        r = new int[rSize][rSize];
        int i, j;

        for (int k = 0; k < rSize; k++) {

            i = k / mazeSize; //przekształcamy wartość k na numer komórki w naszej planszy 20x20
            j = k - i * mazeSize;

            for (int s = 0; s < rSize; s++) { //zerujemy wiersz (-1)
                r[k][s] = np;
            }

           // if (!world[j][i].getFeeder()) { //jeśli w komórce nie ma feedera to sprawdzamy dalej | jeśli jest to cały wiersz -1 |trzeba będzie zmienić bo nie ruszy dalej?

                int goLeft = j - 1;
                if (goLeft >= 0 && world[j][i].isLeft() == false) { // jeśli nie wchodzimy na ujemne i nie ma lewej ściany to

                    int target = i * mazeSize + goLeft; //obliczona współrzędna y do macierzy r dla komórki na lewo od aktualnej

                    if (world[goLeft][i].getFeeder()) { //jest feeder
                        r[k][target] = feed;
                    } else if (world[goLeft][i].getPredator()) {
                        r[k][target] = pred; //przydzielam wartosc ujemna za predatora
                    } else {
                        r[k][target] = p; //przydzielamy 0 bo możliwe przejscie
                    }
                }

                int goRight = j + 1;
                if (goRight < mazeSize && world[j][i].isRight() == false) { //world ma na odwrót współrzędne!!!

                    int target = i * mazeSize + goRight; //obliczona współrzędna y do macierzy r dla komórki na lewo od aktualnejd

                    if (world[goRight][i].getFeeder()) { //jest feeder
                        r[k][target] = feed;
                    } else if (world[goRight][i].getPredator()) {
                        r[k][target] = pred; //przydzielam wartosc ujemna za predatora
                    } else {
                        r[k][target] = p; //przydzielamy 0 bo możliwe przejscie
                    }
                }

                int goUp = i - 1;
                if (goUp >= 0 && world[j][i].isTop() == false) {

                    int target = goUp * mazeSize + j; //obliczona współrzędna y do macierzy r dla komórki na lewo od aktualnejd

                    if (world[j][goUp].getFeeder()) { //jest feeder
                        r[k][target] = feed;
                    } else if (world[j][goUp].getPredator()) {
                        r[k][target] = pred; //przydzielam wartosc ujemna za predatora
                    } else {
                        r[k][target] = p; //przydzielamy 0 bo możliwe przejscie
                    }
                }

                int goDown = i + 1;
                if (goDown < mazeSize && world[j][i].isBottom() == false) {

                    int target = goDown * mazeSize + j; //obliczona współrzędna y do macierzy r dla komórki na lewo od aktualnejd

                    if (world[j][goDown].getFeeder()) { //jest feeder
                        r[k][target] = feed;
                    } else if (world[j][goDown].getPredator()) {
                        r[k][target] = pred; //przydzielam wartosc ujemna za predatora
                    } else {
                        r[k][target] = p; //przydzielamy 0 bo możliwe przejscie
                    }
                }
          //  }
        }
        //printR(r);
    }

    //tylko jako sprawdzenie
    public void printR(int[][] matrix) {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i < 400; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < 400; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < 400; j++) {
                System.out.printf("%4s", matrix[i][j]);
            }
            System.out.println("]");
        }
    }

    public int[][] getMatrixR() {
        return r;
    }

}
