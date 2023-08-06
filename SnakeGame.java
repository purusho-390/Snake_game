import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int GRID_SIZE = 20;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 150;

    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private Timer timer;
    private boolean running;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(new GameKeyListener());
        setFocusable(true);
        initGame();
        timer = new Timer(DELAY, new GameLoop());
        timer.start();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        generateFood();
        direction = KeyEvent.VK_RIGHT;
        running = true;
    }

    private void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_WIDTH);
        int y = rand.nextInt(GRID_HEIGHT);
        food = new Point(x, y);
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead;

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead = new Point(head.x, head.y - 1);
                break;
            case KeyEvent.VK_DOWN:
                newHead = new Point(head.x, head.y + 1);
                break;
            case KeyEvent.VK_LEFT:
                newHead = new Point(head.x - 1, head.y);
                break;
            case KeyEvent.VK_RIGHT:
                newHead = new Point(head.x + 1, head.y);
                break;
            default:
                return;
        }

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            generateFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }

        checkCollision();
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            gameOver();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        running = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        initGame();
        timer.start();
        running = true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private class GameKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
                direction = KeyEvent.VK_UP;
            } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
                direction = KeyEvent.VK_DOWN;
            } else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
                direction = KeyEvent.VK_LEFT;
            } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                direction = KeyEvent.VK_RIGHT;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class GameLoop implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                moveSnake();
                repaint();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
