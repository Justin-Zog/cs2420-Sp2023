import java.util.Arrays;

public class Percolation {

    // ******** CONSTANTS ********
    static int SIZE;
    UnionFind grid;
    int gridSize;

    public Percolation(int size) {
        SIZE = size;
        gridSize = (SIZE*SIZE)+2;
        grid = new UnionFind((SIZE*SIZE)+2, -997);
    }
    static String BLUE =  "\u001B[44m   \u001B[0m";
    static String BLACK = "\u001B[40m   \u001B[0m";
    static String RED =   "\u001B[41m   \u001B[0m";
    static String WHITE = "\033[0;107m   \u001B[0m";


    public void printGrid(int iterations) {
        // prints the grid.
        System.out.println("\n\n");
        for (int i=0; i<gridSize-2; i++) {
            if (grid.getArr()[i] == -997) {
                System.out.print(BLACK);
            }
            else {
                System.out.print(WHITE);
            }
            if ((i+1)%SIZE == 0) {
                System.out.println();
            }
        }
        System.out.println(iterations + " Open Sites");
    }

    public void printFinalGrid(int iterations, int lastOpened) {
        for (int i=0; i<gridSize-2; i++) {
            if (i==lastOpened) {
                System.out.print(RED);
            }
            else if (grid.getArr()[i] == -997) {
                System.out.print(BLACK);
            }
            else if (grid.find(i) == grid.find(gridSize-1)) {
                System.out.print(BLUE);
            }
            else {
                System.out.print(WHITE);
            }
            if ((i+1)%SIZE == 0) {
                System.out.println();
            }
        }
        System.out.println(iterations + " Open Sites");
    }


    public void unionNeighbors(int x, int y) {
        // System.out.println("X and Y are: " + x + " : " + y);
        int selectedID = ((SIZE*x)+y);
        // The index isn't by a wall.
        if (hasTopNeighbor(x)) {
            // Check to see if neighbor is blocked
            int neighborID = ((SIZE*(x-1))+y);
            int neighbor = grid.getArr()[neighborID];
            if (neighbor != -997) {
                grid.union(neighborID, selectedID);
            }
        }
        if (hasBottomNeighbor(x)) {
            // Check if neighbor is blocked
            int neighborID = ((SIZE*(x+1))+y);
            int neighbor = grid.getArr()[neighborID];
            if (neighbor != -997) {
                grid.union(neighborID, selectedID);
            }
        }
        if (hasLeftNeighbor(y)) {
            // Check if neighbor is blocked
            int neighborID = ((SIZE*x)+(y-1));
            int neighbor = grid.getArr()[neighborID];
            if (neighbor != -997) {
                grid.union(neighborID, selectedID);
            }
        }
        if (hasRightNeighbor(y)) {
            // Check if neighbor is blocked
            int neighborID = ((SIZE*x)+(y+1));
            int neighbor = grid.getArr()[neighborID];
            if (neighbor != -997) {
                grid.union(neighborID, selectedID);
            }
        }
    }

    private boolean hasTopNeighbor(int x) {
        return (x != 0);
    }

    private boolean hasBottomNeighbor(int x) {
        return (x != SIZE-1);
    }

    private boolean hasLeftNeighbor(int y) {
        return (y != 0);
    }

    private boolean hasRightNeighbor(int y) {
        return (y != SIZE-1);
    }


    public void percolate() {
        // Sets the 'dummy' cells value as something else so the system works.
        grid.getArr()[gridSize-2] = -1013;
        grid.getArr()[gridSize-1] = -1009;
        // Randomly opens cells until a percolation is sensed by the masters of kung-fu
        boolean isPercolated = false;
        int tracker = 0;
        while (!isPercolated) {
            // System.out.print("The current count is: " + tracker + " **** ");
            int randomIndex = (int) (Math.random()*gridSize);
            if (randomIndex == gridSize-2 || randomIndex == gridSize-1) {
                continue;
            }

            if (grid.getArr()[randomIndex] == -997) {
                tracker ++;
                if (tracker % 50 == 0) {
                    printGrid(tracker);
                }
                // 'Open' the cell
                grid.getArr()[randomIndex] = -1;
                // Check if the cell is on the TOP or BOTTOM and union them if so.
                if (randomIndex < SIZE) {
                    grid.union(gridSize-2, randomIndex);
                }
                if (randomIndex > gridSize-SIZE-3 && randomIndex < gridSize-2) {
                    grid.union(gridSize-1, randomIndex);
                }
                // Union the cell with its neighbors.
                // System.out.println("Random index is: " + randomIndex);
                int x = randomIndex/SIZE;
                int y = randomIndex%SIZE;
                unionNeighbors(x, y);

                // Checks to see if percolation has occurred
                if (grid.find(gridSize-2) == grid.find(gridSize-1)) {
                    int lastOpened = ((SIZE*x)+y);
                    isPercolated = true;
                    printFinalGrid(tracker, lastOpened);
                }
            }
        }
    }

    /**
     * Creates a union find array with every slot filled with -997 which denotes that it is blocked.
     * I chose -997 specifically because it is a prime number and a grid will never have a prime number of spots.
     * When the slot gets opened the value will change to -1. (The dummy cells are an exception).
     * I have added 2 to the size in order to create 2 dummy slots that will act as the top and bottom groups.
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(20);
        p.percolate();
    }
}
