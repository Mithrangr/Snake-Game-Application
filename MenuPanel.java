import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {

    GameFrame frame;

    MenuPanel(GameFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(1300, 750));
        this.setBackground(Color.black);
        this.setLayout(null);

        // 🐍 Title
        JLabel titleLabel = new JLabel("🐍 Snake Game");
        titleLabel.setForeground(Color.green);
        titleLabel.setFont(new Font("Ink Free", Font.BOLD, 80));
        titleLabel.setBounds(320, 120, 700, 100);
        this.add(titleLabel);

        // 🎮 Difficulty Buttons
        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        Font btnFont = new Font("Arial", Font.BOLD, 24);

        easyButton.setFont(btnFont);
        mediumButton.setFont(btnFont);
        hardButton.setFont(btnFont);

        easyButton.setBackground(new Color(120, 220, 120));
        mediumButton.setBackground(new Color(255, 215, 120));
        hardButton.setBackground(new Color(255, 120, 120));

        easyButton.setForeground(Color.black);
        mediumButton.setForeground(Color.black);
        hardButton.setForeground(Color.black);

        easyButton.setFocusPainted(false);
        mediumButton.setFocusPainted(false);
        hardButton.setFocusPainted(false);

        // 🧩 Button Positions
        easyButton.setBounds(525, 300, 250, 60);
        mediumButton.setBounds(525, 380, 250, 60);
        hardButton.setBounds(525, 460, 250, 60);

        this.add(easyButton);
        this.add(mediumButton);
        this.add(hardButton);

        // ❌ Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setBackground(new Color(200, 0, 0));
        exitButton.setForeground(Color.white);
        exitButton.setFocusPainted(false);
        exitButton.setBounds(525, 550, 250, 60);
        this.add(exitButton);

        // 🎯 Button Actions
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.startGame(350); // slow
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.startGame(250); // normal
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.startGame(150); // fast
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
