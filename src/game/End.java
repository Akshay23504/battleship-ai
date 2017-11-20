package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class End implements MouseListener {

    private JPanel window;
    private ImageIcon backgroundImageIcon;
    private JLabel backgroundImageContainer;

    public End(JFrame window, boolean playerOneWon) {
        this.window = (JPanel) window.getContentPane();
        if (playerOneWon) backgroundImageIcon = new ImageIcon("P1Win.png");
        else backgroundImageIcon = new ImageIcon("P2Win.png");
        Image scaledBackgroundImage = backgroundImageIcon.getImage().
                getScaledInstance(window.getWidth(), window.getHeight(), BufferedImage.SCALE_FAST);
        backgroundImageContainer = new JLabel(new ImageIcon(scaledBackgroundImage));
        backgroundImageContainer.setSize(window.getWidth(), window.getHeight());
        backgroundImageContainer.setLocation(0, 0);
        backgroundImageContainer.setBackground(Color.WHITE);
        backgroundImageContainer.addMouseListener(this);
    }

    public void loadEndScreen() {
        window.add(backgroundImageContainer);
        window.setComponentZOrder(backgroundImageContainer, 0);
        window.setVisible(true);
        window.revalidate();
        window.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
