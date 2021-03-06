package board;

import game.Window;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Hunt and Target
 */
public class Algorithm2 {

    private Queue<Point> queue;
    private int customBoard[][];

    /**
     * Constructor to initialize the customBoard and all values are set to 0
     */
    public Algorithm2() {
        customBoard = new int[Window.boardSize][Window.boardSize];
        queue = new LinkedList<>();
        for (int i = 0; i < customBoard.length; i++) {
            for (int j = 0; j < customBoard[0].length; j++) {
                customBoard[i][j] = 0;
            }
        }
    }

    /**
     * Add possible coordinates to the queue surrounding (x, y)
     * @param x x coordinate
     * @param y y coordinate
     */
    public void update(int x, int y) {
        if ((x - 1) >= 0 && this.customBoard[x - 1][y] != 1) {
            queue.add(new Point(x - 1, y));
        }
        if ((x + 1) < Window.boardSize && this.customBoard[x + 1][y] != 1) {
            queue.add(new Point(x + 1, y));
        }
        if ((y - 1) >= 0 && this.customBoard[x][y - 1] != 1) {
            queue.add(new Point(x, y - 1));
        }
        if ((y + 1) < Window.boardSize && this.customBoard[x][y + 1] != 1) {
            queue.add(new Point(x, y + 1));
        }
    }

    public Queue<Point> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Point> queue) {
        this.queue = queue;
    }

    public int[][] getCustomBoard() {
        return customBoard;
    }

    public void setCustomBoard(int[][] customBoard) {
        this.customBoard = customBoard;
    }
}
