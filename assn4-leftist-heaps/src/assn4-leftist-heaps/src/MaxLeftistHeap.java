public class MaxLeftistHeap<AnyType extends Comparable<? super AnyType>> {

    public MaxLeftistHeap() { root = null; }

    // Recursive method that merges to leftistHeaps
    public void merge(MaxLeftistHeap<AnyType> rhs) {
        if (this == rhs) { return; }

        root = merge(root, rhs.root);
        rhs.root = null;
    }

    private Node<AnyType> merge (Node<AnyType> root1, Node<AnyType> root2) {
        // Check so we aren't following a null reference.
        if (root1 == null) { return root2; }

        if (root2 == null) { return root1; }

        // Merges root1 with root2 if root1 is greater than root2
        if (root1.element.compareTo(root2.element) > 0) { return merge1(root1, root2); }
        // Otherwise merge root2 with root1
        else { return merge1(root2, root1); }
    }

    private Node<AnyType> merge1(Node<AnyType> node1, Node<AnyType> node2) {
        // If node1.left == null, it is a single node.
        if (node1.left == null) {
            node1.left = node2;
        }
        else {
            node1.right = merge(node1.right, node2);
            // Checks to see if it follows the properties of a leftist heap and forces it to conform if it doesn't.
            if (node1.left.npl < node1.right.npl) {
                swapChildren( node1 );
            }
            node1.npl = node1.right.npl + 1;
        }
        return node1;
    }

    public void insert(AnyType element) {
        root = merge( new Node<>(element), root);
        size++;
    }

    public AnyType findMax() {
        return root.element;
    }

    public AnyType deleteMax() {
        if (isEmpty()) { return null; }

        AnyType maxItem = root.element;
        root = merge(root.left, root.right);

        return maxItem;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void makeEmpty() {
        root = null;
    }

    public String toString() {
        if (root == null) { return " Empty Tree"; }
        else { return toString(root, 0); }
    }

    private String toString(Node<AnyType> node, int depth) {
        if (node == null) { return ""; }
        StringBuilder sb = new StringBuilder();
        sb.append(toString(node.right, depth+1));
        sb.append("  ".repeat(depth) + node.element + "\n");
        sb.append(toString(node.left, depth+1));
        return sb.toString();
    }

    // CAUTION: This toString method will delete the whole tree!!!
    // Only use this if you are okay with making the tree null!!!
    public void toString2() {
        if (root == null) {
            return;
        } else {
            if (!(this.isEmpty())) {
                System.out.println(deleteMax());
                toString2();
            }
        }
    }

    private static class Node<AnyType> {

        AnyType element;  // This nodes element
        Node<AnyType> left;  // Left Child
        Node<AnyType> right; // Right Child
        int npl; // Null path length

        // Constructors
        Node(AnyType element) {
            this (element, null, null);
        }

        Node(AnyType element, Node<AnyType> lt, Node<AnyType> rt) {
            this.element = element; left = lt; right = rt; npl=0;
        }
    }

    private Node<AnyType> root; //Root of tree

    private int size;

    public int getSize() {
        return this.size;
    }

    private void swapChildren( Node<AnyType> node) {
        // Swaps the left and right child of a node.
        Node<AnyType> tempNode = node.left;
        node.left = node.right;
        node.right = tempNode;
    }

    public static void main(String[] args) {
        Integer arr[] = {13, 12, 11, 7, 6, 5};
        MaxLeftistHeap heap = new MaxLeftistHeap();

        for (int i = 0; i<arr.length; i++) {
            heap.insert(arr[i]);
        }

        System.out.println(heap.toString());
    }

}
