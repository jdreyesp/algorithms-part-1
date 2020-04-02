package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private final int n;
    // private final Stopwatch stopWatch;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        // stopWatch = new Stopwatch();

        this.n = n;

        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            // MonteCarlo simulation
            final Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(n) + 1;
                int randomCol = StdRandom.uniform(n) + 1;
                percolation.open(randomRow, randomCol);
            }

            double size = n * n;
            double result = percolation.numberOfOpenSites() / size;

            results[i] = result;
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidenceValue();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidenceValue();
    }

    // X  ±  Z * (s / √n)
    // Z = 95% = 1.960 (https://www.mathsisfun.com/data/confidence-interval.html)
    private double confidenceValue() {
        return 1.960D * (stddev() / Math.sqrt(n));
    }

    @Override
    public String toString() {
        return // "ElapsedTime (ms): " + stopWatch.elapsedTime() + "\n" +
                "mean\t\t\t\t\t= " + mean() + "\n" +
                "stddev\t\t\t\t\t= " + stddev() + "\n" +
                "95% confidence interval\t= [" + confidenceLo() + ", " + confidenceHi() + "]";
    }

    // test client (see below)
    /* public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        int[][] tests = new int[3][2];
        tests[0][0] = n;
        tests[0][1] = trials;

        tests[1][0] = n*2;
        tests[1][1] = trials;

        tests[2][0] = n;
        tests[2][1] = trials*2;

        for (int i = 0; i < tests.length; i++) {

            System.out.println("\n== N: " + tests[i][0] + " trials: " + tests[i][1] + " ==");
            final week1.PercolationStats percolationStats = new week1.PercolationStats(tests[i][0], tests[i][1]);
            System.out.println(percolationStats.toString());
        }

    } */

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        final PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println(percolationStats.toString());
    }
}
