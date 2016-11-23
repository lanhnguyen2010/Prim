import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Prims {
    private boolean unsettled[];
    private boolean settled[];
    private int numberofvertices;
    private int adjacencyMatrix[][];
    private int key[];
    public static final int INFINITE = 999;
    private int parent[];
    private ArrayList<Integer> longest = new ArrayList();
    private int longestLength = 0;

    public Prims(int numberofvertices) {
        this.numberofvertices = numberofvertices;
        unsettled = new boolean[numberofvertices + 1];
        settled = new boolean[numberofvertices + 1];
        adjacencyMatrix = new int[numberofvertices + 1][numberofvertices + 1];
        key = new int[numberofvertices + 1];
        parent = new int[numberofvertices + 1];
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public int getUnsettledCount(boolean unsettled[]) {
        int count = 0;
        for (int index = 0; index < unsettled.length; index++) {
            if (unsettled[index]) {
                count++;
            }
        }
        return count;
    }

    public void primsAlgorithm() {
        int evaluationVertex;
        for (int source = 1; source <= numberofvertices; source++) {
            for (int destination = 1; destination <= numberofvertices; destination++) {
                this.adjacencyMatrix[source][destination] = adjacencyMatrix[source][destination];
            }
        }

        for (int index = 1; index <= numberofvertices; index++) {
            key[index] = INFINITE;
        }
        key[1] = 0;
        unsettled[1] = true;
        parent[1] = 1;

        while (getUnsettledCount(unsettled) != 0) {
            evaluationVertex = getMimumKeyVertexFromUnsettled(unsettled);
            unsettled[evaluationVertex] = false;
            settled[evaluationVertex] = true;
            evaluateNeighbours(evaluationVertex);
        }
    }

    private int getMimumKeyVertexFromUnsettled(boolean[] unsettled2) {
        int min = Integer.MAX_VALUE;
        int node = 0;
        for (int vertex = 1; vertex <= numberofvertices; vertex++) {
            if (unsettled[vertex] == true && key[vertex] < min) {
                node = vertex;
                min = key[vertex];
            }
        }
        return node;
    }

    public void evaluateNeighbours(int evaluationVertex) {

        for (int destinationvertex = 1; destinationvertex <= numberofvertices; destinationvertex++) {
            if (settled[destinationvertex] == false) {
                if (adjacencyMatrix[evaluationVertex][destinationvertex] != INFINITE) {
                    if (adjacencyMatrix[evaluationVertex][destinationvertex] < key[destinationvertex]) {
                        key[destinationvertex] = adjacencyMatrix[evaluationVertex][destinationvertex];
                        parent[destinationvertex] = evaluationVertex;
                    }
                    unsettled[destinationvertex] = true;
                }
            }
        }
    }

    public void printMST() {
        System.out.println("SOURCE  : DESTINATION = WEIGHT");
        for (int vertex = 2; vertex <= numberofvertices; vertex++) {
            System.out.println(parent[vertex] + "\t:\t" + vertex + "\t=\t" + adjacencyMatrix[parent[vertex]][vertex]);
        }
    }

    public void findLongestPath() {

        for (int vertex = 2; vertex <= numberofvertices; vertex++) {
            if (isLeave(vertex)) {
                int i = vertex;
                System.out.println("path from 1 to " + vertex);
                ArrayList<Integer> path = new ArrayList();
                while (i != 1) {
                    System.out.println(i);
                    path.add(i);
                    i = parent[i];
                }
                path.add(1);
                System.out.println("path length: " + sumArray(path));
                if (sumArray(path) > sumArray(longest)) {
                    longest = path;
                }

            }
        }
        System.out.println("longest path: ");
        for (int i : longest) {
            System.out.println(i);
        }
        System.out.println("longest path length: " + sumArray(longest));

    }

    public boolean isLeave(int vertex) {
        for (int i = 1; i <= numberofvertices; i++) {
            if (parent[i] == vertex) {
                return false;
            }
        }
        return true;
    }

    public int sumArray(ArrayList<Integer> path) {
        int sum = 0;
        for (int i : path) {
            if (i != 1) {
                sum += adjacencyMatrix[parent[i]][i];
            }
        }
        return sum;
    }

    public void adaptWeight(int maxLength) {
        Collections.reverse(longest);
        int length = 0;
        for (int i =0; i < longest.size(); i++) {
            if (i>0) {
                length += adjacencyMatrix[longest.get(i-1)][longest.get(i)];
                if (length >= maxLength && i>=2) {
                    System.out.println("incorrect length: "+ length);
                    int w = length - maxLength;
                    adjacencyMatrix[longest.get(i-1)][longest.get(i)] += w;
                    adjacencyMatrix[longest.get(i-2)][longest.get(i-1)] += w;
                    adjacencyMatrix[longest.get(i-3)][longest.get(i-2)] += w;
                }
            }
        }
    }

    public int getLongestLength() {
        return longestLength;
    }

    public static void main(String... arg) {
        int adjacency_matrix[][];
        int number_of_vertices;
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("Enter the number of vertices");
            number_of_vertices = scan.nextInt();
            adjacency_matrix = new int[number_of_vertices + 1][number_of_vertices + 1];

            System.out.println("Enter the Weighted Matrix for the graph");
            for (int i = 1; i <= number_of_vertices; i++) {
                for (int j = 1; j <= number_of_vertices; j++) {
                    adjacency_matrix[i][j] = scan.nextInt();
                    if (i == j) {
                        adjacency_matrix[i][j] = 0;
                        continue;
                    }
                    if (adjacency_matrix[i][j] == 0) {
                        adjacency_matrix[i][j] = INFINITE;
                    }
                }
            }

            Prims prims = new Prims(number_of_vertices);
            int maxLength = 9;
            prims.setAdjacencyMatrix(adjacency_matrix);
            while (prims.getLongestLength() <= maxLength) {
                prims.primsAlgorithm();
                prims.printMST();
                prims.findLongestPath();
                prims.adaptWeight(maxLength);
            }

        } catch (InputMismatchException inputMismatch) {
            System.out.println("Wrong Input Format");
        }
        scan.close();
    }
}