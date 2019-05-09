package RustyTemple;

//klasa rysuje labirynt na podstawie klasy cell (lub pliku z labiryntenm (?) )
//ma rysować labirynt do bufferedImage i zwracać to do TempleTimer
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JPanel;

public class MapDrawer { //map creator

    static int w;
    static int h;
    static int cellSize = 20;
    private BufferedImage block, map;
    private final int IMG_SIZE = 16;
    private final int SHIFT = 8;
    private File f;
    private JPanel panel;

    public MapDrawer(File f, int cellSize, int x, int y, JPanel panel) {
        this.panel = panel;
        this.w = panel.getWidth();
        this.h = panel.getHeight();
        this.f = f;
        //this.cellSize = cellSize;
        map = new BufferedImage((x + 1) * cellSize + 1, (y + 1) * cellSize + 1, BufferedImage.TYPE_INT_ARGB);
        try {
            map = javax.imageio.ImageIO.read(TempleTimer.class.getResource("/RT/resources/background.jpg"));
            block = javax.imageio.ImageIO.read(TempleTimer.class.getResource("/RT/resources/shiny_stone_samll.jpg"));
        } catch (IOException e) {
            map = new BufferedImage(w, panel.getHeight(), 2);
            block = new BufferedImage(w, panel.getHeight(), 2);
        }
        this.drawMaze();
    }

    public void drawMaze() {
        try {
            BufferedReader txt = new BufferedReader(new FileReader(f));
            String line;
            int var = h / cellSize;
            int x, y;

            Graphics2D graphics2d = (Graphics2D) map.getGraphics();
            graphics2d.setColor(Color.ORANGE);

            while (((line = txt.readLine()) != null)) {

                StringTokenizer t = new StringTokenizer(line, " ");

                x = Integer.parseInt(t.nextToken()) * var;
                y = Integer.parseInt(t.nextToken()) * var;

                if (t.hasMoreTokens()) {
                    String operation = t.nextToken();

                    switch (operation) { //tu wstawianie klocków
                        case "PD":
                             graphics2d.drawLine(x, y, x + var, y);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x + 16 - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x + 32 - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);

                            graphics2d.drawLine(x, y, x, y + var);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT + 16, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT + 32, IMG_SIZE, IMG_SIZE, null);
                            break;
                        case "P":
                             graphics2d.drawLine(x, y, x + var, y);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null); //do osobnej metody te 3 linie
//                            graphics2d.drawImage(block, x + 16 - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x + 32 - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
                            break;
                        case "D":
                             graphics2d.drawLine(x, y, x, y + var);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT + 16, IMG_SIZE, IMG_SIZE, null);
//                            graphics2d.drawImage(block, x - SHIFT, y - SHIFT + 32, IMG_SIZE, IMG_SIZE, null);
                            break;
                        default:
                            break;
                    }

                }
            }
            for (int i = 0; i < panel.getWidth(); i += 16) { //dolna i prawa krawędź
//                graphics2d.drawImage(block, i, panel.getHeight() - 8, IMG_SIZE, IMG_SIZE, null);
//                graphics2d.drawImage(block, panel.getWidth() - 8, i, IMG_SIZE, IMG_SIZE, null);
            }
             graphics2d.drawLine(0, panel.getHeight()-1, panel.getHeight()-1, panel.getHeight()-1);
             graphics2d.drawLine(panel.getHeight()-1, 0, panel.getHeight()-1, panel.getHeight()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getMap() {
        return map;
    }
}
