import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    int DELAY;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton restartButton;
    int highScore = 0;
    File highScoreFile = new File("highscore.txt");
    Image backgroundImage;
    Image appleImage;
    Image snakeHeadImage;
    Image snakeBodyImage;


    GamePanel(int delay) {
        this.DELAY = delay;
        random = new Random();
        backgroundImage = new ImageIcon("images/background.jpg").getImage();
        appleImage = new ImageIcon("images/apple.png").getImage();
        snakeHeadImage = new ImageIcon("images/snake_head.png").getImage();
        snakeBodyImage = new ImageIcon("images/snake_body.png").getImage();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
        
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 18));
        restartButton.setBackground(new Color(0, 180, 80)); 
        restartButton.setForeground(Color.white);
        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setVisible(false); 
        restartButton.setBounds(SCREEN_WIDTH / 2 - 75, SCREEN_HEIGHT / 2 + 80, 150, 45);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                bodyParts = 6;
                applesEaten = 0;
                direction = 'R';
                x[0] = 0;
                y[0] = 0;
                restartButton.setVisible(false); 
                startGame();
                repaint();
            }
        });

        this.add(restartButton);
        // Load saved high score
try {
    if (highScoreFile.exists()) {
        Scanner fileReader = new Scanner(highScoreFile);
        if (fileReader.hasNextInt()) {
            highScore = fileReader.nextInt();
        }
        fileReader.close();
    }
} catch (Exception e) {
    e.printStackTrace();
}

        startGame();
    }
    

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    if (running) {
        // Draw background
        g2.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);

        // Draw apple
        g2.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);

        // --- Draw smooth snake body first ---
        for (int i = 1; i < bodyParts; i++) {
            int prevX = x[i-1];
            int prevY = y[i-1];
            int currX = x[i];
            int currY = y[i];

            // Offset first segment to avoid overlapping head
            if (i == 1) {
                switch(direction) {
                    case 'U': prevY += UNIT_SIZE/4; break;
                    case 'D': prevY -= UNIT_SIZE/4; break;
                    case 'L': prevX += UNIT_SIZE/4; break;
                    case 'R': prevX -= UNIT_SIZE/4; break;
                }
            }

            int width = Math.abs(currX - prevX) + UNIT_SIZE;
            int height = Math.abs(currY - prevY) + UNIT_SIZE;
            int drawX = Math.min(prevX, currX);
            int drawY = Math.min(prevY, currY);

            g2.drawImage(snakeBodyImage, drawX, drawY, width, height, this);
        }

        // --- Draw snake head last so it's visible on top ---
        int headX = x[0];
        int headY = y[0];
        double rotation = 0;
        switch (direction) {
            case 'U': rotation = Math.toRadians(270); break;
            case 'D': rotation = Math.toRadians(90); break;
            case 'L': rotation = Math.toRadians(180); break;
            case 'R': rotation = 0; break;
        }
        g2.rotate(rotation, headX + UNIT_SIZE/2, headY + UNIT_SIZE/2);
        g2.drawImage(snakeHeadImage, headX, headY, UNIT_SIZE, UNIT_SIZE, this);
        g2.rotate(-rotation, headX + UNIT_SIZE/2, headY + UNIT_SIZE/2);

        // --- Draw score ---
      g.setColor(Color.white);
g.setFont(new Font("Ink Free", Font.BOLD, 35));
FontMetrics metrics = getFontMetrics(g.getFont());

// Draw Current Score (center-top)
g.drawString("Score: " + applesEaten,
    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
    g.getFont().getSize() + 20);

// Draw High Score (top-right)
g.drawString("High Score: " + highScore,
    SCREEN_WIDTH - 250,
    g.getFont().getSize() + 20);


    } else {
        gameOver(g2);
    }
}




    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i -1];
            y[i] = y[i -1];
        }

        switch(direction) {
            case 'U': y[0] = y[0] - UNIT_SIZE; break;
            case 'D': y[0] = y[0] + UNIT_SIZE; break;
            case 'L': x[0] = x[0] - UNIT_SIZE; break;
            case 'R': x[0] = x[0] + UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

public void gameOver(Graphics g) {
    // Update and save high score
if (applesEaten > highScore) {
    highScore = applesEaten;
    try {
        PrintWriter writer = new PrintWriter(highScoreFile);
        writer.println(highScore);
        writer.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Draw background dim
    g.setColor(new Color(0, 0, 0, 180));
    g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    // Draw "Game Over" text
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over",
        (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2,
        SCREEN_HEIGHT / 2 - 50);

    // Draw score
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.setColor(Color.white);
    g.drawString("Score: " + applesEaten,
        (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
        SCREEN_HEIGHT / 2 + 20);

    // Show restart button
    int buttonWidth = 150;
    int buttonHeight = 45;
    int buttonX = (SCREEN_WIDTH - buttonWidth) / 2;
    int buttonY = (SCREEN_HEIGHT / 2) + 100;  
    restartButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
    restartButton.setVisible(true);
}

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                   
                }
        }
    }
}