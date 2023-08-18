public class Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int totalNodesAdded;
    private int totalNodesRemoved;

    public Queue() { this.size = 0; }

    public int getSize() { return this.size; }
    public boolean isEmpty() { return this.size == 0; }
    public int getTotalNodesAdded() { return this.totalNodesAdded; }
    public int getTotalNodesRemoved() { return this.totalNodesRemoved; }

    public void printContents() {

        Node<E> current = head;

        while (current != null) {
            System.out.print(current.element + ", ");
            current = current.next;
        }
        System.out.println();

    }


    public void add(E value) {

        Node<E> node = new Node<>(value);

        // If the queue is empty
        if (isEmpty()) {
            head = node;
        }
        else {
            tail.next = node;
        }
        tail = node;
        this.totalNodesAdded++;
        this.size++;

    }

    public E remove() {

        if (isEmpty()) {
            System.out.println("ERROR: Tried to remove a node but the list was empty.");
            return null;
        }
        else {
            Node<E> node = head;
            head = head.next;

            this.size--;
            this.totalNodesRemoved++;
            return node.element;
        }
    }

    private static class Node<E> {
        public E element;
        public Node<E> next;

        public Node(E element) {
            this(element, null);
        }

        public Node(E element, Node<E> node) {
            this.element = element;
            this.next = node;
        }
    }

    public static void main(String[] args) {

        Queue<String> queue = new Queue<>();
        queue.add("Justin");
        queue.add("Emily");
        queue.add("Spencer");
        queue.add("Ethan");

        System.out.println(queue.remove());
        System.out.println();


        queue.printContents();
        System.out.println(queue.getSize());
        System.out.println();

        System.out.println(queue.remove());
        System.out.println();
        queue.remove();

        queue.printContents();
        System.out.println(queue.getSize());
        System.out.println();

        queue.add("Zelda");
        queue.add("Ryu");

        queue.printContents();
        System.out.println();

        queue.remove();
        queue.printContents();

    }

}
