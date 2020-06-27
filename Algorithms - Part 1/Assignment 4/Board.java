import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;
public class Board {

    private int N;  //Dimension of board
    private int[] board;    //Board of interest

    //Construct a board from an N-by-N array of blocks
    public Board(int[][] blocks)  {
        N = blocks[0].length;
        board = new int[N * N];
        for (int i = 0; i < N; i++) // blocks[i][j]: block in row i, column j
            for (int j = 0; j < N; j++)
                board[i * N + j] = blocks[i][j];
    }

    //String representation of the board
    public String toString() {              
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < board.length; i++) {
            s.append(String.format("%2d ", board[i]));
            if (i % N == 0)
                s.append("\n");
        }
        return s.toString();
    }
    
    //Board dimension N
    public int dimension() {                 
        return N;
    }
    
    //Number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N * N; i++) //Compare board[1] through board[N^2-1] with goal
                if (board[i] != i + 1 && board[i] != 0) //Count for blocks in wrong place
                    count++;
        return count;
    }
    
    //Sum of Manhattan distances between blocks and goal
    public int manhattan() {               
        int sum = 0;
        for (int i = 0; i < N * N; i++)
            if (board[i] != i + 1 && board[i] != 0)
                sum += manhattan(board[i], i);
        return sum;
    }
    
    //Return manhattan distance of a misplaced block
    private int manhattan(int goal, int current) {  
        int row, col;   //Row and column distance from the goal
        row = Math.abs((goal - 1) / N - current / N);   //Row difference
        col = Math.abs((goal - 1) % N - current % N);   //Column difference
        return row + col;
    }
    
    //Is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < N * N - 1; i++)
             if (board[i] != i + 1) 
                 return false;
        return true;
    }
    
    //Does this board equal y?
    public boolean equals(Object y) {
        if (y == this)  return true;
        if (y == null)  return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return Arrays.equals(this.board, that.board);
    }
    
    //All neighboring boards
    public Iterable<Board> neighbors() {    
        int index = 0;  //Record the position of empty block
        boolean found = false;  //If empty block is found
        Board neighbor;
        Queue<Board> q = new Queue<Board>();

        for (int i = 0; i < board.length; i++)  //Search for empty block
            if (board[i] == 0) {
                index = i;
                found = true;
                break;
            }
        
        if (!found)  return null;

        if (index / N != 0) {   //If not first row
            neighbor = new Board(board);
            exch(neighbor, index, index - N);   //Exchange with upper block
            q.enqueue(neighbor);
        }

        if (index / N != (N - 1)) { //If not last row
            neighbor = new Board(board);
            exch(neighbor, index, index + N);   //Exchange with lower block
            q.enqueue(neighbor);
        }

        if ((index % N) != 0) { //If not leftmost column
            neighbor = new Board(board);
            exch(neighbor, index, index - 1);   //Exchange with left block
            q.enqueue(neighbor);
        }

        if ((index % N) != N - 1) { //If not rightmost column
            neighbor = new Board(board);
            exch(neighbor, index, index + 1);   //Exchange with left block
            q.enqueue(neighbor);
        }

        return q;
    }
    
    //Exchange two elements in the array
    private Board exch(Board a, int i, int j) {
        int temp = a.board[i];
        a.board[j] = a.board[i];
        a.board[i] = temp;
        return a;
    }
    
    //A board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {                  
        Board twin;
        if (N == 1)  return null;   //Check if twin board exists
        twin = new Board(board);

        if (board[0] != 0 && board[1] != 0)
            exch(twin, 0, 1);   //If the first two blocks in first row is not empty, exchange them.
        else
            exch(twin, N, N + 1);   //Otherwise, exchange the first two blocks on second row.
        return twin;
    }
    
    //Private constructor useful in twin()
    private Board(int[] board) {            
        N = (int) Math.sqrt(board.length);
        this.board = new int[board.length];
        for (int i = 0; i < board.length; i++)
            this.board[i] = board[i];
    }
    
}
