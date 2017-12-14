package game;

import board.Algorithm1;
import board.Algorithm2;
import board.Algorithm3;

import java.awt.*;
import java.util.Random;

/**
 * Calls different algorithms based on the inputs(KB) received from the game
 * and all the methods return a valid coordinate
 */
public class AI {

    public static int[] algorithm1(Algorithm1 playerKB) {
        int[] result;
        if (playerKB.lastHit.x == -1 && playerKB.hitBeforeLast.x == -1 && playerKB.getPossibleCoordinates().isEmpty()){ // state = 0
            result = playerKB.calculateTop6();
        } else {
            result = playerKB.findTarget();
            if (result == null || result[0] < 0 || result[1] < 0) result = playerKB.calculateTop6();
        }
        int r = playerKB.getMovesMade(result[0], result[1]);
        while (r != 0) {
            if (playerKB.lastHit.x == -1 && playerKB.hitBeforeLast.x == -1 && playerKB.getPossibleCoordinates().isEmpty()) {
                result = playerKB.calculateTop6();
            } else {
                result = playerKB.findTarget();
                if (result == null || result[0] < 0 || result[1] < 0) result = playerKB.calculateTop6();
            }
            r = playerKB.getMovesMade(result[0], result[1]);
        }
        playerKB.setMovesMade(result[0], result[1]);
        return result;
    }

    public static int[] algorithm2(Algorithm2 playerKB) {
        int[] indices = new int[2];
        do {
            Random random = new Random();
            indices[0] = random.nextInt(Window.boardSize);
            indices[1] = random.nextInt(Window.boardSize);
        } while(playerKB.getCustomBoard()[indices[0]][indices[1]] == 1);
        playerKB.getCustomBoard()[indices[0]][indices[1]] = 1; // Mark as visited
        return indices;
    }

    public static int[] algorithm3(Algorithm3 playerKB) {
        int[] indices = new int[2];
        do {
            Random random = new Random();
            do {
                indices[1] = random.nextInt(Window.boardSize);
            } while (indices[1] < 0 || indices[1] >= Window.boardSize);
            if (indices[1] % 2 == 0) {
                do {
                    indices[0] = random.nextInt(Window.boardSize);
                } while (indices[0] % 2 == 0);
            } else {
                do {
                    indices[0] = random.nextInt(Window.boardSize);
                } while (indices[0] % 2 != 0);
            }
        } while(playerKB.getCustomBoard()[indices[0]][indices[1]] == 1);
        playerKB.getCustomBoard()[indices[0]][indices[1]] = 1; // Mark as visited
        return indices;
    }

    public static int[] algorithm4(int n) {
        int[] indices = new int[2];
        Random random = new Random();
        indices[0] = random.nextInt(n);
        indices[1] = random.nextInt(n);
        return indices;
    }

}
