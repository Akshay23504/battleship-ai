package ship;

import javax.swing.*;
import java.awt.*;

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
        if (isPlayer1) shipPieceAlive = new ImageIcon("Player1.png").getImage();
        else shipPieceAlive = new ImageIcon("Player2.png").getImage();
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
        if (isPlayer1) setShipImage("Player1Hit.png");
        else setShipImage("Player2Hit.png");
    }

    public boolean isDestroyed() {
        return shipIsDestroyed;
    }
}
