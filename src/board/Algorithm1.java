package board;

import game.Window;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Algorithm1 {

    private int[][] scoreMap = new int[Window.boardSize][Window.boardSize];
    private ScoreMap[][] scores = new ScoreMap[Window.boardSize][Window.boardSize];
    private int[][] movesMade = new int[Window.boardSize][Window.boardSize];
    private int[][] enemyGrid = new int[Window.boardSize][Window.boardSize];
    private int[][] top6 = new int[6][2];
    public Point lastHit;
    public Point hitBeforeLast;
    public Queue<Point> possibleCoordinates = new LinkedList<>();
    public List<Point> hitList = new ArrayList<>();
    private final int shotFired = 1;
    private final int shipHit = 2;
    private boolean goVertical = false;
    private boolean goHorizontal = false;
    public Point keyCoordinate = new Point(-1, -1);
    private int state = 0; // 0 - default, 1 - first hit, 2 - find direction, 3 - right direction, 4 - left direction
    int x;
    int y;
    int xMin;
    int xMax;
    int yMin;
    int yMax;

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
                enemyGrid[i][j] = 0; // Nothing is hit or miss
                movesMade[i][j] = 0;
            }
        }
        lastHit = new Point(-1, -1);
        hitBeforeLast = new Point(-1, -1);
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

    public int[] findTarget() {
        System.out.println(getPossibleCoordinates());
        if (lastHit.x != -1 && hitBeforeLast.x == -1 && state == 0) {
            state = 1;
            keyCoordinate = lastHit;
            clearPossibleCoordinates();
            if (movesMade[lastHit.x + 1][lastHit.y] == 0) {
                setPossibleCoordinates(new Point(lastHit.x, lastHit.y + 1));
            }
            if (movesMade[lastHit.x][lastHit.y + 1] == 0) {
                setPossibleCoordinates(new Point(lastHit.x + 1, lastHit.y));
            }
            if (movesMade[lastHit.x - 1][lastHit.y] == 0) {
                setPossibleCoordinates(new Point(lastHit.x, lastHit.y - 1));
            }
            if (movesMade[lastHit.x][lastHit.y - 1] == 0) {
                setPossibleCoordinates(new Point(lastHit.x - 1, lastHit.y));
            }
            System.out.println("State: " + state);
            Point result = getPointFromPossibleCoordinates();
            return new int[]{result.x, result.y};
        } else if ((state == 1 && lastHit.x != -1) || state > 1) {
            if (state == 1) state = 2;
        /*if (hitList.size() > 0) {
            lastHit = hitList.get(0);
        } else if (hitList.size() == 1) {

        }
        if (hitList.size() > 1) {
            hitBeforeLast = hitList.get(1);
        } else {
            hitBeforeLast = lastHit;
        }*/
            if (state == 2) {
                clearPossibleCoordinates();
                findDirection();
                addPossibleCoordinates();
                state = 3;
            }
            System.out.println("State: " + state);
            System.out.println(getPossibleCoordinates());
            if (getPossibleCoordinates().size() == 0) return null;
            Point result = getPointFromPossibleCoordinates();
            return new int[]{result.x, result.y};
        } else {
            System.out.println("State: " + state);
            System.out.println(getPossibleCoordinates());
            Point result = getPointFromPossibleCoordinates();
            if (result == null) return null;
            return new int[]{result.x, result.y};
        }
    }

    private void findDirection() {
        x = lastHit.x;
        y = lastHit.y;
        xMin = x - scoreMap.length;
        xMax = x + scoreMap.length;
        yMin = y - scoreMap.length;
        yMax = y + scoreMap.length;

        if (xMin < 0) xMin = 0;
        if (xMax > scoreMap.length) xMax = scoreMap.length;
        if (yMin < 0) yMin = 0;
        if (yMax > scoreMap.length) yMax = scoreMap.length;


        if (keyCoordinate.x == x) {
//            if (Math.abs(hitBeforeLast.y - y) == 1) {
//                goVertical = true;
//            }
            goVertical = true;
        }
        if (keyCoordinate.y == y) {
//            if (Math.abs(hitBeforeLast.x - x) == 1) {
//                goHorizontal = true;
//            }
            goHorizontal = true;
        }
    }

    private void addPossibleCoordinates() {
        if (goHorizontal) {
            for (int ix = x - 1; ix < xMax; ix++) {
                if (enemyGrid[y][ix] == shotFired || ix <= 0) continue;
                if (enemyGrid[y][ix] != shipHit) {
                    if (!getPossibleCoordinates().contains(new Point(ix, y)) && movesMade[ix][y] == 0) {
                        setPossibleCoordinates(new Point(ix - 1, y));
                    }
                }
            }
        }

        if (goHorizontal) {
            for (int ix = x+1; ix >= xMin; ix--) {
                if (enemyGrid[y][ix] == shotFired || ix >= Window.boardSize) continue;
                if (enemyGrid[y][ix] != shipHit) {
                    if (!getPossibleCoordinates().contains(new Point(ix, y)) && movesMade[ix][y] == 0) {
                        setPossibleCoordinates(new Point(ix - 1, y));
                    }
                }
            }
        }

        if (goVertical) {
            for (int iy = y-1; iy < yMax; iy++) {
                if (enemyGrid[iy][x] == shotFired || iy <= 0) continue;
                if (enemyGrid[iy][x] != shipHit) {
                    if (!getPossibleCoordinates().contains(new Point(x, iy)) && movesMade[x][iy] == 0) {
                        setPossibleCoordinates(new Point(x, iy - 1));
                    }
                }
            }
        }

        if (goVertical) {
            for (int iy = y+1; iy >= yMin; iy--) {
                if (enemyGrid[iy][x] == shotFired || iy >= Window.boardSize) continue;
                if (enemyGrid[iy][x] != shipHit) {
                    if (!getPossibleCoordinates().contains(new Point(x, iy)) && movesMade[x][iy] == 0) {
                        setPossibleCoordinates(new Point(x, iy - 1));
                    }
                }
            }
        }
        System.out.println("Inside Add Possible");
        System.out.println(getPossibleCoordinates());
    }

    public void updateEnemyGrid(int x, int y, int result) {
        enemyGrid[x][y] = result;
        if (result == 1) {
            hitList.add(0, new Point(x, y));
            hitBeforeLast = lastHit;
            lastHit = new Point(x, y);
        } else if (result == 2) {
            hitBeforeLast = lastHit;
            lastHit = new Point(-1, -1);
            if (lastHit.x == -1 && hitBeforeLast.x != -1 && state == 3) {
                System.out.println("Came here333");
                updatePossibleCoordinates();
                state = 4;
            } else if (lastHit.x == -1 && hitBeforeLast.x == -1 && state == 4) {
                System.out.println("Came here444");
                state = 0;
                clearPossibleCoordinates();
            } /*else if (lastHit.x == -1 && hitBeforeLast.x == -1 && (state != 3)) {
                System.out.println("Came here000");
                clearPossibleCoordinates();
            }*/
            updatePossibleCoordinates();
        }
    }

    private void updatePossibleCoordinates() {
        getPossibleCoordinates().removeIf(point -> {
            if (goHorizontal) {
                if (point.y != hitBeforeLast.y || point.x + 1 >= hitBeforeLast.x || point.y < 0 || point.x < 0) {
                    return true;
                }
            } else if (goVertical) {
                if (point.x != hitBeforeLast.x || point.y + 1 >= hitBeforeLast.y || point.y < 0 || point.x < 0) {
                    return true;
                }
            }
            return false;
        });
    }

    public void displayScoreMap() {
        for (int i = 0; i < scoreMap.length; i++) {
            for (int j = 0; j < scoreMap[0].length; j++) {
                System.out.print(scoreMap[i][j] + "    ");
            }
            System.out.println();
        }
    }

    public void clearPossibleCoordinates() {
        possibleCoordinates.clear();
    }

    public Point getPointFromPossibleCoordinates() {
        return possibleCoordinates.poll();
    }

    public Queue<Point> getPossibleCoordinates() {
        return possibleCoordinates;
    }

    public void setPossibleCoordinates(Point point) {
        this.possibleCoordinates.add(point);
    }

    public void setMovesMade(int x, int y) {
        this.movesMade[x][y] = 1;
    }

    public int getMovesMade(int x, int y) {
        return movesMade[x][y];
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
