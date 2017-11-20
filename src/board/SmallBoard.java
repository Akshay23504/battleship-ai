package board;

import ship.ShipPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SmallBoard extends JPanel {

    private Object[][] array;
    private BufferedImage boardImage;
    private static final int X_ORIGIN = 23; // X coordinate of the top left
    private static final int Y_ORIGIN = 39; // y coordinate of the top left
    private static final int TILE_SIZE = 20; // size of the tile spaces
    private static final int BORDER_SIZE = 3; // size of the border between tiles
    private static final int PIECE_SIZE = 18; // size of the pieces that appear

    public SmallBoard(Object[][] array) {
        this.array = array;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension((X_ORIGIN + 2 + (TILE_SIZE+BORDER_SIZE) * array.length),
                Y_ORIGIN+ 2 + ((TILE_SIZE + BORDER_SIZE) * array.length)));
        setSize(getPreferredSize());

        try {
            boardImage = ImageIO.read(new File("boardSmallLabels.png"));
        } catch (IOException e) {
            System.out.println("Failed to load image");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        // draws the board image
        graphics2D.drawImage(boardImage, 0, 15, this);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                // if there is a ship piece at the location
                if ((array[i][j]).getClass().getName().equals("ship.ShipPiece")) {
                    // draw the image of the ship piece at the proper board
                    graphics2D.drawImage(((ShipPiece) array[i][j]).getShipImage(),
                            X_ORIGIN + 2 + ((TILE_SIZE + BORDER_SIZE) * i) + BORDER_SIZE / 2,
                            Y_ORIGIN + 2 + ((TILE_SIZE + BORDER_SIZE) * j) + BORDER_SIZE / 2,
                            PIECE_SIZE, PIECE_SIZE, this);
                }
            }
        }
    }

    public Object[][] getArray() {
        return array;
    }

    public void setArray(Object[][] array) {
        this.array = array;
    }
}
