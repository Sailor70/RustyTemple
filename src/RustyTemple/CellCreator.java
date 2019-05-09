/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustyTemple;

//klasa czyta plik z labiryntem i na jego podstawie tworzy komórki (klasa Cell) dla całego labiryntu
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class CellCreator { //worldCreator

    private static Cell[][] maze;
    private int size;

    public CellCreator(File f) throws IOException {
        size = getMazeSize(f) + 1;
        try {
            BufferedReader txt = new BufferedReader(new FileReader(f));
            String line;
            maze = new Cell[size][size];
            int x;
            int y;
            String left = "left";
            String right = "right";
            String top = "top";
            String bottom = "bottom";
            while ((line = txt.readLine()) != null) {

                StringTokenizer t = new StringTokenizer(line, " ");
                x = Integer.parseInt(t.nextToken());
                y = Integer.parseInt(t.nextToken());

                if (t.hasMoreTokens()) {
                    String operation = t.nextToken();

                    switch (operation) {

                        case "PD":

                            maze[x][y] = new Cell();
                            maze[x][y].makeWall(top);
                            maze[x][y].makeWall(left);

                            if (x != 0 && y != 0) {
                                maze[x][y - 1].makeWall(bottom);
                                maze[x - 1][y].makeWall(right);
                            }
                            if (x == 0 && y != 0) {
                                maze[x][y - 1].makeWall(bottom);
                            }
                            if (x != 0 && y == 0) {
                                maze[x - 1][y].makeWall(right);
                            }
                            break;

                        case "P":

                            maze[x][y] = new Cell();
                            maze[x][y].makeWall(top);

                            if (x != 0 && y != 0) {
                                maze[x][y - 1].makeWall(bottom);
                            }
                            if (x == 0 && y != 0) {
                                maze[x][y - 1].makeWall(bottom);
                            }
                            break;

                        case "D":
                            maze[x][y] = new Cell();
                            maze[x][y].makeWall(left);

                            if (x != 0 && y != 0) {
                                maze[x - 1][y].makeWall(right);
                            }
                            if (x != 0 && y == 0) {
                                maze[x - 1][y].makeWall(right);
                            }
                            break;
                    }
                } else {
                    maze[x][y] = new Cell();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getMazeSize(File file) throws FileNotFoundException, IOException {

        BufferedReader txt = new BufferedReader(new FileReader(file));
        String line;
        String size = "";

        while ((line = txt.readLine()) != null) {

            StringTokenizer t = new StringTokenizer(line, " ");
            size = t.nextToken();

        }

        return Integer.parseInt(size);
    }

    public Point getSize() {
        Point p = new Point();
        p.x = size;
        p.y = size;
        return p;
    }

    public static Cell[][] getWorld() {
        return maze;
    }
}
