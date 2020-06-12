package week4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        final StringBuilder boardStringBuilder = new StringBuilder();
        boardStringBuilder.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i ++) {
            boardStringBuilder.append(" ");
            for (int j = 0; j < dimension(); j ++) {
                boardStringBuilder.append(tiles[i][j] + " ");
                if(j == dimension() - 1) boardStringBuilder.append("\n");
            }
        }
        return boardStringBuilder.toString();
    }

    // board dimension n
    public int dimension() {return tiles.length;}

    // number of tiles out of place
    public int hamming() {
        int outOfPlace = 0;
        for (int i = 0; i < dimension(); i ++) {
            for (int j = 0; j < dimension(); j++) {
                if(tiles[i][j] == 0) continue;
                else if(tiles[i][j] != i * dimension() + j + 1) outOfPlace++;
            }
        }
        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int numOfMisplacements = 0;
        for (int i = 0; i < dimension(); i ++) {
            for (int j = 0; j < dimension(); j++) {
                if(tiles[i][j] == i + j + 1 || tiles[i][j] == 0) continue;
                else {
                    int element = tiles[i][j];
                    //vertical distance to its supposed good location
                    numOfMisplacements += Math.abs((i+1) - rowOfElement(element));
                    //horizontal distance to its supposed good location
                    numOfMisplacements += Math.abs((j+1) - columnOfElement(element));
                }
            }
        }
        return numOfMisplacements;
    }

    private int rowOfElement(int element) {
        return Double.valueOf(Math.ceil(Float.valueOf(element) / Float.valueOf(dimension()))).intValue();
    }

    private int columnOfElement(int element) {
        return dimension() - (dimension() * rowOfElement(element) - element);
    }

    // is this board the goal board?
    public boolean isGoal() {return hamming() == 0;}

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board)y;
        return this.dimension() == that.dimension() && sameElements(that);
    }

    private boolean sameElements(Board that) {
        boolean sameElements = true;
        for (int i = 0; i < dimension(); i ++) {
            for (int j = 0; j < dimension(); j++) {
                if(this.tiles[i][j] != that.tiles[i][j]) {
                    sameElements = false;
                    break;
                }
            }
        }
        return sameElements;
    }

    private class TilePosition {
        private int x;
        private int y;

        public TilePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final TilePosition position = searchBlankSquare();
        int row = position.x;
        int col = position.y;

        List<Board> neighbors = new ArrayList<>();

        if (row - 1 >= 0) neighbors.add(swapTiles(position, new TilePosition(row - 1, col)));
        if (row + 1 < dimension()) neighbors.add(swapTiles(position, new TilePosition(row + 1, col)));
        if (col - 1 >= 0) neighbors.add(swapTiles(position, new TilePosition(row, col - 1)));
        if (col + 1 < dimension()) neighbors.add(swapTiles(position, new TilePosition(row, col + 1)));

        return neighbors;
    }

    private TilePosition searchBlankSquare() {
        for (int rowIndex = 0; rowIndex < this.tiles.length; rowIndex++ ) {
            int[] row = this.tiles[rowIndex];
            for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                if (0 == row[columnIndex]) {
                    return new TilePosition(rowIndex, columnIndex);
                }
            }
        }
        return null;
    }

    private Board swapTiles(TilePosition origin, TilePosition target) {
        int[][] boardCopy = new int[tiles.length][tiles.length];
        for(int i=0; i< tiles.length; i++) {
            boardCopy[i] = tiles[i].clone();
        }

        final int temp = boardCopy[origin.x][origin.y];
        boardCopy[origin.x][origin.y] = boardCopy[target.x][target.y];
        boardCopy[target.x][target.y] = temp;
        return new Board(boardCopy);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Random random = new Random();
        return swapTiles(
                new TilePosition(random.nextInt(dimension()), random.nextInt(dimension())),
                new TilePosition(random.nextInt(dimension()), random.nextInt(dimension())));
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        final Board b = new Board(new int[][] {{1, 2, 3}, {0, 7, 8}, {4, 5, 6}});
        final Board b2 = new Board(new int[][] {{1, 2, 3}, {0, 7, 8}, {4, 5, 6}});
        final Board b3 = new Board(new int[][] {{1, 2, 3}, {6, 0, 8}, {4, 5, 7}});
        final Board goalBoard = new Board(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});

        System.out.println(b.toString());
        System.out.println("Hamming: " + b.hamming());
        System.out.println("Manhattan: " + b.manhattan());

        System.out.println("Hamming goal: " + goalBoard.hamming());

        System.out.println("Equal boards: Equals=" + b.equals(b2));
        System.out.println("Not equal boards: Equals=" + b.equals(b3));

        System.out.println(b3.neighbors());
        System.out.println(b3.twin());
    }

}
