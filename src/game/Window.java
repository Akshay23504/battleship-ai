package game;

import board.*;
import ship.Ship;
import ship.ShipPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

public class Window {

    private JFrame frame;
    private final int windowWidth = 900;
    private final int windowHeight = 600;

    public static int numberOfBattleships, lengthOfBattleships;
    public static int numberOfCruisers, lengthOfCruisers;
    public static int numberOfDestroyers, lengthOfDestroyers;
    public static int numberOfSubmarines, lengthOfSubmarines;
    public static int boardSize;
    private boolean gameRunning;
    public static Mode gameMode;
    enum AlgorithmSelector {
        Algorithm1,
        Algorithm2,
        Algorithm3,
        Algorithm4
    }

    private AlgorithmSelector algorithmSelector = AlgorithmSelector.Algorithm4;

    private JLabel playersTurn;
    private boolean playerOneTurn;
    private final String playerOneTurnString = "Player 1's turn";
    private final String playerTwoTurnString = "Player 2's turn";

    public void setupMenu() {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.setMinimumSize(new Dimension(windowWidth, windowHeight));
        frame.setResizable(false);
        frame.pack();
        Menu menu = new Menu(frame);
        menu.setup();
        while (!menu.isReadyToStart()) {}
        gameRunning = true;
        setupBoard();
    }

    public void setupBoard() {
        Ship[] playerOneShips = setupShips(true);
        Ship[] playerTwoShips = setupShips(false);

        Board board = new Board(selectShipPositions(playerOneShips, true));
        SmallBoard smallBoard = new SmallBoard(selectShipPositions(playerTwoShips, false));
        smallBoard.setLocation(board.getWidth() + 10, board.getY());

        playersTurn = new JLabel(playerTwoTurnString);
        playersTurn.setForeground(Color.WHITE);
        playersTurn.setSize(100, 100);
        playersTurn.setLocation(board.getWidth() + 10, smallBoard.getHeight() + 10);
        playersTurn.setVisible(true);

        frame.setPreferredSize(new Dimension(smallBoard.getX() + smallBoard.getWidth() + 10, frame.getHeight()));
        frame.setSize(frame.getPreferredSize());
        frame.pack();
        frame.getContentPane().add(board);
        frame.getContentPane().add(smallBoard);
        frame.getContentPane().add(playersTurn);
        frame.addMouseListener(board);
        frame.setVisible(true);

        beginGame(playerOneShips, playerTwoShips, board, smallBoard);
    }

    private Ship[] setupShips(boolean isPlayerOne) {
        Ship[] battleships = createShips(numberOfBattleships, lengthOfBattleships, isPlayerOne);
        Ship[] cruisers = createShips(numberOfCruisers, lengthOfCruisers, isPlayerOne);
        Ship[] destroyers = createShips(numberOfDestroyers, lengthOfDestroyers, isPlayerOne);
        Ship[] submarines = createShips(numberOfSubmarines, lengthOfSubmarines, isPlayerOne);

        Ship[] playerShips = concatShipArray(battleships, cruisers);
        playerShips = concatShipArray(playerShips, destroyers);
        playerShips = concatShipArray(playerShips, submarines);
        return playerShips;
    }

    private Ship[] createShips(int numberOfShips, int shipSize, boolean isPlayerOne) {
        Ship[] ships = new Ship[numberOfShips];
        for (int i = 0; i < numberOfShips; i++) {
            ShipPiece[] shipArray = new ShipPiece[shipSize];
            for (int j = 0; j < shipSize; j++) {
                ShipPiece shipPiece = new ShipPiece(isPlayerOne);
                shipArray[j] = shipPiece;
            }
            ships[i] = new Ship(shipArray);
        }
        return ships;
    }

    private Ship[] concatShipArray(Ship[] a, Ship[] b) {
        Ship[] ship = new Ship[a.length + b.length];
        System.arraycopy(a, 0, ship, 0, a.length);
        System.arraycopy(b, 0, ship, a.length, b.length);
        return ship;
    }

    private Object[][] selectShipPositions(Ship[] ships, boolean isPlayerOne) {
        Creator creator = new Creator(ships, boardSize, frame);
        creator.setup();
        frame.getContentPane().add(creator);
        frame.getContentPane().repaint();
        frame.setVisible(true);

        switch (gameMode) {
            case HumanVsAI:
                if (!isPlayerOne) {
                    creator.triggerRandomButton();
                    creator.triggerEndSetupButton();
                }
                break;
            case AIVsAI:
                creator.triggerRandomButton();
                creator.triggerEndSetupButton();
            case HumanVsHuman:
            default:
                break;
        }

        while (!creator.isCompleteSetup()) {}
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        return creator.getBoardArray();
    }

    private void beginGame(Ship[] playerOneShips, Ship[] playerTwoShips, Board board, SmallBoard smallBoard) {
        changeTurns(board, smallBoard);
        setPlayerOneTurn(false);
        Algorithm1 playerOneAlgorithm1KB = null;
        Algorithm1 playerTwoAlgorithm1KB = null;
        Algorithm2 playerOneAlgorithm2KB = null;
        Algorithm2 playerTwoAlgorithm2KB = null;
        Algorithm3 playerOneAlgorithm3KB = null;
        Algorithm3 playerTwoAlgorithm3KB = null;

        switch (algorithmSelector) {
            case Algorithm1:
                playerOneAlgorithm1KB = new Algorithm1();
                playerTwoAlgorithm1KB = new Algorithm1();
                playerOneAlgorithm1KB.initializeScoreMap();
                playerTwoAlgorithm1KB.initializeScoreMap();
                break;
            case Algorithm2:
                playerTwoAlgorithm2KB = new Algorithm2();
                playerOneAlgorithm2KB = new Algorithm2();
                break;
            case Algorithm3:
                playerTwoAlgorithm3KB = new Algorithm3();
                playerOneAlgorithm3KB = new Algorithm3();
                break;
            case Algorithm4:
                break;
        }

        int[] indices = new int[2];

        while (gameRunning) {
            switch (gameMode) {
                case HumanVsAI:
                    if (!playerOneTurn) {
                        switch (algorithmSelector) {
                            case Algorithm1:
                                indices = AI.algorithm1(playerTwoAlgorithm1KB);
                                int result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                while (result == 0) {
                                    indices = AI.algorithm1(playerTwoAlgorithm1KB);
                                    result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                }
                                playerTwoAlgorithm1KB.updateEnemyGrid(indices[0], indices[1], result);
                                playerTwoAlgorithm1KB.updateScoreMap(indices[0], indices[1]);
                                break;
                            case Algorithm2:
                                if (playerTwoAlgorithm2KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm2(playerTwoAlgorithm2KB);
                                } else {
                                    Point point = playerTwoAlgorithm2KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerTwoAlgorithm2KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm3:
                                if (playerTwoAlgorithm3KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm3(playerTwoAlgorithm3KB);
                                } else {
                                    Point point = playerTwoAlgorithm3KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerTwoAlgorithm3KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm4:
                                indices = AI.algorithm4(board.getArray().length);
                                while (board.selectedPositionOnBoardByPlayer(indices[0], indices[1]) == 0) {
                                    indices = AI.algorithm4(board.getArray().length);
                                }
                                break;
                        }
                        MouseEvent mouseEvent = new MouseEvent(frame, 0, 0, 0, indices[0], indices[1], 1, false);
                        for (MouseListener mouseListener : frame.getMouseListeners()) {
                            mouseListener.mousePressed(mouseEvent);
                        }
                    }
                    break;
                case AIVsAI:
                    if (!playerOneTurn) {
                        switch (algorithmSelector) {
                            case Algorithm1:
                                indices = AI.algorithm1(playerTwoAlgorithm1KB);
                                int result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                while (result == 0) {
                                    indices = AI.algorithm1(playerTwoAlgorithm1KB);
                                    result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                }
                                playerTwoAlgorithm1KB.updateEnemyGrid(indices[0], indices[1], result);
                                playerTwoAlgorithm1KB.updateScoreMap(indices[0], indices[1]);
                                break;
                            case Algorithm2:
                                if (playerTwoAlgorithm2KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm2(playerTwoAlgorithm2KB);
                                } else {
                                    Point point = playerTwoAlgorithm2KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerTwoAlgorithm2KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm3:
                                if (playerTwoAlgorithm3KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm3(playerTwoAlgorithm3KB);
                                } else {
                                    Point point = playerTwoAlgorithm3KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerTwoAlgorithm3KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm4:
                                indices = AI.algorithm4(board.getArray().length);
                                while (board.selectedPositionOnBoardByPlayer(indices[0], indices[1]) == 0) {
                                    indices = AI.algorithm4(board.getArray().length);
                                }
                                break;
                        }
                    } else {
                        switch (algorithmSelector) {
                            case Algorithm1:
                                indices = AI.algorithm1(playerOneAlgorithm1KB);
                                int result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                while (result == 0) {
                                    indices = AI.algorithm1(playerOneAlgorithm1KB);
                                    result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                }
                                playerOneAlgorithm1KB.updateEnemyGrid(indices[0], indices[1], result);
                                playerOneAlgorithm1KB.updateScoreMap(indices[0], indices[1]);
                                break;
                            case Algorithm2:
                                if (playerOneAlgorithm2KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm2(playerOneAlgorithm2KB);
                                } else {
                                    Point point = playerOneAlgorithm2KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerOneAlgorithm2KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm3:
                                if (playerOneAlgorithm3KB.getQueue().isEmpty()) {
                                    indices = AI.algorithm3(playerOneAlgorithm3KB);
                                } else {
                                    Point point = playerOneAlgorithm3KB.getQueue().poll();
                                    indices[0] = point.x;
                                    indices[1] = point.y;
                                }
                                result = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                                if (result == 1) playerOneAlgorithm3KB.update(indices[0], indices[1]);
                                break;
                            case Algorithm4:
                                indices = AI.algorithm4(board.getArray().length);
                                while (board.selectedPositionOnBoardByPlayer(indices[0], indices[1]) == 0) {
                                    indices = AI.algorithm4(board.getArray().length);
                                }
                                break;
                        }
                    }
                    board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                    /*try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/
                    MouseEvent mouseEvent = new MouseEvent(frame, 0, 0, 0, indices[0], indices[1], 1, false);
                    for (MouseListener mouseListener : frame.getMouseListeners()) {
                        mouseListener.mousePressed(mouseEvent);
                    }
                    break;
                case HumanVsHuman:
                default:
                    break;

            }
            boolean playerOneAllShipsDead = true;

            for (int i = 0; i < playerOneShips.length; i++) {
                if (playerOneShips[i].checkIfDead()) {
                    for (int j = 0; j < playerOneShips[i].getShipPieces().length; j++) {
                        playerOneShips[i].getShipPieces()[j].setShipImage("dead.png");
                    }
                } else {
                    playerOneAllShipsDead = false;
                }
            }

            board.repaint();
            smallBoard.repaint();

            boolean playerTwoAllShipsDead = true;
            for (int i = 0; i < playerTwoShips.length; i++) {
                if (playerTwoShips[i].checkIfDead()) {
                    for (int j = 0; j < playerTwoShips[i].getShipPieces().length; j++) {
                        playerTwoShips[i].getShipPieces()[j].setShipImage("dead.png");
                    }
                } else {
                    playerTwoAllShipsDead = false;
                }
            }

            board.repaint();
            smallBoard.repaint();

            if (playerOneAllShipsDead || playerTwoAllShipsDead) {
                gameRunning = false;
                for (int i = 0; i < board.getArray().length; i++) {
                    for (int j = 0; j < board.getArray()[i].length; j++) {
                        if ((board.getArray()[i][j].equals(1))) {
                            board.getArray()[i][j] = 0;
                        }
                    }
                }
                new End(frame, !playerOneAllShipsDead).loadEndScreen();
            }
        }
    }

    private void changeTurns(Board board, SmallBoard smallBoard) {
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ChangeTurns changeTurns = new ChangeTurns((JPanel) frame.getContentPane(), board, smallBoard);
                final Object[][] board1Temp = board.getArray();
                final Object[][] board2Temp = smallBoard.getArray();
                if (!board.isTurn() && gameRunning) {
                    board.setVisible(false);
                    smallBoard.setVisible(false);
                    board.setArray(board2Temp);
                    smallBoard.setArray(board1Temp);
                    changeTurns.loadChangeTurnScreen(gameMode, playerOneTurn);
                    setPlayerOneTurn(!playerOneTurn);
                    if (playerOneTurn) playersTurn.setText(playerOneTurnString);
                    else playersTurn.setText(playerTwoTurnString);
                }
            }
        });
    }

    private void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

}
