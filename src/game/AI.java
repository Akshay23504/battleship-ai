package game;

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
}
