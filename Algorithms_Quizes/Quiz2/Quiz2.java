import java.util.*;
import java.io.*;

public class Quiz2 {
    public static void main(String[] args) throws IOException {

        String inputFile = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String[] line1 = br.readLine().split(" ");
            int M = Integer.parseInt(line1[0]);

            String[] line2 = br.readLine().split(" ");
            int[] cargoWeights = Arrays.stream(line2)
                    .mapToInt(Integer::parseInt)
                    .toArray();;

            int maxCargo = loadCargo(M, cargoWeights);
            int[][] matrix = calculateCargoMatrix(M, cargoWeights);

            System.out.println(maxCargo);

            for (int[] i : matrix) {
                for (int j : i) {
                    System.out.print(j);
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadCargo(int M, int[] cargoWeights) {
        int[][] dp = new int[cargoWeights.length + 1][M + 1];

        for (int i = 1; i <= cargoWeights.length; i++) {
            for (int m = 0; m <= M; m++) {
                dp[i][m] = dp[i - 1][m];
                if (cargoWeights[i - 1] <= m) {
                    dp[i][m] = Math.max(dp[i][m], dp[i - 1][m - cargoWeights[i - 1]] + cargoWeights[i - 1]);
                }
            }
        }

        return dp[cargoWeights.length][M];
    }

    public static int[][] calculateCargoMatrix(int M, int[] cargoWeights) {
        int[][] matrix = new int[M + 1][cargoWeights.length + 1];

        matrix[0][0] = 1;
        for (int m = 1; m <= M; m++) {
            matrix[m][0] = 0;
        }

        for (int m = 0; m <= M; m++) {
            for (int i = 1; i <= cargoWeights.length; i++) {
                if (cargoWeights[i - 1] > m) {
                    matrix[m][i] = matrix[m][i - 1];
                } else {
                    matrix[m][i] = matrix[m][i - 1] | matrix[m - cargoWeights[i - 1]][i - 1];
                }
            }
        }

        return matrix;
    }
}