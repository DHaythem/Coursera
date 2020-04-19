import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
  Builds a N*N sized WeightedQuickUnionUF grid to mock create a simple percolation system.
  Initially all nodes in the grid are blocked and must be opened.
  The grid is considered to percolate when there is a connection from an open node on the top row to an open node on the bottom row.
  Whether a node is open or not is kept in an array.
  All connections are done through a WeightedQuickUnionUF object.
  We have a second WeightedQuickUnionUF object for checking fullness so as to not run into the backwash issue.
*/

public class Percolation {
    
    private boolean[][] grid;
    private WeightedQuickUnionUF wqfGrid;
    private WeightedQuickUnionUF wqfFull;
    private int gridSize;
    private int gridSquared;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;
    
    /*
      Initialises an N * N WeightedQuickUnionUF object plus two extra nodes for the virtual top and virtual bottom nodes.
      Creates an internal boolean array to keep track of whether a node is considered open or not.
      Also initialises a second N * N WeightedQuickUnionUF object plus one extra node as a second collection to check for fullness and avoid the backwash issue.
      N dimensions of the grid
    */

    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException("N must be greater than zero");
        gridSize = N;
        gridSquared = N * N ;
        grid = new boolean[gridSize][gridSize];
        wqfGrid = new WeightedQuickUnionUF(gridSquared + 2); // includes virtual top bottom
        wqfFull = new WeightedQuickUnionUF(gridSquared + 1); // includes virtual top
        virtualBottom = gridSquared + 1;
        virtualTop = gridSquared;
        openSites = 0;

    }

    /*
     Sets a given node coordinates to be open (if it isn't open already).
     First, sets the appropriate index in the grid to be true and then attempts to union with all adjacent open nodes.
     If the node is in the first row then it will union with the virtual top node. 
     If the node is in the last row then it will union with the virtual bottom row.
     This does connections both for the grid as well as the full, but checkes to make sure that the nodes in full never connect to the virtual bottom node.
    */
    
    public void open(int row, int col) {
        validateSite(row, col);

        int shiftRow = row - 1;
        int shiftCol = col - 1;
        int flatIndex = flattenGrid(row, col);

        // If already open, stop
        if (isOpen(row, col)) {
            return;
        }

        // Open Site
        grid[shiftRow][shiftCol] = true;
        openSites++;

        if (row == 1) {  // Top Row
            wqfGrid.union(virtualTop, flatIndex);
            wqfFull.union(virtualTop, flatIndex);
        }

        if (row == gridSize) {  // Bottom Row
            wqfGrid.union(virtualBottom, flatIndex);
        }

        // Check and Open Left
        if (isOnGrid(row, col - 1) && isOpen(row, col - 1)) {
            wqfGrid.union(flatIndex, flattenGrid(row, col - 1));
            wqfFull.union(flatIndex, flattenGrid(row, col - 1));
        }

        // Check and Open Right
        if (isOnGrid(row, col + 1) && isOpen(row, col + 1)) {
            wqfGrid.union(flatIndex, flattenGrid(row, col + 1));
            wqfFull.union(flatIndex, flattenGrid(row, col + 1));
        }

        // Check and Open Up
        if (isOnGrid(row - 1, col) && isOpen(row - 1, col)) {
            wqfGrid.union(flatIndex, flattenGrid(row - 1, col));
            wqfFull.union(flatIndex, flattenGrid(row - 1, col));
        }

        // Check and Open Down
        if (isOnGrid(row + 1, col) && isOpen(row + 1, col)) {
            wqfGrid.union(flatIndex, flattenGrid(row + 1, col));
            wqfFull.union(flatIndex, flattenGrid(row + 1, col));
        }
    }
    
    /*
      Converts an index for a 0-based array from two grid coordinates which are 1-based.
      First checks to see if the coordinates are out of bounds.
    */
    private int flattenGrid(int row, int col) {
        return gridSize * (row - 1) + col - 1;
    }
    
    //Checks to see if two given coordinates are valid.
    private boolean isOnGrid(int row, int col) {
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftRow >= 0 && shiftCol >= 0 && shiftRow < gridSize && shiftCol < gridSize);
    }
    
    //Throws an error if the given coordinates are not valid.
    private void validateSite(int row, int col) {
        if (!isOnGrid(row, col)) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    //Checks whether this node is open or not.
    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];

    }

    /*
     Checks if a given node if 'full'. A node is considered full if it connects to the virtual top node.
     Note that this check is against the full which is not connected to the virtual bottom node so that we don't get affected by backwash.
    */
    
    public boolean isFull(int row, int col) {
        validateSite(row, col);
        return wqfFull.connected(virtualTop, flattenGrid(row, col));
    }

    // Test: number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    //the grid percolate if the virtual top node connects to the virtual bottom node.
    public boolean percolates() {
        return wqfGrid.connected(virtualTop, virtualBottom);
    }

    // test client
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);

        Percolation percolation = new Percolation(size);
        int argCount = args.length;
        for (int i = 1; argCount >= 2; i += 2) {
            int row = Integer.parseInt(args[i]);
            int col = Integer.parseInt(args[i + 1]);
            StdOut.printf("Adding row: %d  col: %d %n", row, col);
            percolation.open(row, col);
            if (percolation.percolates()) {
                StdOut.printf("%nThe System percolates %n");
            }
            argCount -= 2;
        }
        if (!percolation.percolates()) {
            StdOut.print("Does not percolate %n");
        }

    }
}
