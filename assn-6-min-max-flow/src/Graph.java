import java.io.File;
import java.util.*;

public class Graph {
    private int vertexCt;  // Number of vertices in the graph.
    private int[][] capacity;  // Adjacency  matrix
    private int[][] residual; // residual matrix
    private int[][] edgeCost; // cost of edges in the matrix
    private String graphName;  //The file from which the graph was created.
    private int source = 0; // start of all paths
    private int sink; // end of all paths
    private int[] pred;

    public Graph(String fileName) {
        this.vertexCt = 0;
        source  = 0;
        this.graphName = "";
        makeGraph(fileName);

    }

    /**
     * Method to add an edge
     *
     * @param source      start of edge
     * @param destination end of edge
     * @param cap         capacity of edge
     * @param weight      weight of edge, if any
     * @return edge created
     */
    private boolean addEdge(int source, int destination, int cap, int weight) {
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        capacity[source][destination] = cap;
        residual[source][destination] = cap;
        edgeCost[source][destination] = weight;
        edgeCost[destination][source] = -weight;
        return true;
    }

    /**
     * Method to get a visual of the graph
     *
     * @return the visual
     */
    public String printMatrix(String label, int[][] m) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n " + label+ " \n     ");
        for (int i=0; i < vertexCt; i++)
            sb.append(String.format("%5d", i));
        sb.append("\n");
        for (int i = 0; i < vertexCt; i++) {
            sb.append(String.format("%5d",i));
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d",m[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method to make the graph
     *
     * @param filename of file containing data
     */
    private void makeGraph(String filename) {
        try {
            graphName = filename;
            System.out.println("\n****Find Flow " + filename);
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            pred = new int[vertexCt];
            capacity = new int[vertexCt][vertexCt];
            residual = new int[vertexCt][vertexCt];
            edgeCost = new int[vertexCt][vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                for (int j = 0; j < vertexCt; j++) {
                    capacity[i][j] = 0;
                    residual[i][j] = 0;
                    edgeCost[i][j] = 0;
                }
            }

            // If weights, need to grab them from file
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                int weight = reader.nextInt();
                if (!addEdge(v1, v2, cap, weight))
                    throw new Exception();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sink = vertexCt - 1;
        System.out.println( printMatrix("Edge Cost" ,edgeCost));
    }


    public void minCostMaxFlow(){
        System.out.println( printMatrix("Capacity", capacity));
        System.out.println(printMatrix("Residual", residual));

        int value = 0;
        while (hasCheapestAugmentedPathExists()) {
            // Prints the cheapest path
            StringBuilder sb = new StringBuilder();
            for (int x=sink; x!=Integer.MIN_VALUE; x=pred[x]) {
                sb.append(x + " ");
            }
            sb.reverse();

            // The Real Lifting Starts Here
            int counter = 0;
            int prev;
            int availableFlow = Integer.MAX_VALUE;
            // Loop through from back to front to see what the max flow of our shortest path is.
            for (int v=sink; v!=source; v=prev) {
                prev = pred[v];
                availableFlow = Math.min(availableFlow, residual[prev][v]);
                counter++;
            }
            /*
             Now that we know how much we can send through,
             update the augmented matrix by subtracting the
             available flow from the path.
             NOTE: SOURCE and SINK should NEVER have a predecessor
                   Otherwise you hit an infinite loop.
             */
            int[] visitedPath = new int[counter+1];
            int counter2 = 0;
            int cost = 0;
            for (int v=sink; v!=source; v=prev) {
                visitedPath[counter2] = v;
                prev = pred[v];
                residual[prev][v] = (residual[prev][v] - availableFlow);
                cost += edgeCost[prev][v];
                residual[v][prev] = availableFlow;
                counter2++;
            }
            value += availableFlow;
            sb.append(" | Flow: " + value + " | Cost: " + cost);
            System.out.println(sb.toString());
        }
        System.out.println("\nTotal Flow Used: " + value);
        System.out.println(printMatrix("End Residual", residual));
        // Prints the final flow (Bottom half of the end residual matrix
        System.out.println("Final Flow and Cost On Each Edge");
        for (int u=0; u<vertexCt; u++) {
            for (int v=0; v<vertexCt; v++) {
                if (v>=u) continue;
                if (residual[u][v] != 0) {
                    System.out.println("Flow " + v + " -> " + u + " (" + residual[u][v] + ") Cost: " + edgeCost[v][u]);
                }
            }
        }

    }

    // Lets us know if a shortest path exists.
    public boolean hasCheapestAugmentedPathExists() {
        Arrays.fill(pred, Integer.MIN_VALUE);

        double[] cost = new double[vertexCt];
        Arrays.fill(cost, Double.POSITIVE_INFINITY);
        cost[source] = 0;

        // Print the path here.
        for (int i=0; i<vertexCt; i++) {
            for (int u=0; u<vertexCt; u++) {
                for (int v=0; v<vertexCt; v++) {
                    if (residual[u][v] != 0 && ((cost[u] + edgeCost[u][v]) < cost[v])) {
                        cost[v] = cost[u] + edgeCost[u][v];
                        pred[v] = u;
                    }
                }
            }
        }
        return pred[sink] != Integer.MIN_VALUE;
    }

    public static void main(String[] args) {
        String[] files = {"match0.txt", "match1.txt", "match2.txt", "match3.txt", "match4.txt", "match5.txt","match6.txt", "match7.txt", "match8.txt", "match9.txt"};
        for (String fileName : files) {
            Graph graph = new Graph(fileName);
            graph.minCostMaxFlow();
        }
    }
}