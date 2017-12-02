package board;

import game.Window;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Algorithm3 {

    private Queue<Point> queue;
    private int customBoard[][];

    public Algorithm3() {
        customBoard = new int[Window.boardSize][Window.boardSize];
        queue = new LinkedList<>();
        for (int i = 0; i < customBoard.length; i++) {
            for (int j = 0; j < customBoard[0].length; j++) {
                customBoard[i][j] = 0;
            }
        }
    }

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
