package ship;

import java.awt.*;

public class Ship {

    private ShipPiece[] shipPieces;
    private Point startingPosition;

    public Ship(ShipPiece[] shipPieces) {
        this.shipPieces = shipPieces;
        startingPosition = new Point(0, 0);
    }

    public boolean checkIfDead() {
        boolean isDead = true;
        for (ShipPiece shipPiece : shipPieces) {
            if (!shipPiece.isDestroyed()) isDead = false;
        }
        return isDead;
    }

    public ShipPiece[] getShipPieces() {
        return shipPieces;
    }

    public void setShipPieces(ShipPiece[] shipPieces) {
        this.shipPieces = shipPieces;
    }

    public Point getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Point startingPosition) {
        this.startingPosition = startingPosition;
    }
}
