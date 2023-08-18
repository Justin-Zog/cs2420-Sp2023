import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// ******************ERRORS********************************
// Throws UnderflowException as appropriate

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    private BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create tree from list
     * @param arr   List of elements
     * @param label Name of tree
     * @ordered true if want an ordered tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }

    /**
     * Build a NON BST tree by inorder
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }


    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a tree
     */
    public String toString() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + "\n\n" + toString(root, 0);
    }

    private String toString(BinaryNode<E> node, int depth) {
        if (node == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString(node.right, depth+1));
        sb.append("  ".repeat(depth) + node.element + "\n");
        sb.append(toString(node.left, depth+1));
        return sb.toString();
    }


    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(n)
     *
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }


    /**
     * The complexity of finding the deepest node is O(n)
     * @return
     */
    public E deepestNode() {
        if (root==null) return (E)("No nodes in tree");
        // Creates an 'object' that stores the depth of the deepestNode as well as the value of the deepest node.
        // The 'object's type is [int, E]
        Object[] deepestNode = new Object[]{0, root.element};

        return (E)deepestNode(root, 0, deepestNode)[1];
    }

    private Object[] deepestNode(BinaryNode<E> node, int depth, Object[] deepestNode) {
        if (node==null) return deepestNode;

        if (depth > (int)deepestNode[0]) {
            deepestNode[1] = node.element;
            deepestNode[0] = depth;
        }

        Object[] deepestNodeRight = deepestNode(node.right, depth+1, deepestNode);
        Object[] deepestNodeLeft = deepestNode(node.left, depth+1, deepestNode);

        if ((int)deepestNodeLeft[0] > (int)deepestNodeRight[0]) {
            return deepestNodeLeft;
        }
        else {
            return deepestNodeRight;
        }
    }

    /**
     * The complexity of finding the flip is O(n)
     * reverse left and right children recursively
     */
    public void flip() {
        flip(root);
    }

    private void flip(BinaryNode<E> node) {
        if (node == null) return;
        // Flips the children of the node.
        BinaryNode<E> tempLeft = node.left;
        node.left = node.right;
        node.right = tempLeft;
        // Flips the children's children.
        flip(node.left);
        flip(node.right);
    }

    /**
     * Counts number of nodes in specified level
     * The complexity of nodesInLevel is O(n)
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        if (root == null) {
            return -1;
        }

        return nodesInLevel(level, root, 0);
    }

    private int nodesInLevel(int level, BinaryNode<E> node, int depth) {
        if (depth > level) return 0;
        if (node == null) return 0;

        if (level != depth) {
            return (nodesInLevel(level, node.right, depth+1) + nodesInLevel(level, node.left, depth+1));
        }
        else {
            return 1;
        }
    }

    /**
     * Print all paths from root to leaves
     * The complexity of printAllPaths is O(n)
     */
    public void printAllPaths() {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<E> ancestors = new ArrayList<>();

        paths = printAllPaths(root, paths, ancestors);

        for (String path : paths) {
            System.out.println(path);
        }
    }

    private ArrayList<String> printAllPaths(BinaryNode<E> node, ArrayList<String> paths, ArrayList<E> ancestors) {
        if (node == null) return paths;

        ancestors.add(node.element);

        if (node.left == null && node.right == null) {
            StringBuilder sb = new StringBuilder();
            for (E ancestor : ancestors) {

                sb.append(ancestor + " ");

            }
            paths.add(sb.toString());
            ancestors.remove(ancestors.size() - 1);
            return paths;
        }

        if (node.left != null) printAllPaths(node.left, paths, ancestors);

        if (node.right != null) printAllPaths(node.right, paths, ancestors);

        // Removes itself from ancestors before returning.
        ancestors.remove(ancestors.size() - 1);

        return paths;
    }


    /**
     * Counts all non-null binary search trees embedded in tree
     *  The complexity of countBST is O(n)
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;

        ArrayList<Integer> totalTrees = new ArrayList<>();

        return countBST(root, totalTrees);
    }

    private Integer countBST(BinaryNode<E> node, ArrayList<Integer> totalTrees) {
        if (node == null) return 1;

        Integer leftSide = countBST(node.left, totalTrees);
        Integer rightSide = countBST(node.right, totalTrees);

        boolean isLeftGood = true;
        boolean isRightGood = true;

        if (node.left != null) {
            isLeftGood = (node.left.element.compareTo(node.element) < 0);
        }
        if (node.right != null) {
            isRightGood = (node.right.element.compareTo(node.element) > 0);
        }

        if (!(isRightGood && isLeftGood)) {
            if (node == root) {
                return totalTrees.size();
            }
            return 0;
        }

        if (isRightGood && isLeftGood && leftSide == 1 && rightSide == 1) {
            totalTrees.add(1);
            if (node == root) {
                return totalTrees.size();
            }
            return 1;
        }

        return totalTrees.size();
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * The complexity of bstInsert depends on the tree.  If it is balanced the complexity is O(log n)
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }
    /**
     * Remove all paths from tree that sum to less than given value
     * @param \sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer k) {

        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }
        pruneK(root, 0, k);

    }

    private Integer pruneK(BinaryNode<E> node, Integer sum, Integer k) {
        if (node == null) return sum;

        sum += (Integer)node.element;

        if (sum >= k) return sum;

        Integer rightSum = pruneK(node.right, sum, k);
        Integer leftSum = pruneK(node.left, sum, k);

        if (rightSum >= k ) {
            return sum + rightSum;
        }
        else {
            node.right = null;
        }

        if (leftSum >= k) {
            return sum + rightSum;
        }
        else {
            node.left = null;
        }

        // Deletes the root if the whole tree is less than k.
        if (root.left == null && root.right == null && (Integer)root.element < k) {
            root = null;
        }

        return sum;

    }

    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        List<E> inOrderList = Arrays.asList(inOrder);
        List<E> preOrderList = Arrays.asList(preOrder);

        root = new BinaryNode<>(preOrder[0]);
        buildTreeTraversals(inOrderList, preOrderList, root);
    }

    private void buildTreeTraversals(List<E> inOrder, List<E> preOrder, BinaryNode<E> node) {

        List<E> leftSubtree;
        List<E> rightSubtree;

        // Left Subtree
        leftSubtree = inOrder.subList(0, inOrder.indexOf(node.element));
        if (!leftSubtree.isEmpty()) {
            E potentialLeft = preOrder.get(preOrder.indexOf(node.element) + 1);

            if (leftSubtree.contains(potentialLeft)) {
                node.left = new BinaryNode<>(potentialLeft);
                buildTreeTraversals(leftSubtree, preOrder, node.left);
            }
        }

        // Right Subtree
        rightSubtree = inOrder.subList(inOrder.indexOf(node.element) + 1, inOrder.size());
        if (!rightSubtree.isEmpty()) {
            ArrayList<E> tempList = new ArrayList<>();
            for (E item : preOrder) {
                if (rightSubtree.contains(item)) tempList.add(item);
            }

            node.right = new BinaryNode<>(tempList.get(0));
            buildTreeTraversals(rightSubtree, preOrder, node.right);
        }
    }

    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {

        if (root == null) return "Empty Tree";

        if (!(this.contains(a) && this.contains(b))) {
            return "none";
        }

        return (lca(root, a, b));
    }

    private String lca(BinaryNode<E> node, E a, E b) {
        if (node == null) return "";

        // We know that both a and b exist in the tree
        // so if the nodes element is equal to a or b at some point, that will be the lca.
        if (node.element == a) {
            return a.toString();
        }
        else if (node.element == b) {
            return b.toString();
        }
        else if ((node.element.compareTo(a) < 0 && node.element.compareTo(b) > 0)
                || (node.element.compareTo(a) > 0 && node.element.compareTo(b) < 0)) return node.element.toString();
        // Both terms are greater than the current node.
        else if (node.element.compareTo(a) < 0 && node.element.compareTo(b) < 0) return lca(node.right, a, b);
        // Both terms are less than the current node.
        else if (node.element.compareTo(a) > 0 && node.element.compareTo(b) > 0) return lca(node.left, a, b);
        else return "";
    }

    public ArrayList<E> getInOrderTraversal() {
        if (root == null) return null;

        ArrayList<E> inOrderTree = new ArrayList<>();

        return getInOrderTraversal(root, inOrderTree);

    }

    private ArrayList<E> getInOrderTraversal(BinaryNode<E> node, ArrayList<E> inOrder) {

        if (node == null) return inOrder;

        getInOrderTraversal(node.left, inOrder);
        inOrder.add(node.element);
        getInOrderTraversal(node.right, inOrder);

        return inOrder;

    }

    /**
     * Balance the tree
     */
    public void balanceTree() {

        List<E> inOrder = getInOrderTraversal();

        this.root = null;
        balanceTree(inOrder, 0, inOrder.size());
    }

    private void balanceTree(List<E> inOrder, int low, int high) {

        if (inOrder.isEmpty()) return;
        if (low > high) return;

        int mid = ((high - low) / 2);

        bstInsert(inOrder.get(mid));

        List<E> lowerSplice = inOrder.subList(low, mid);

        List<E> upperSplice = inOrder.subList(mid+1, high);

        balanceTree(lowerSplice, low, lowerSplice.size());
        balanceTree(upperSplice, low, upperSplice.size());

    }

    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }

        ArrayList<E> keptNodes = new ArrayList<>();

        keptNodes = keepRange(a, b, root, keptNodes);
        Collections.sort(keptNodes);

        root = null;

        balanceTree(keptNodes, 0, keptNodes.size());

    }

    private ArrayList<E> keepRange(E a, E b, BinaryNode<E> node, ArrayList<E> keptNodes) {

        if (node == null) return keptNodes;

        if (node.element.compareTo(a) >= 0 && node.element.compareTo(b) <= 0) {
            keptNodes.add(node.element);
        }

        if (node.element.compareTo(a) < 0) node.left = null;
        if (node.element.compareTo(b) > 0) node.right = null;

        keepRange(a, b, node.left, keptNodes);
        keepRange(a, b, node.right, keptNodes);

        return keptNodes;

    }

    // Basic node stored in unbalanced binary  trees
    private static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }

}
