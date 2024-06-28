import java.util.*;
import java.io.*;

public class Quiz3 {
    static class Edge implements Comparable<Edge> {
        int source, destination;
        double weight;

        public Edge(int src, int dest, double weight) {
            this.source = src;
            this.destination = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    static class DisjointSet {
        Map<Integer, Integer> parent;

        public DisjointSet(int n) {
            parent = new HashMap<>();
            for (int i = 0; i < n; i++) {
                parent.put(i, i);
            }
        }

        public int find(int x) {
            if (parent.get(x) == x) {
                return x;
            }
            int root = find(parent.get(x));
            parent.put(x, root);
            return root;
        }

        public void mergeSets(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                parent.put(rootX, rootY);
            }
        }
    }

    static double calculateDistance(double[] point1, double[] point2) {
        double x1 = point1[0];
        double y1 = point1[1];
        double x2 = point2[0];
        double y2 = point2[1];
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }


    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        int N = Integer.parseInt(reader.readLine());

        for (int t = 0; t < N; t++) {
            String[] inputLine = reader.readLine().split(" ");
            int S = Integer.parseInt(inputLine[0]);
            int P = Integer.parseInt(inputLine[1]);


            double[][] stations = new double[P][2];
            for (int i = 0; i < P; i++) {
                String[] coordinates = reader.readLine().split(" ");
                stations[i][0] = Double.parseDouble(coordinates[0]);
                stations[i][1] = Double.parseDouble(coordinates[1]);
            }

            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < P; i++) {
                for (int j = i + 1; j < P; j++) {
                    double distance = calculateDistance(stations[i], stations[j]);
                    edges.add(new Edge(i, j, distance));
                }
            }

            Collections.sort(edges);

            DisjointSet disjointSet = new DisjointSet(P);
            int count = 0;
            double threshold = 0.0;
            for (Edge edge : edges) {
                int sourceRoot = disjointSet.find(edge.source);
                int destinationRoot = disjointSet.find(edge.destination);
                if (sourceRoot != destinationRoot) {
                    disjointSet.mergeSets(sourceRoot, destinationRoot);
                    threshold = edge.weight;
                    count++;
                }

                if (count == P - S) {
                    break;
                }
            }

            System.out.printf("%.2f\n", threshold);
        }

        reader.close();
    }

}
