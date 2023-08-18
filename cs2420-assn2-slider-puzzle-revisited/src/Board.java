import java.util.Random;

//functional handling of an instance of the board. Can be created using an id.
public class Board {
    private static final int SIZE = 3;
    private static final String SOLVED_ID = "123456780";
    private int[][] board;  // Values of board
    private int blankRow;   // Row location of blank
    private int blankCol;   // Column location of blank

    /**
     * Generate a new board
     */
    public Board() {
        board = new int[SIZE][SIZE];
    }

    /**
     * @param state String representation of the board
     * @return true if board state is the solution
     */
    Boolean isSolved(String state) {
        return state.equals(SOLVED_ID);
    }

    /**
     * Create board from string version
     *
     * @param id
     */
    Board(String id) {
        board = new int[SIZE][SIZE];
        int c = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (id.charAt(c) == '0') {
                    blankRow = i;
                    blankCol = j;
                }
                //assigns each item of the string to the board as an int.
                board[i][j] = Integer.parseInt(id.substring(c, ++c));
            }
    }


    @Override
    /**
     * Convert matrix version of  board to ID
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] i : board) {
            for (int j : i) {
                sb.append(j + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //Create a board by performing legal moves on a board
//jumbleCt indicates the number of moves to make
//if jumbleCt ==0, return the winning board

    /**
     * Create a solved board then make jumbleCt random moves
     *
     * @param jumbleCt number of random moves to make
     */
    public void makeBoard(int jumbleCt) {
        int val = 1;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = val++;
        blankRow = SIZE - 1;
        blankCol = SIZE - 1;
        board[blankRow][blankCol] = 0;
        jumble(jumbleCt);
    }

    /**
     * create a board from a given set of values
     *
     * @param values values of board in order by rows
     */
    void makeBoard(int[] values) {
        int c = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (values[c] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
                board[i][j] = values[c++];
            }
    }


    /**
     * Perform a slide up operation, if possible
     *
     * @return true if slide up was possible
     */
    boolean slideUp()  // If possible, slides a tile up into the blank position.  Returns success of operation.
    {
        if (blankRow == SIZE - 1) return false;
        board[blankRow][blankCol] = board[blankRow + 1][blankCol];
        board[blankRow + 1][blankCol] = 0;
        blankRow += 1;
        return true;
    }

    /**
     * Perform a slide Down operation, if possible
     *
     * @return true if slideDown was performed
     */
    boolean slideDown()  // If possible, slides a tile down into the blank position.  Returns success of operation.
    {
        if (blankRow == 0) return false;
        board[blankRow][blankCol] = board[blankRow - 1][blankCol];
        board[blankRow - 1][blankCol] = 0;
        blankRow -= 1;
        return true;
    }

    /**
     * Perform a slide Left, if possible
     *
     * @return true if slide Left was done
     */
    boolean slideLeft()  // If possible, slides a tile left into the blank position.  Returns success of operation.
    {
        if (blankCol == SIZE - 1) return false;
        board[blankRow][blankCol] = board[blankRow][blankCol + 1];
        board[blankRow][blankCol + 1] = 0;
        blankCol += 1;
        return true;
    }

    /**
     * Perform a slide Right, if possible
     *
     * @return true if slide Righrt was performed
     */
    boolean slideRight()  // If possible, slides a tile right into the blank position.  Returns success of operation.
    {
        if (blankCol == 0) return false;
        board[blankRow][blankCol] = board[blankRow][blankCol - 1];
        board[blankRow][blankCol - 1] = 0;
        blankCol -= 1;
        return true;
    }
    /**
     * Randomly apply ct moves to the board, making sure they are legal and don't undo the previous move
     *
     * @param ct
     */
    void jumble(int ct) {
        Random rand = new Random();
        String moveStr = "UDLR";  // Moves representing Up, Down, Left, Right
        char lastMove = ' ';
        char thisMove;
        for (int i = 0; i < ct; i++) {
            thisMove = moveStr.charAt(rand.nextInt(4));
            while (!makeMove(thisMove, lastMove)) {
                thisMove = moveStr.charAt(rand.nextInt(4));

            }
            lastMove = thisMove;
        }
    }

    /**
     * If move is legal (not  undoing previous move), make it
     * @param move     Move to attempt
     * @param lastmove Previously completed move
     * @return success of move
     */
    boolean makeMove(char move, char lastmove) {
        // System.out.println("makeMove " + move + lastmove + "\n");
        boolean moved = false;
        switch (move) {
            case 'R':
                if (lastmove != 'L') {
                    moved = slideRight();
                }
                break;
            case 'L':
                if (lastmove != 'R') {
                    moved = slideLeft();
                }
                break;
            case 'D':
                if (lastmove != 'U') {
                    moved = slideDown();
                }
                break;
            case 'U':
                if (lastmove != 'D') {
                    moved = slideUp();
                }
                break;
        }
        return moved;
    }

    /**
     * @return String version of board
     */
    public String getId() {
        String id = "";
        for (int i[] : board) {
            for (int j : i) {
                id += j;
            }
        }
        return id;
    }

    /**
     * @return Boolean that tells whether the puzzle is solvable or not
     *
     * This uses the inversion count of the puzzle to determine whether it can be solved or not.
     */
    public boolean isSolvable() {
        int inversions = 0;

        for (int i = 0; i < getId().length() - 1; i++) {

            if ((getId().charAt(i)) == '0') {
                continue;
            }

            for (int j = i+1; j < getId().length(); j++) {

                if ((getId().charAt(j)) == '0') {
                    continue;
                }

                if ((int)(getId().charAt(i)) > (int)(getId().charAt(j))) {
                    inversions++;
                }
            }
        }
        return (inversions % 2 == 0);
    }
}
