import java.util.Scanner;

public class Game {
    private static final String SOLVED_ID = "123456780";
    Board theBoard;
    String originalBoardID;
    String boardName;

    /**
     *  Solve the provided board
     * @param label Name of board (for printing)
     * @param b Board to be solved
     */
    public void playGiven(String label, Board b) {
        theBoard = b;
        originalBoardID = b.getId();
        boardName = label;
        System.out.println("Board initial: " + boardName + " \n" + theBoard.toString());
        String aStarSolution = aStarSolve(originalBoardID);
        String bruteSolution = bruteSolve(originalBoardID);

        if (aStarSolution.compareTo(bruteSolution) != 0) {
            System.out.println("\nA* and Brute did not have the same solution\n");
        }

    }

    /**
     * Create a random board (which is solvable) by jumbling jumnbleCount times.
     * Solve
     * @param label Name of board (for printing)
     * @param jumbleCount number of random moves to make in creating a board
     */
    public void playRandom(String label, int jumbleCount) {
        theBoard = new Board();
        theBoard.makeBoard(jumbleCount);
        System.out.println(label + "\n" + theBoard);
        playGiven(label, theBoard);


    }

    public void printSteps(Queue<State> queue, State state) {

        System.out.println("\nBrute Solve");
        System.out.println("Moves Required: " + state.getSteps() + " (" + state.getNumSteps() + ")");
        System.out.println(boardName + " Queue Added " + queue.getTotalNodesAdded() + " Nodes. Queue Removed " + queue.getTotalNodesRemoved() + " Nodes.");

    }

    public void printSteps(AVLTree<State> tree, State state) {

        System.out.println("A* Solve");
        System.out.println("Moves Required: " + state.getSteps() + " (" + state.getNumSteps() + ")");
        System.out.println(boardName + " Tree Added " + tree.getTotalNodesAdded() + " Nodes. Tree Removed " + tree.getTotalNodesRemoved() + " Nodes.");

    }

    public String bruteSolve(String initialID) {

        Queue<State> queue = new Queue<>();

        // Adds the initial state of the board to the queue
        queue.add(new State(initialID, ""));

        // Checks to see if the puzzle is solvable.
        if (!theBoard.isSolvable()) {
            System.out.println("This puzzle has no solution...");
            return "";
        }

        while (true) {

            char[] moves = new char[]{ 'U', 'D', 'R', 'L'};

            State currentState = queue.remove();
            Board currentBoard = new Board(currentState.getId());

            // Tries all four moves and adds the move to the queue if it successful.
            for (char move : moves) {
                if (currentBoard.makeMove(move, currentState.getLast())) {
                    queue.add(new State(currentBoard.getId(), currentState.getSteps() + move));
                    // Since the if statement was true, currentBoard was modified, we need to revert it back.
                    currentBoard = new Board(currentState.getId());
                }
            }

            if (currentBoard.isSolved(currentBoard.getId())) {
                printSteps(queue, currentState);
                return currentState.getSteps();
            }
        }
    }


    public String aStarSolve(String initialID) {

        AVLTree<State> tree = new AVLTree<>();

        // Adds the initial state of the board to the tree
        tree.insert(new State(initialID, ""));

        if (!theBoard.isSolvable()) {
            System.out.println("This puzzle has no solution...");
            return "";
        }

        while (true) {

            char[] moves = new char[]{ 'U', 'D', 'R', 'L'};

            State currentState = tree.findMin();
            // Delete the state from the AVLTree
            tree.deleteMin();

            Board currentBoard = new Board(currentState.getId());

            // Tries all four moves and adds the moves to the tree if successful.
            for (char move: moves) {
                if (currentBoard.makeMove(move, currentState.getLast())) {
                    tree.insert(new State(currentBoard.getId(), currentState.getSteps() + move));
                    // Since the if statement was true, currentBoard was modified and needs to be changed back.
                    currentBoard = new Board(currentState.getId());
                }
            }

            if (currentBoard.isSolved(currentState.getId())) {
                printSteps(tree, currentState);
                return currentState.getSteps();
            }
        }

    }

    public static void main(String[] args) {
        String[] games = {"102453786", "123740658", "023156478", "413728065", "145236078", "123456870"};
        String[] gameNames = {"Easy Board", "Game1", "Game2", "Game3", "Game4", "Game5 No Solution"};
        Game g = new Game();
        Scanner in = new Scanner(System.in);
        Board b;
        String resp;
        for (int i = 0; i < games.length; i++) {
            b = new Board(games[i]);
            g.playGiven(gameNames[i], b);
            System.out.println("Click any key to continue\n");
            resp = in.nextLine();
        }

        boolean playAgain = true;
        //playAgain = false;

        int JUMBLECT = 18;  // how much jumbling to do in random board
        while (playAgain) {
            g.playRandom("Random Board", JUMBLECT);

            System.out.println("Play Again?  Answer Y for yes\n");
            resp = in.nextLine().toUpperCase();
            playAgain = (resp != "") && (resp.charAt(0) == 'Y');
        }


    }


}
