package ESDES;

import java.util.Arrays;

public class Methods {
    static boolean debug = false;
    public static char[][] columnTranspositionWithMultipleRounds(char[][] matrix,int[] randomNumber, int rounds){
        System.out.println("------------------------ First we fill the matrix with the plain text --------------------");
        System.out.println("Matrix: ");
        Util.printMatrix(matrix);
        System.out.println("------------------------ Then the column transposition with the random number " +
                Arrays.toString(randomNumber) + " and "+ rounds+" rounds --------------------");
        for (int r = 0; r < rounds; r++) {
            matrix = transposeMatrix(matrix, randomNumber);
            System.out.println("Después de la ronda " + (r + 1) + ":");
            Util.printMatrix(matrix);
        }

        return matrix;
    }

    public static char[][] transposeMatrix(char[][] matrix, int[] randomNumber) {
        int dimension = matrix.length;
        char[][] result = new char[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result[i][j] = matrix[j][randomNumber[i] - 1];
            }
        }
        return result;
    }

    public static String shiftRowsStage(char[][] transposedMatrix, boolean encrypt) {
        int rows = transposedMatrix.length;

        for (int i = 1; i < rows; i++) {
            int shiftAmount = encrypt ? i : rows - i;
            transposedMatrix[i] = shiftRow(transposedMatrix[i], shiftAmount);
        }

        StringBuilder shiftedString = new StringBuilder();
        for (char[] row : transposedMatrix) {
            for (char c : row) {
                shiftedString.append(c);
            }
        }
        return shiftedString.toString();
    }

    public static char[] shiftRow(char[] row, int shift){
        int length = row.length;
        char[] shiftedRow = new char[length];

        for (int i = 0; i < length; i++) {
            shiftedRow[i] = row[(i + shift) % length];
        }

        return shiftedRow;
    }

    public static char[][] inverseColumnTranspositionMultipleRounds(char[][] matrix, int[] randomNumber, int rounds){
        for (int r = 0; r < rounds; r++) {
            matrix = inverseColumnTransposition(matrix, randomNumber);
            if(debug) System.out.println("Después de la ronda " + (r + 1) + ":");
            if(debug) Util.printMatrix(matrix);
        }
        return matrix;
    }

    public static char[][] inverseColumnTransposition(char[][] matrix, int[] randomNumber) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        char[][] result = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < randomNumber.length; j++) {
                result[j][randomNumber[i] - 1] = matrix[i][j];
            }
        }

        return result;
    }

}
