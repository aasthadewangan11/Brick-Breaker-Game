import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle; // Added for collision detection

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;
    private MapGenerator map;
    private int level = 1; // Starting level

    public GamePlay() {
        map = new MapGenerator(level); // Using level variable
        totalBricks = calculateTotalBricks(level); // Calculating total bricks based on level
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        g.setColor(Color.yellow);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.GREEN);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (ballPosY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("    Game Over Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);
        }
        if (totalBricks == 0) {
            play = false;
            ballYDir = -2;
            ballXDir = -1;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("    Game Over: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("   Press Enter to Restart", 190, 340);
            
        }

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Level: " + level, 20, 60);
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();
    
        if (play) {
            // Collision detection
            Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);
    
            if (ballRect.intersects(paddleRect)) {
                ballYDir = -ballYDir;
            }
    
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        int bricksWidth = map.bricksWidth;
                        int bricksHeight = map.bricksHeight;
    
                        Rectangle brickRect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
    
                        if (ballRect.intersects(brickRect)) {
                            map.setBricksValue(0, i, j);
                            totalBricks--;
                            score += 5;
    
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + bricksWidth) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }
                            break A;
                        }
                    }
                }
            }
    
            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX < 0) {
                ballXDir = -ballXDir;
            }
            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }
            if (ballPosX > 670) {
                ballXDir = -ballXDir;
            }
    
            if (totalBricks == 0) {
                level++; // Increase level
                if (level > 7) {
                    level = 7; // Cap level at 7 for maximum 70 bricks
                }
                totalBricks = calculateTotalBricks(level); // Calculate new total bricks
                map = new MapGenerator(level); // Generate new map for the next level
                ballXDir *= 1.5; // Double ball speed for each level
                ballYDir *= 1.5;
                
            }
            
            // Check game over conditions
            if (ballPosY > 570 || totalBricks == 0) {
                play = false;
                ballXDir = 0;
                ballYDir = 0;
                level = 1; // Reset level to 1 upon game over
                totalBricks = calculateTotalBricks(level); // Reset total bricks based on level
                map = new MapGenerator(level); // Generate map for level 1
                ballXDir = -1; // Reset ball speed
                ballYDir = -2;
                repaint();
            }
        }
        repaint();
    }
    
    

    @Override
    public void keyTyped(KeyEvent e) {
        // Empty implementation
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Empty implementation
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                playerX += 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                playerX -= 20;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                score = 0;
                playerX = 310;
                totalBricks = calculateTotalBricks(level); // Reset total bricks based on level
                map = new MapGenerator(level);

                play = true;
                repaint();
            }
        }
    }

    // Helper method to calculate total bricks based on level
    private int calculateTotalBricks(int level) {
        int total = 0;
        if (level >= 1 && level <= 3) {
            total = (level * 5);
        } else {
            total = 20 + ((level - 3) * 4);
            if (total > 70) {
                total = 70; // Maximum total bricks capped at 70
            }
        }
        return total;
    }
}
