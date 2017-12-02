package board;

import ship.ShipPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Board extends JPanel implements MouseListener {

    private BufferedImage boardImage;
    private Object[][] array;
    private static final int X_ORIGIN = 54; // X coordinate of the top left
    private static final int Y_ORIGIN = 56; // Y coordinate of the top left
    private static final int TILE_SIZE = 47; // Size of the tile spaces
    private static final int BORDER_SIZE = 5; // size of the border between spaces
    private volatile boolean isTurn;
    private boolean state;

    public Board(Object[][] array) {
        this.array = array;
        isTurn = true;
        state = false;

        setBackground(Color.white);
        setPreferredSize(new Dimension((X_ORIGIN + array.length + 1 + ((TILE_SIZE + BORDER_SIZE) * array.length)),
                Y_ORIGIN + array.length + 1 + ((TILE_SIZE + BORDER_SIZE) * array.length)));
        setSize(getPreferredSize());
        setLocation(0, 0);

        try {
            boardImage = ImageIO.read(new File("boardLabels.png"));
        } catch (IOException e) {
            System.out.println("Failed to load image");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(boardImage, 0, 0, this);

        // loops through all spots in the board
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                // checks if there is a 1 or a ShipPiece that has not been destroyed
                if (array[i][j].equals(1) || ((array[i][j]).getClass().getName().equals("ship.ShipPiece")
                        && !((ShipPiece) array[i][j]).isDestroyed())) {
                    // covers the spot on the board with a gray box
                    graphics2D.setColor(Color.gray);
                    graphics2D.fillRect(X_ORIGIN + i + 1 + ((TILE_SIZE + BORDER_SIZE) * i), Y_ORIGIN + j + 1 +
                                    ((TILE_SIZE + BORDER_SIZE) * j),
                            TILE_SIZE + (BORDER_SIZE / 2) - 1, TILE_SIZE + (BORDER_SIZE / 2) - 1);
                    // if there is a ship piece at the position that is destroyed
                } else if ((array[i][j]).getClass().getName().equals("ship.ShipPiece")) {
                    // draw the image associated with the ship piece
                    graphics2D.drawImage(((ShipPiece) array[i][j]).getShipImage(),
                            X_ORIGIN + i + ((TILE_SIZE + BORDER_SIZE) * i) + BORDER_SIZE / 2,
                            Y_ORIGIN + j + ((TILE_SIZE + BORDER_SIZE) * j) + BORDER_SIZE / 2, this);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // left click
        if (isTurn && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            // turns the x coordinate of the mouse into an x coordinate in the board array
            int value = mouseEvent.getX();
            int counter1 = 0;
            while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter1) + BORDER_SIZE < value) counter1++;
            counter1--;

            // turns the y coordinate of the mouse into a y coordinate in the board array
            int value2 = mouseEvent.getY() - (TILE_SIZE / 2);
            int counter2 = 0;
            while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter2) + BORDER_SIZE < value2) counter2++;
            counter2--;

            selectedPositionOnBoardByPlayer(counter1, counter2);
        } else if (!isTurn && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            state = true;
        }
    }

    public int selectedPositionOnBoardByPlayer(int i, int j) {
        int result = 0;
        boolean valid = false;
        // if counter1 and counter2 are valid positions in the array
        if (i < array.length && i >= 0) {
            if (j < array[0].length && j >= 0) {
                // if the object at the coordinate is 1
                if (array[i][j].equals(1)) {
                    valid = true;
                    // set the object at the coordinate to 0 (an empty space)
                    array[i][j] = 0;
                    repaint();
                    result = 2;
                    // end the turn
                    isTurn = false;
                    // if the object at the coordinate is a ShipPiece that is not destroyed
                } else if ((array[i][j]).getClass().getName().equals("ship.ShipPiece")
                        && !((ShipPiece) array[i][j]).isDestroyed()) {
                    valid = true;
                    // destroy the ship piece
                    ((ShipPiece) array[i][j]).destroy();
                    repaint();
                    result = 1;
                    // end the turn
                    isTurn = false;
                }
                state = false;
            }
        }
        return result;
    }

    public Object[][] getArray() {
        return array;
    }

    public void setArray(Object[][] array) {
        this.array = array;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
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
