package game;

import board.Algorithm1;
import board.Algorithm2;

import java.awt.*;
import java.util.Random;

// TODO Change name probably
public class AI {

    public static int[] getRandomIndices(int n) {
        int[] indices = new int[2];
        Random random = new Random();
        indices[0] = random.nextInt(n);
        indices[1] = random.nextInt(n);
        return indices;
    }

    public static int[] dynamicProgramming(Algorithm1 playerKB) {
//        return playerKB.calculateTop6();
        int[] result;
        if (playerKB.lastHit.x == -1 && playerKB.hitBeforeLast.x == -1 && playerKB.getPossibleCoordinates().isEmpty()){ // state = 0
            result = playerKB.calculateTop6();
        } else {
            result = playerKB.findTarget();
            if (result == null) result = playerKB.calculateTop6();
        }
        int r = playerKB.getMovesMade(result[0], result[1]);
        while (r != 0) {
            if (playerKB.hitList.size() == 0) {
                result = playerKB.calculateTop6();
            } else {
                result = playerKB.findTarget();
                if (result == null) result = playerKB.calculateTop6();
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
        }while(playerKB.getCustomBoard()[indices[0]][indices[1]] == 1);

        playerKB.getCustomBoard()[indices[0]][indices[1]] = 1; // Mark as visited
        return indices;
    }

}
