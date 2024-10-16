package ESDES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Util {

    public static int[] generateRandomColumnOrder(int dimension) {
        List<Integer> columnOrder = new ArrayList<>();

        for (int i = 1; i <= dimension; i++) {
            columnOrder.add(i);
        }

        Collections.shuffle(columnOrder, new Random());

        int[] result = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            result[i] = columnOrder.get(i);
        }

        return result;
    }
    public static char[][] fillMatrix(String plainText, int dimension) {
        char[][] matrix = new char[dimension][dimension];
        StringBuilder plainTextBuilder = new StringBuilder(plainText);
        int k = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if(k < plainTextBuilder.length()) matrix[i][j] = plainTextBuilder.charAt(k);
                else matrix[i][j] = ' ';                k++;
            }
        }
        return matrix;
    }
    public static void printMatrix(char[][] matrix) {
        for (char[] chars : matrix) {
            for (char aChar : chars) {
                System.out.print(aChar + "\t");
            }
            System.out.println();
        }
    }
    public static String matrixToString(char[][] matrix) {
        StringBuilder string = new StringBuilder();
        for (char[] chars : matrix) {
            for (char aChar : chars) {
                string.append(aChar);
            }
        }
        return string.toString();
    }
}
