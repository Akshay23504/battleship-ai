package board;

import game.Window;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm1 {

    private int[][] scoreMap = new int[Window.boardSize][Window.boardSize];
    private ScoreMap[][] scores = new ScoreMap[Window.boardSize][Window.boardSize];
    int[][] enemyGrid = new int[Window.boardSize][Window.boardSize];
    int[][] top6 = new int[6][2];
    List<Integer> hitList;
    Coordinates lastShot = new Coordinates(0, 0, 0);
    private final int shotFired = 1;
    private final int shipHit = 2;
    int x, y;

    // Initialize the score map here
    public void initializeScoreMap () {
        int ix, iy, jx, jy;
        for (int i = 0; i < scoreMap.length; i++) {
            for (int j = 0; j < scoreMap[0].length; j++) {
                ix = j + 1;
                iy = i + 1;
                jx = scoreMap.length - j;
                jy = scoreMap.length - i;
                scoreMap[i][j] = calculateScore(ix, iy, jx, jy);
                scores[i][j] = new ScoreMap(ix, iy, jx, jy);
            }
        }
    }

    public void updateScoreMap(int x, int y) {
        int counter = 1;
        scoreMap[x][y] = 0;
        scores[x][y].setIx(0);
        scores[x][y].setIy(0);
        scores[x][y].setJx(0);
        scores[x][y].setJy(0);

        for (int j = y - 1; j >= 0; j--) {
            scores[x][j].setJx(counter++);
            scoreMap[x][j] = calculateScore(
                    scores[x][j].getIx(), scores[x][j].getIy(), scores[x][j].getJx(), scores[x][j].getJy()
            );
        }
        counter = 1;
        for (int j = y + 1; j < scoreMap.length; j++) {
            scores[x][j].setIx(counter++);
            scoreMap[x][j] = calculateScore(
                    scores[x][j].getIx(), scores[x][j].getIy(), scores[x][j].getJx(), scores[x][j].getJy()
            );

        }
        counter = 1;
        for (int i = x - 1; i >= 0; i--) {
            scores[i][y].setJy(counter++);
            scoreMap[i][y] = calculateScore(
                    scores[i][y].getIx(), scores[i][y].getIy(), scores[i][y].getJx(), scores[i][y].getJy()
            );
        }
        counter = 1;
        for (int i = x + 1; i < scoreMap.length; i++) {
            scores[i][y].setIy(counter++);
            scoreMap[i][y] = calculateScore(
                    scores[i][y].getIx(), scores[i][y].getIy(), scores[i][y].getJx(), scores[i][y].getJy()
            );
        }
        calculateTop6();
    }

    public int[] calculateTop6() {
        Map<int[], Integer> map = new HashMap<>();
        for (int i = 0; i < scoreMap.length; i++) {
            for (int j = 0; j < scoreMap[0].length; j++) {
                map.put(new int[]{i, j}, scoreMap[i][j]);
            }
        }
        map = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int counter = 1;
        for (int[] i : map.keySet()) {
            if (counter > 6) break;
            top6[counter++ - 1] = i;
        }
        return top6[new Random().nextInt(top6.length)];
    }

    private int calculateScore(int ix, int iy, int jx, int jy) {
        return (ix + jx - (Math.abs(ix - jx))) * (iy + jy - (Math.abs(iy - jy)));
    }


    private class Coordinates {
        int x, y, val;

        public Coordinates(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getVal() {
            return val;
        }
    }

    private class ScoreMap {
        int ix, iy, jx, jy;

        public ScoreMap(int ix, int iy, int jx, int jy) {
            this.ix = ix;
            this.iy = iy;
            this.jx = jx;
            this.jy = jy;
        }

        public int getIx() {
            return ix;
        }

        public void setIx(int ix) {
            this.ix = ix;
        }

        public int getIy() {
            return iy;
        }

        public void setIy(int iy) {
            this.iy = iy;
        }

        public int getJx() {
            return jx;
        }

        public void setJx(int jx) {
            this.jx = jx;
        }

        public int getJy() {
            return jy;
        }

        public void setJy(int jy) {
            this.jy = jy;
        }
    }

}
