package ESDES;

import SDES.SDES;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String plainText = "NO, I AM YOUR FATHER";
        int dimension = (int) Math.ceil(Math.sqrt(plainText.length()));
        int[] randomNumber = Util.generateRandomColumnOrder(dimension);
        int rounds = 2;
        char[][] matrix = Util.fillMatrix(plainText, dimension);

        char[][] transposedMatrix = Methods.columnTranspositionWithMultipleRounds(matrix,randomNumber,rounds);

        String shiftedString = Methods.shiftRowsStage(transposedMatrix, true);

        System.out.println("The string input of S-DES is: "+shiftedString);
        SDES sdes = new SDES();
        sdes.setSameKeyTrue();
        sdes.init(shiftedString);

        System.out.println("------------- Then we fill a matrix and apply the shiftRowsStage in inverse ---------------");
        StringBuilder sdesDecryptedText = sdes.getDecryptedText();
        char[][] decryptedMatrix = Util.fillMatrix(String.valueOf(sdesDecryptedText),dimension);
        System.out.println("Matrix to apply the shiftRowsStage in inverse is: ");
        Util.printMatrix(decryptedMatrix);
        String decryptedShifted = Methods.shiftRowsStage(decryptedMatrix,false);
        char[][] decryptedShiftedMatrix = Util.fillMatrix(decryptedShifted,dimension);
        System.out.println("Matrix after shiftRowsStage in inverse: ");
        Util.printMatrix(decryptedShiftedMatrix);
        System.out.println("------------- Finally apply the column transposition ---------------");
        System.out.println("Random number: "+ Arrays.toString(randomNumber));
        char[][] decryptedTransposedMatrix = Methods.inverseColumnTranspositionMultipleRounds(decryptedShiftedMatrix,randomNumber,rounds);
        System.out.println("The final decrypted text of the Enhanced S-DES is: "+Util.matrixToString(decryptedTransposedMatrix));
    }
}
