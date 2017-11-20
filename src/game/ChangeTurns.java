package game;

import board.Board;
import board.SmallBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ChangeTurns implements MouseListener {

    private JPanel window;
    private JLabel backgroundImageContainer;
    private Board board;
    private SmallBoard smallBoard;

    public ChangeTurns(JPanel window, Board board, SmallBoard smallBoard) {
        this.window = window;
        Image scaledBackgroundImage = new ImageIcon("NextTurn.png").getImage().
                getScaledInstance(this.window.getWidth(), this.window.getHeight(), BufferedImage.SCALE_FAST);
        backgroundImageContainer = new JLabel(new ImageIcon(scaledBackgroundImage));
        backgroundImageContainer.setSize(this.window.getWidth(), this.window.getHeight());
        backgroundImageContainer.setLocation(0, 0);
        this.board = board;
        this.smallBoard = smallBoard;
    }

    public void loadChangeTurnScreen(Mode gameMode, boolean isPlayerOne) {
        window.add(backgroundImageContainer);
        backgroundImageContainer.addMouseListener(this);
        window.setVisible(true);
        window.repaint();

        switch (gameMode) {
            case HumanVsAI:
                if (isPlayerOne) {
                    setSomeThings();
                }
                break;
            case AIVsAI:
                setSomeThings();
                break;
            case HumanVsHuman:
                default:
                    break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setSomeThings();
    }

    private void setSomeThings() {
        window.remove(backgroundImageContainer);
        window.revalidate();
        window.repaint();
        board.setTurn(true);
        board.setVisible(true);
        smallBoard.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
