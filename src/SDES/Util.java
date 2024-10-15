package SDES;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    public static void printBits(String message, int[] bits) {
        System.out.println(message + Arrays.stream(bits)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining()));
    }

    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        return IntStream.concat(Arrays.stream(arr1), Arrays.stream(arr2))
                .toArray();
    }

    public static void printMatrix(String message, int[][] matrix) {
        System.out.println(message);
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public static int[] charToEightBitBlock(char character) {
        int[] bitBlock = new int[8];

        int asciiValue = (int) character;

        for (int i = 7; i >= 0; i--) {
            bitBlock[i] = asciiValue & 1;
            asciiValue >>= 1;
        }

        return bitBlock;
    }

    public static char eightBitBlockToChar(int[] eightBitBlock) {
        int asciiValue = 0;

        for (int i = 0; i < 8; i++) {
            asciiValue = (asciiValue << 1) | eightBitBlock[i];
        }

        return (char) asciiValue;
    }

    public static int[] parseKeyInput(String keyInput) {
        int[] key = new int[8];
        for (int i = 0; i < 8; i++) {
            char bit = keyInput.charAt(i);
            if (bit == '0' || bit == '1') {
                key[i] = bit - '0';
            }
        }
        return key;
    }

}
