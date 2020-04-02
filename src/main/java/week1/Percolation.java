package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] grid;
    private int numberOfOpened = 0;
    private final WeightedQuickUnionUF unionUF;
    private final int topSiteIdxForUF;
    private final int bottomSiteIdxForUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        grid = new boolean[n][n];

        //+2 virtual sites for top and bottom
        int unionUFSize = n*n + 2;
        unionUF = new WeightedQuickUnionUF(unionUFSize);
        topSiteIdxForUF = unionUFSize - 2;
        bottomSiteIdxForUF = unionUFSize - 1;

        for (int i = 0; i < n; i++) {
            unionUF.union(i, topSiteIdxForUF);
            unionUF.union((n*n) - (n - i), bottomSiteIdxForUF);
        }

    }

    // O(n)

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        // System.out.println("Opening [" + row + "," + col + "]");

        if (isOpen(row, col)) return;

        int currentCellIdxForUF = (row -1) * grid.length + (col -1);

        if (row + 1 <= grid.length && isOpen(row + 1, col)) {
            // System.out.println("Connecting ["+ row + "," + col + "] with [" + (row + 1) + "," + col + "]");
            unionUF.union(currentCellIdxForUF, currentCellIdxForUF + grid.length);
        }

        if (row - 1 > 0 && isOpen(row - 1, col)) {
            // System.out.println("Connecting ["+ row + "," + col + "] with [" + (row - 1) + "," + col + "]");
            unionUF.union(currentCellIdxForUF, currentCellIdxForUF - grid.length);
        }

        if (col + 1 <= grid.length && isOpen(row, col + 1)) {
            // System.out.println("Connecting ["+ row + "," + col + "] with [" + (row) + "," + (col + 1) + "]");
            unionUF.union(currentCellIdxForUF, currentCellIdxForUF + 1);
        }

        if (col - 1 > 0 && isOpen(row, col - 1)) {
            // System.out.println("Connecting ["+ row + "," + col + "] with [" + (row) + "," + (col - 1) + "]");
            unionUF.union(currentCellIdxForUF, currentCellIdxForUF - 1);
        }

        grid[row - 1][col - 1] = true;
        numberOfOpened++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBoundaries(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBoundaries(row, col);
        if (!isOpen(row, col)) return false;

        int currentCellIdxForUF = ((row - 1) * grid.length) + (col - 1);
        return unionUF.connected(currentCellIdxForUF, topSiteIdxForUF);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        if (numberOfOpenSites() == 0) return false;
        else return unionUF.connected(topSiteIdxForUF, bottomSiteIdxForUF);
    }

    private void checkBoundaries(int row, int col) {
        if ((row < 1 || row > grid.length) || (col < 1 || col > grid.length)) throw new IllegalArgumentException();
    }

    // test client (optional)
    public static void main(String[] args) {

        final int n = 4;

        for (int i = 0; i < 20; i++) {
            // MonteCarlo simulation
            final Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(n) + 1;
                int randomCol = StdRandom.uniform(n) + 1;
                percolation.open(randomRow, randomCol);
            }

            double size = n * n;
            double result = percolation.numberOfOpenSites() / size;
            System.out.println(result);
        }


    }

}
