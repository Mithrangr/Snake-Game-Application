import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {

    MenuPanel menuPanel;
    GamePanel gamePanel;

    GameFrame() {
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new CardLayout());

        menuPanel = new MenuPanel(this);
        this.add(menuPanel, "Menu");

        this.pack();
        this.setVisible(true);
    }

    public void startGame(int delay) {
    this.getContentPane().removeAll();
    gamePanel = new GamePanel(delay);  // Pass delay here
    this.add(gamePanel);
    this.revalidate();
    this.repaint();
    gamePanel.requestFocusInWindow();
}
}
