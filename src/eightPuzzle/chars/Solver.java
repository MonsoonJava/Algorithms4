package eightPuzzle.chars;

import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private static final Comparator<Node> BY_PRIORITY = new ByPriority();

    private boolean unsolvable = false;

    private ArrayList<Board> stepList = null;

    private int moves = 0;

    private class Node {

        private Node father;

        private Board board;

        private int moves;

        private int priority;

        private int manhaPriority;

        Node(Node father, Board board, int moves) {
            this.father = father;
            this.board = board;
            this.moves = moves;
            this.manhaPriority = board.manhattan();
            this.priority = moves + manhaPriority;
        }
    }

    private static class ByPriority implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.priority == o2.priority ?  (o1.manhaPriority == o2.manhaPriority ? 0 : o1.manhaPriority > o2.manhaPriority ? 1 : -1): (o1.priority > o2.priority ? 1 : -1);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (null == initial) {
            throw new NullPointerException();
        }
        MinPQ<Node> boardPQ = new MinPQ<Node>(BY_PRIORITY);

        MinPQ<Node> twinPQ = new MinPQ<Node>(BY_PRIORITY);

        ArrayList<Node> closes = new ArrayList<Node>();

        ArrayList<Node> twinCloses = new ArrayList<Node>();

        Node goalNode = null;

        boardPQ.insert(new Node(null, initial, 0));
        // 設置twin的初始隊列，用於檢測是否unsolvable
        twinPQ.insert(new Node(null, initial.twin(), 0));
        while (!boardPQ.isEmpty() && !twinPQ.isEmpty()) {
            Node minNode = boardPQ.delMin();
            Node minTwinNode = twinPQ.delMin();
            // maintain close list
            closes.add(minNode);
            twinCloses.add(minTwinNode);
            if (minNode.board.isGoal()) {
                break;
            }
            if (minTwinNode.board.isGoal()) {
                unsolvable = true;
                break;
            }
            Iterable<Board> neighbors = minNode.board.neighbors();
            Iterable<Board> twinNeighbors = minTwinNode.board.neighbors();
            // 添加不重复的neighbor
            appendNeighbors(minNode, neighbors, closes, boardPQ);
            appendNeighbors(minTwinNode, twinNeighbors, twinCloses, twinPQ);
        }
        if (!unsolvable) {
            goalNode = closes.get(closes.size() - 1);
            moves = goalNode.moves;
            ArrayList<Board> toReserve = new ArrayList<Board>();
            while (true) {
                toReserve.add(goalNode.board);
                if(goalNode.father == null){
                    break;
                }
                goalNode = goalNode.father;
            }
            stepList = new ArrayList<Board>(toReserve.size());
            for (int i = toReserve.size() - 1; i >= 0; i--) {
                stepList.add(toReserve.get(i));
            }
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return !unsolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!unsolvable) {
            return moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return stepList;
    }

    private void appendNeighbors(Node parentNode, Iterable<Board> boards, ArrayList<Node> closes,
            MinPQ<Node> PQ) {
        if (closes != null && closes.size() > 0) {
            for (Board board : boards) {
                boolean isExist = false;
                for (Node node : closes) {
                    if(node.board.dimension() != board.dimension() && node.board.manhattan() != board.manhattan()){
                        continue;
                    }
                    if (node.board.equals(board)) {
                        isExist = true;
                        continue;
                    }
                }
                if (!isExist) {
                    PQ.insert(new Node(parentNode, board, parentNode.moves + 1));
                }
            }
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
