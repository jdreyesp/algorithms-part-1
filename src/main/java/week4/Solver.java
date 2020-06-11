package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Solver {

    private final class GameBoard implements Comparable<GameBoard>{
        private Board board;
        private int manhattan;
        private int moves;
        private int priority;

        private GameBoard(Board board, int manhattan, int moves, int priority) {
            this.board = board;
            this.manhattan = manhattan;
            this.moves = moves;
            this.priority = priority;
        }

        @Override
        public int compareTo(GameBoard o) {
            return this.priority - o.priority;
        }
    }

    private final MinPQ<GameBoard> leafNodes;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        leafNodes = new MinPQ();
        int manhattan = initial.manhattan();
        leafNodes.insert(new GameBoard(initial, manhattan, 0, manhattan));
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {return solution() != null;}

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int moves = ((List<Board>)solution()).size();
        return moves == 0 ? -1 : moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        List<Board> boardSequence = new ArrayList<>();

        return calculateSolution(leafNodes, boardSequence);
    }

    private List<Board> calculateSolution(MinPQ<GameBoard> leafNodes, List<Board> boardSequence) {
        final GameBoard minPriorityGameBoard;
        try {
            minPriorityGameBoard = (GameBoard) leafNodes.delMin();
        } catch (NoSuchElementException e){
            return null;
        }

        boardSequence.add(minPriorityGameBoard.board);

        if(minPriorityGameBoard.board.isGoal()) return boardSequence;

        for(Board neighbor : minPriorityGameBoard.board.neighbors()) {
            int distance = neighbor.manhattan();
            int moves = minPriorityGameBoard.moves ++;
            if(!neighbor.equals(minPriorityGameBoard.board)) {
                leafNodes.insert(new GameBoard(neighbor, distance, moves, distance + moves));
            }
        }

        return calculateSolution(leafNodes, boardSequence);
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
