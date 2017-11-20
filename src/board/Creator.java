package board;

import ship.Ship;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Creator extends JPanel {

    private BufferedImage boardImage;
    private Object[][] boardArray;
    private Ship[] shipArray;
    private JPanel[] panelArray;
    private JButton endSetup, randomizeShipsButton;
    private JFrame window;
    private volatile boolean completeSetup;
    private static final int X_ORIGIN = 54;
    private static final int Y_ORIGIN = 56;
    private static final int TILE_SIZE = 47;
    private static final int BORDER_SIZE = 5;
    private static boolean currentlyPlacingShip;

    public Creator(Ship[] shipArray, int boardSize, JFrame window) {
        boardImage = null;
        completeSetup = false;
        currentlyPlacingShip = false;
        setLayout(null);
        setBackground(Color.BLACK);
        setLocation(0, 0);
        this.window = window;

        Object[][] board = new Object[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 1;
            }
        }

        boardArray = board;
        this.shipArray = shipArray;
        panelArray = new JPanel[this.shipArray.length];

        try {
            boardImage = ImageIO.read(new File("boardLabels.png"));
        } catch (IOException e) {
            System.out.println("Failed to load Image");
        }
    }

    public void setup() {
        int largestShipSize = 0;
        for (int i = 0; i < shipArray.length; i++) {
            if (shipArray[i].getShipPieces().length > largestShipSize) {
                largestShipSize = shipArray[i].getShipPieces().length;
            }
        }

        int windowWidth = X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * boardArray.length) + (2 * BORDER_SIZE) + 50
                + ((largestShipSize + 1) * TILE_SIZE);
        int windowHeight = Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * (boardArray.length + 1));
        if (windowHeight < 2 * TILE_SIZE + (shipArray.length * (TILE_SIZE + BORDER_SIZE + 2))) {
            windowHeight = 2 * TILE_SIZE + (shipArray.length * (TILE_SIZE + BORDER_SIZE + 2));
        }
        window.setPreferredSize(new Dimension(windowWidth, windowHeight));
        window.setMinimumSize(new Dimension(windowWidth, windowHeight));
        window.pack();
        setSize(window.getContentPane().getSize());

        JLabel boardLabel = new JLabel(new ImageIcon(boardImage));
        boardLabel.setSize(X_ORIGIN + boardArray.length + 1 + ((TILE_SIZE + BORDER_SIZE) * boardArray.length),
                Y_ORIGIN + boardArray.length + 1 + ((TILE_SIZE + BORDER_SIZE) * (boardArray.length)));
        boardLabel.setLocation(0, 0);
        boardLabel.setHorizontalAlignment(SwingConstants.LEFT);
        boardLabel.setVerticalAlignment(SwingConstants.TOP);
        add(boardLabel);

        addRandomizeButton(boardLabel.getWidth());
        addCompleteSetupButton(boardLabel.getWidth());

        // Loop for all ships
        for (int i = 0; i < shipArray.length; i++) {
            final int shipNumber = i;

            // X axis layout for the ship
            JPanel jPanel = new JPanel();
            jPanel.setBackground(Color.BLACK);
            jPanel.setOpaque(false);
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
            jPanel.add(Box.createRigidArea(new Dimension(0, 0)));

            // Loop for all ship pieces
            for (int j = 0; j < shipArray[i].getShipPieces().length; j++) {
                // Add label for each image to panel
                jPanel.add(new JLabel(new ImageIcon(shipArray[i].getShipPieces()[j].getShipImage())));
                jPanel.add(Box.createRigidArea(new Dimension(BORDER_SIZE + 2, 0)));
            }

            jPanel.setLocation(X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * boardArray.length) + (2 * BORDER_SIZE) + 50,
                    TILE_SIZE + BORDER_SIZE + 2 + (i * (TILE_SIZE + BORDER_SIZE + 2)) + endSetup.getHeight());
            jPanel.setSize(shipArray[i].getShipPieces().length * (TILE_SIZE + BORDER_SIZE), TILE_SIZE);
            shipArray[shipNumber].setStartingPosition(jPanel.getLocation());
            add(jPanel);
            panelArray[i] = jPanel;
            setComponentZOrder(jPanel, 0);

            jPanel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        JPanel component = (JPanel) e.getComponent().getParent().getParent();
                        Point point = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component));
                        jPanel.setLocation((int) point.getX() - (TILE_SIZE / 2), (int) point.getY() - (TILE_SIZE / 2));
                        currentlyPlacingShip = true;
                    }
                }
            });

            jPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    // gets the coordinates of the mouse in terms of the window
                    JPanel component = (JPanel) e.getComponent().getParent().getParent();
                    Point point = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component));
                    int counter1 = 0;
                    int counter2 = 0;

                    // calculates the position in the grid array based on the mouse coordinates
                    int value = (int) point.getX();

                    while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter1) < value) counter1++;
                    counter1--;

                    int value2 = (int) (point.getY());
                    while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter2) < value2) counter2++;
                    counter2--;

                    // if left button released
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        // calls the left click method
                        currentlyPlacingShip = false;
                        leftClick(shipNumber, counter1, counter2);
                        // if right button released
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        rightClick(shipNumber, counter1, counter2);
                    }
                    endSetup.repaint();
                }
            });


        }
    }

    /**
     * Gets called when right click is pressed on a ship panel. Attempts to
     * rotate the panel
     */
    private void rightClick(int shipNumber, int x, int y) {
        // isVertical is set based on the layout of the panel (X or y axis)
        boolean isVertical = false;
        if (((BoxLayout) panelArray[shipNumber].getLayout()).getAxis() == BoxLayout.Y_AXIS) isVertical = true;
        // calls the remove method to remove the ship (not the panel)
        removeShipFromBoardArray(shipArray[shipNumber]);

        // attempts to rotate the panel.
        if (rotatePanel(panelArray[shipNumber]) && !currentlyPlacingShip) {
            // if it works call the add method to add the ship pieces in the new orientation
            addShipToBoardArray(shipArray[shipNumber], new Point(x, y), !isVertical);
        } else if (!currentlyPlacingShip) {
            panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
            rotatePanel(panelArray[shipNumber]);
        }
        showFinishButton();
    }

    /**
     * Gets called when left mouse is pressed on a ship panel
     */
    private void leftClick(int shipNumber, int x, int y) {
        // if the panel has an X_AXIS box layout
        if ((((BoxLayout) panelArray[shipNumber].getLayout()).getAxis() == BoxLayout.X_AXIS)) {
            // checks if the panel is on the grid
            if (x < boardArray.length - panelArray[shipNumber].getWidth() / TILE_SIZE + 1 && x >= 0) {
                if (y < boardArray[0].length - panelArray[shipNumber].getHeight() / TILE_SIZE + 1 && y >= 0) {
                    // calls the method to place a ship panel on the proper place on the grid image
                    placeShipPanelOnBoard(x, y, shipNumber, false);
                } else {
                    // sets the location back to its starting position
                    panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
                    // removes the panel from the array
                    removeShipFromBoardArray(shipArray[shipNumber]);
                }
            } else {
                // sets the location back to the starting position
                panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
                // removes the panel from the array
                removeShipFromBoardArray(shipArray[shipNumber]);
            }
        } else {
            // checks if the panel is on the grid
            if (x < boardArray.length - panelArray[shipNumber].getWidth() / TILE_SIZE + 1 && x >= 0) {
                if (y < boardArray[0].length - panelArray[shipNumber].getHeight() / TILE_SIZE + 1 && y >= 0) {
                    // calls the method to place a ship panel on the proper place on the grid image
                    placeShipPanelOnBoard(x, y, shipNumber, true);
                } else {
                    // rotates the panel so it is along the x axis
                    rotatePanel(panelArray[shipNumber]);
                    // sets the location back to the starting position
                    panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
                    // removes the panel from the array
                    removeShipFromBoardArray(shipArray[shipNumber]);
                }
            } else {
                // rotates the panel so it is along the x axis
                rotatePanel(panelArray[shipNumber]);
                // sets the location back to the starting position
                panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
                // removes the panel from the array
                removeShipFromBoardArray(shipArray[shipNumber]);
            }
        }
        showFinishButton();
    }

    private void placeShipPanelOnBoard(int x, int y, int shipNumber, boolean isVertical) {
        // Set the location
        panelArray[shipNumber].setLocation(X_ORIGIN + x + (((TILE_SIZE + BORDER_SIZE) * x) + BORDER_SIZE / 2),
                Y_ORIGIN + y + ((TILE_SIZE + BORDER_SIZE) * y) + BORDER_SIZE / 2);
        // Check for intersection with another panel
        if (isIntersection(panelArray[shipNumber])) {
            if (isVertical) rotatePanel(panelArray[shipNumber]);
            removeShipFromBoardArray(shipArray[shipNumber]);
            // Set the panel location to its original location
            panelArray[shipNumber].setLocation(shipArray[shipNumber].getStartingPosition());
        } else {
            removeShipFromBoardArray(shipArray[shipNumber]);
            addShipToBoardArray(shipArray[shipNumber], new Point(x, y), isVertical);
        }
    }

    private void showFinishButton() {
        boolean showFinishButton = true;
        if (!currentlyPlacingShip) {
            for (int i = 0; i < shipArray.length; i++) {
                if (shipArray[i].getStartingPosition().equals(panelArray[i].getLocation())) {
                    showFinishButton = false;
                }
            }
            endSetup.setVisible(showFinishButton);
        }
    }

    private boolean isIntersection(JPanel jPanel) {
        for (JPanel aPanelArray : panelArray) {
            if (jPanel.getBounds().intersects(aPanelArray.getBounds()) && !jPanel.equals(aPanelArray)) return true;
        }
        return false;
    }

    private void removeShipFromBoardArray(Ship ship) {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                for (int k = 0; k < ship.getShipPieces().length; k++) {
                    if (boardArray[j][i] == ship.getShipPieces()[k]) boardArray[j][i] = 1;
                }
            }
        }
    }

    private void addShipToBoardArray(Ship ship, Point location, boolean isVertical) {
        if (location.getX() < boardArray.length && location.getX() >= 0 && location.getY() < boardArray.length &&
                location.getY() >= 0) {
            for (int i = 0; i < ship.getShipPieces().length; i++) {
                if (isVertical) boardArray[(int) location.getX()][(int) location.getY() + i] = ship.getShipPieces()[i];
                else boardArray[(int) location.getX() + i][(int) location.getY()] = ship.getShipPieces()[i];
            }
        }
    }

    private boolean rotatePanel(JPanel jPanel) {
        // if the panel has an x axis box layout
        if (((BoxLayout) jPanel.getLayout()).getAxis() == BoxLayout.X_AXIS) {
            if (jPanel.getX() > X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * boardArray.length) && !currentlyPlacingShip) {
                return false;
            }

            // set the layout to y axis
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

            // swap the width and height
            jPanel.setSize(jPanel.getHeight(), jPanel.getWidth());

            // replaces all x axis padding with y axis padding between the ship piece pictures
            for (int i = 0; i < jPanel.getComponentCount(); i++) {
                if (!jPanel.getComponent(i).getClass().toString().equals("JPanel")) {
                    jPanel.add(Box.createRigidArea(new Dimension(0, BORDER_SIZE + 2)), i);
                    jPanel.remove(++i);
                }
            }
            jPanel.add(Box.createRigidArea(new Dimension(0, 0)), 0);
            jPanel.remove(1);

            // re-validate the panel, forcing the layout to update
            jPanel.validate();

            // sets the panel location
            jPanel.setLocation(jPanel.getX(), jPanel.getY());

            // Length of the ship
            int counter = 0;
            while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter) < jPanel.getY() + jPanel.getWidth()) counter++;
            counter--;

            // if the panel is intersecting another ship panel or is partially off the grid
            if (!(counter <= boardArray[0].length - jPanel.getHeight() / TILE_SIZE && counter >= 0) ||
                    isIntersection(jPanel)) {
                // return that the rotation was a failure
                return false;
            }

            // if the panel has a y axis box layout
        } else if (((BoxLayout) jPanel.getLayout()).getAxis() == BoxLayout.Y_AXIS) {
            // set the layout to y axis
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

            // swap the width and height
            jPanel.setSize(jPanel.getHeight(), jPanel.getWidth());

            // replaces all y axis padding with x axis padding between the ship piece pictures
            for (int i = 0; i < jPanel.getComponentCount(); i++) {
                if (!jPanel.getComponent(i).getClass().toString().equals("JPanel")) {
                    jPanel.add(Box.createRigidArea(new Dimension(BORDER_SIZE + 2, 0)), i);
                    jPanel.remove(++i);
                }
            }

            jPanel.add(Box.createRigidArea(new Dimension(0, 0)), 0);
            jPanel.remove(1);

            // re-validate the panel, forcing the layout to update
            jPanel.validate();

            // sets the panel location
            jPanel.setLocation(jPanel.getX(), jPanel.getY());

            // Length of the ship
            int counter = 0;
            while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter) < jPanel.getX() + jPanel.getHeight()) counter++;
            counter--;

            // if the panel is intersecting another ship panel or is partially off the grid
            if (!(counter <= boardArray.length - jPanel.getWidth() / TILE_SIZE && counter >= 0)
                    || isIntersection(jPanel)) {
                return false;
            }

        }
        // return that the rotation was a success
        return true;
    }

    private void addRandomizeButton(int buttonXPosition) {
        randomizeShipsButton = new JButton("Randomize Board");
        randomizeShipsButton.setBounds(buttonXPosition, 0, window.getWidth() - buttonXPosition, TILE_SIZE - 5);
        randomizeShipsButton.addActionListener(e -> {
            Random random = new Random();
            for (int i = 0; i < panelArray.length; i++) {
                panelArray[i].setLocation(shipArray[i].getStartingPosition());
            }

            for (int i = 0; i < panelArray.length; i++) {
                int timeout = 0;
                while (timeout++ < 500 && shipArray[i].getStartingPosition().equals(panelArray[i].getLocation())) {
                    int x = random.nextInt(boardArray.length);
                    int y = random.nextInt(boardArray.length);
                    leftClick(i, x, y);
                    if (random.nextInt(2) == 0 &&
                            !shipArray[i].getStartingPosition().equals(panelArray[i].getLocation())) {
                        rightClick(i, x, y);
                    }
                }
            }
        });
        add(randomizeShipsButton);
        randomizeShipsButton.setVisible(true);
        repaint();
    }

    private void addCompleteSetupButton(int buttonXPosition) {
        endSetup = new JButton("Setup Complete");
        endSetup.setBounds(buttonXPosition, randomizeShipsButton.getHeight(), window.getWidth() - buttonXPosition,
                TILE_SIZE - 5);
        endSetup.addActionListener(e -> completeSetup = true);
        add(endSetup);
        endSetup.setVisible(false);
    }

    public void triggerRandomButton() {
        randomizeShipsButton.doClick();
    }

    public void triggerEndSetupButton() {
        endSetup.doClick();
    }

    public boolean isCompleteSetup() {
        return completeSetup;
    }

    public Object[][] getBoardArray() {
        return boardArray;
    }
}
