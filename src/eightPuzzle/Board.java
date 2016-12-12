package eightPuzzle;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private final int dimension;

    private final int[][] grids;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        dimension = blocks.length;
        grids = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grids[i][j] = blocks[i][j];
            }
        }
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int incorrectNum = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grids[i][j] == 0) {
                    continue;
                }
                if (grids[i][j] != (i * dimension + j + 1)) {
                    incorrectNum++;
                }
            }
        }
        return incorrectNum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan_dis = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grids[i][j] == 0) {
                    continue;
                }
                if (grids[i][j] != (i * dimension + j + 1)) {
                    int x_cor = grids[i][j] % dimension == 0? grids[i][j] / dimension - 1 : grids[i][j] / dimension;
                    int y_cor = grids[i][j] - x_cor * dimension - 1;
                    int x_dis = x_cor - i > 0 ? x_cor - i : i - x_cor;
                    int y_dis = y_cor - j > 0 ? y_cor - j : j - y_cor;
                    manhattan_dis += x_dis + y_dis;
                }
            }
        }
        return manhattan_dis;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Position positionOne = new Position(StdRandom.uniform(dimension), StdRandom.uniform(dimension));
        Position positionTwo = new Position(StdRandom.uniform(dimension), StdRandom.uniform(dimension));
        int[][] twin = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                twin[i][j] = grids[i][j];
            }
        }
        while (true) {
            if(twin[positionOne.x][positionOne.y] == 0 || positionOne.equals(positionTwo)){
                positionOne.x = StdRandom.uniform(dimension);
                positionOne.y = StdRandom.uniform(dimension);
            }
            if(twin[positionTwo.x][positionTwo.y] == 0 || positionOne.equals(positionTwo)){
                positionTwo.x = StdRandom.uniform(dimension);
                positionTwo.y = StdRandom.uniform(dimension);
            }
            if(!positionOne.equals(positionTwo)){
                break;
            }
        }
        int tempValue = twin[positionOne.x][positionOne.y];
        twin[positionOne.x][positionOne.y] = twin[positionTwo.x][positionTwo.y];
        twin[positionTwo.x][positionTwo.y] = tempValue;
        return new Board(twin);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if(null == y) return false;
        if (this == y)
            return true;
        if (y.getClass() == this.getClass()) {
            if (this.dimension != ((Board) y).dimension())
                return false;
            String other = ((Board) y).toString();
            String[] x_cor = other.split("\n");
            if (x_cor.length -1 != dimension)
                return false;
            for (int i = 0; i < x_cor.length - 1; i++) {
                String[] y_cor = x_cor[i + 1].trim().split(" ");
                if (y_cor.length != dimension)
                    return false;
                for (int j = 0; j < y_cor.length; j++) {
                    if (this.grids[i][j] != Integer.parseInt(y_cor[j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterator();
    }

    private class BoardIterator implements Iterable<Board> {

        private ArrayList<Board> neighbors = new ArrayList<>();

        BoardIterator() {
            Position zeroPosition = null;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (grids[i][j] == 0) {
                        zeroPosition = new Position(i, j);
                        break;
                    }
                }
            }
            // 0上放的Board
            if (zeroPosition.x > 0) {
                neighbors.add(oneNeighbour(zeroPosition, new Position(zeroPosition.x - 1, zeroPosition.y)));
            }
            // 0右侧的Board
            if (zeroPosition.y < dimension - 1) {
                neighbors.add(oneNeighbour(zeroPosition, new Position(zeroPosition.x, zeroPosition.y + 1)));
            }
            // 0下方的Board
            if (zeroPosition.x < dimension - 1) {
                neighbors.add(oneNeighbour(zeroPosition, new Position(zeroPosition.x + 1, zeroPosition.y)));
            }
            // 0左方的Board
            if (zeroPosition.y > 0) {
                neighbors.add(oneNeighbour(zeroPosition, new Position(zeroPosition.x, zeroPosition.y - 1)));
            }
        }

        @Override
        public Iterator<Board> iterator() {
            return neighbors.iterator();
        }

        private Board oneNeighbour(Position froPos, Position toPos) {
            int[][] copyGrids = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    copyGrids[i][j] = grids[i][j];
                }
            }
            int tempValue = copyGrids[froPos.x][froPos.y];
            copyGrids[froPos.x][froPos.y] = copyGrids[toPos.x][toPos.y];
            copyGrids[toPos.x][toPos.y] = tempValue;
            return new Board(copyGrids);
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            sb.append(" ");
            for (int j = 0; j < dimension; j++) {
                sb.append(grids[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] test = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        int[][] testeq = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        int[][] tess = {{0,2},{1,3}};
        Board board = new Board(test);
        Board boardd = new Board(testeq);
        System.out.println(board.equals(boardd));
        System.out.println(board);
        System.out.println(board.twin());
      /* System.out.println(board.dimension());
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board);
        System.out.println(board.twin());
        System.out.println(boardd);
        System.out.println(boardd.twin());*/
        
    }

    private class Position {
        private int x;

        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (super.equals(obj))
                return true;
            if (obj.getClass().equals(Position.class))
                return this.x == ((Position) obj).x && this.y == ((Position) obj).y;
            return false;
        }
    }

}