import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public int map[][];
    public int bricksWidth;
    public int bricksHeight;
    private Color[] brickColors = {Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE,Color.RED, Color.GREEN, Color.BLUE, Color.WHITE}; // Different brick colors for levels

    public MapGenerator(int level) {
        int rows, cols;
        if (level == 1) {
            rows = 1;
            cols = 5;
        } else if (level == 2) {
            rows = 2;
            cols = 5;
        } else if (level == 3) {
            rows = 3;
            cols = 5;
        } else {
            rows = 4;
            cols = level%4 + 6;
        }

        map = new int[rows][cols];
        for (int[] mapRow : map) {
            for (int j = 0; j < map[0].length; j++) {
                mapRow[j] = level; // Assign different values for different levels
            }
        }
        bricksWidth = 540 / cols;
        bricksHeight = 150 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(brickColors[map[i][j] - 1]); // Set color based on level
                    g.fillRect(j * bricksWidth + 80, i * bricksHeight + 50, bricksWidth, bricksHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * bricksWidth + 80, i * bricksHeight + 50, bricksWidth, bricksHeight);
                }
            }
        }
    }

    public void setBricksValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
