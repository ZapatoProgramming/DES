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

    public static void printBits(int[] bits) {
        System.out.println(Arrays.stream(bits)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining()));
    }

    public static int[] mergeArrays(int[] arr1, int[] arr2) {
        return IntStream.concat(Arrays.stream(arr1), Arrays.stream(arr2))
                .toArray();
    }
}
