import java.util.Arrays;

public class UnionFind {
    /**
     * A union find is done by using an array.
     * The value at each given index is the parent of the node with that index (id).
     *   1   2   3   4   5
     *[ -2   1  -3   5   3 ]
     */
    // ******** Setters *********
    private int[] arr;

    public UnionFind(int size, int fillValue) {
        arr = new int[size];
        Arrays.fill(arr, fillValue);
    }

    public UnionFind(int size) {
        arr = new int[size];
        Arrays.fill(arr, -1);
    }

    // ******** GETTERS ********
    public int[] getArr() {
        return arr;
    }

    // ******** METHODS **********
    /**
     *
     * @param index: The index whose parent we are trying to find.
     * @return the index of the parent of the index that find was performed on.
     */
    public int find(int index) {
        if (arr[index] < 0) {
            return index;
        }
        else {
            // Gets the topmost node and performs path compression at the same time.
            int motherNode = find(arr[index]);
            arr[index] = motherNode;
            return motherNode;
        }
    }

    /**
     * Finds the mothernode of both indices then compares and merges.
     * @param i: The index of the first value.
     * @param j: The index of the second value.
     */
    public void union(int i, int j) {
        int mother1 = find(i);
        int mother2 = find(j);

        if (mother1 == mother2) {
            System.out.print("");
            // System.out.println("The index " + i + " and " + j + " have the same mother: " + mother1);
        }
        else if (arr[mother1] < arr[mother2] || arr[mother1] == arr[mother2]) {
            // Sets the size of the bigger tree.
            int motherTwoSize = arr[mother2];
            arr[mother1] += motherTwoSize;
            // Sets the parent of the smaller tree to the bigger tree.
            arr[mother2] = mother1;
            // System.out.println(i + "'s tree was bigger or equal to " + j + "'s tree.");
        }
        else {
            // Sets the size of the bigger tree.
            int motherOneSize = arr[mother1];
            arr[mother2] += motherOneSize;
            // Sets the parent of the smaller tree to the bigger tree.
            arr[mother1] = mother2;
            // System.out.println(j + "'s tree was bigger or equal to " + i + "'s tree.");
        }
    }

    // ********** TESTS **********
    public static void main(String[] args) {
        UnionFind uf = new UnionFind(15);

        System.out.println("TESTING UNION METHOD OF OBJECTS IN DIFFERENT GROUPS");
        uf.union(3, 5);
        uf.union(6, 7);
        uf.union(2, 5);
        uf.union(6, 2);
        // Expected mother of 6 is 3.
        System.out.println("Six's mother is: " + uf.find(6));
        uf.union(14, 1);
        uf.union(13, 0);
        uf.union(9, 10);
        uf.union(13, 1);
        // Expected mother of 14 is 13
        System.out.println("Fourteen's mother is: " + uf.find(14));

        System.out.println();
        System.out.println("TESTING UNION OF OBJECTS IN THE SAME GROUP");
        uf.union(6, 3);
        uf.union(1, 13);
        uf.union(9, 10);

        System.out.println();
        System.out.println("TESTING THE FIND METHOD");
        System.out.println("Ten's mother is " + uf.find(10));
        System.out.println("Seven's mother is " + uf.find(7));
        System.out.println("One's mother is " + uf.find(1));
        System.out.println("Eight's mother is " + uf.find(8));

        System.out.println();
        System.out.println("Check if path compression works below.");
        System.out.println(Arrays.toString(uf.arr));
    }

}
