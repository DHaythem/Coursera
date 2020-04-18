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
    
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF full;
    private int N;
    private int top;
    private int bottom;
    private boolean[] openNodes;
    
    /*
      Initialises an N * N WeightedQuickUnionUF object plus two extra nodes for the virtual top and virtual bottom nodes.
      Creates an internal boolean array to keep track of whether a node is considered open or not.
      Also initialises a second N * N WeightedQuickUnionUF object plus one extra node as a second collection to check for fullness and avoid the backwash issue.
      N dimensions of the grid
    */
    
    public Percolation(int N) {
        if (N <= 0) throw new java.lang.IllegalArgumentException("N must be greater than zero");
        
        grid = new WeightedQuickUnionUF(N * N + 2);
        full = new WeightedQuickUnionUF(N * N + 1);
        
        this.N = N;
        
        top = getSingleArrayIdx(N, N) + 1;
        bottom = getSingleArrayIdx(N, N) + 2;
        
        openNodes = new boolean[N * N];
    }
    
    /*
      Converts an index for a 0-based array from two grid coordinates which are 1-based.
      First checks to see if the coordinates are out of bounds.
      i Node row
      j Node column
    */
    
    private int getSingleArrayIdx(int i, int j) {
        doOutOfBoundCheck(i, j);
        return N * (i - 1) + j - 1;
    }
    
    /*
      Throws an error if the given coordinates are not valid.
    */
    
    private void doOutOfBoundCheck(int i, int j) {
        if (!isValid(i, j)) throw new java.lang.IllegalArgumentException("Values are out of bounds");
    }
    
    /*
      Checks to see if two given coordinates are valid.
    */
    
    private boolean isValid(int i, int j) {
        return i > 0 && j > 0 && i <= N && j <= N;
    }
    
    /*
     Sets a given node coordinates to be open (if it isn't open already).
     First is sets the appropriate index of the`openNodes` array to be true and then attempts to union with all adjacent open nodes.
     If the node is in the first row then it will union with the virtual top node. 
     If the node is in the last row then it will union with the virtual bottom row.
     This does connections both for the grid as well as the full, but checkes to make sure that the nodes in full never connect to the virtual bottom node.
    */
    
    public void open(int i, int j) {
        doOutOfBoundCheck(i, j);
        
        if (isOpen(i, j)) return; //No need to open this again as it's already open
        
        int idx = getSingleArrayIdx(i, j);
        openNodes[idx] = true;
        
        //Node is in the top row. Union node in grid and full to the virtual top row.
        if (i == 1) {
            grid.union(top, idx);
            full.union(top, idx);
        }
        
        //Node is in the bottom row. Only union the node in grid to avoid backwash issue.
        if (i == N) grid.union(bottom, idx);
        
        //Union with the node above the given node if it is already open
        if (isValid(i - 1, j) && isOpen(i - 1, j)) {
            grid.union(getSingleArrayIdx(i - 1, j), idx);
            full.union(getSingleArrayIdx(i - 1, j), idx);
        }

        //Union with the node to the right of the given node if it is already open
        if (isValid(i, j + 1) && isOpen(i, j + 1)) {
            grid.union(getSingleArrayIdx(i, j + 1), idx);
            full.union(getSingleArrayIdx(i, j + 1), idx);
        }

        //Union with the node below the given node if it is already open
        if (isValid(i + 1, j) && isOpen(i + 1, j)) {
            grid.union(getSingleArrayIdx(i + 1, j), idx);
            full.union(getSingleArrayIdx(i + 1, j), idx);
        }

        // Union with the node to the left of the given node if it is already open
        if (isValid(i, j - 1) && isOpen(i, j - 1)) {
            grid.union(getSingleArrayIdx(i, j - 1), idx);
            full.union(getSingleArrayIdx(i, j - 1), idx);
        }
    }
    
    
    //Checks whether this node is open or not.
    public boolean isOpen(int i, int j) {
        doOutOfBoundCheck(i ,j);
        return openNodes[getSingleArrayIdx(i, j)];    
    }
    
    /*
     Checks if a given node if 'full'. A node is considered full if it connects to the virtual top node.
     Note that this check is against the full which is not connected to the virtual bottom node so that we don't get affected by backwash.
    */
    public boolean isFull(int i, int j) {
        int idx = getSingleArrayIdx(i, j);
        return full.connected(idx, top);
    }
    
    //the grid percolate if the virtual top node connects to the virtual bottom node.
    public boolean percolate() {
        return grid.connected(top, bottom);
    }
}
