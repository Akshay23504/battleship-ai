package ship;

import javax.swing.*;
import java.awt.*;

/**
 * A single square of a ship. A ship piece is part of the ship. This class also
 * has a few helper methods to perform functionalities like setting an image
 * for a ship piece when there is a hit
 */
public class ShipPiece {

    private Image shipPieceAlive;
    private boolean shipIsDestroyed;
    private boolean isPlayer1;

    /**
     * Constructor that has a boolean to determine which player the ship piece
     * belongs to. false is player 2, true is player 1
     */
    public ShipPiece(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
        if (isPlayer1) shipPieceAlive = new ImageIcon("images/player1.png").getImage();
        else shipPieceAlive = new ImageIcon("images/player2.png").getImage();
        shipIsDestroyed = false;
    }

    public void setShipImage(String file) {
        shipPieceAlive = new ImageIcon(file).getImage();
    }

    public Image getShipImage() {
        return shipPieceAlive;
    }

    public void destroy() {
        shipIsDestroyed = true;
        if (isPlayer1) setShipImage("images/player1Hit.png");
        else setShipImage("images/player2Hit.png");
    }

    public boolean isDestroyed() {
        return shipIsDestroyed;
    }
}
