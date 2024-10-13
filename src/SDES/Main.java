package SDES;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        subkeysGeneration();

    }

    public static void subkeysGeneration(){
        int[] key = SubkeysGenerator.generateKey(new int[]{1,0,1,0,0,0,0,0,1,0});

        System.out.println("------------------------ First, apply P10 to the key and circular left shift to L and R" +
                " of the result ------------------------");

        int[] P10 = SubkeysGenerator.permutation10(key);

        int[] L = Arrays.copyOfRange(P10, 0, key.length / 2);
        Util.printBits("L: ", L);
        int[] circularLeftShiftofL = SubkeysGenerator.circularLeftShift(L);
        Util.printBits("Circular left shift of L: ", circularLeftShiftofL);

        int[] R = Arrays.copyOfRange(P10, key.length / 2, key.length);
        Util.printBits("R: ", R);
        int[] circularLeftShiftofR = SubkeysGenerator.circularLeftShift(R);
        Util.printBits("Circular left shift of R: ", circularLeftShiftofR);

        int[] previousP8K1 = Util.mergeArrays(circularLeftShiftofL, circularLeftShiftofR);
        Util.printBits("Circular left shift of R + Circular left shift of L: ",
                previousP8K1);

        System.out.println("------------------------ Then apply P8 to obtain subkey K1" +
                " ------------------------");

        int[] K1 = SubkeysGenerator.permutation8(previousP8K1);
        Util.printBits("K1: ", K1);

        System.out.println("------------------------ After that, we do a double circular left shif to" +
                " Circular left shift of R and Circular left shift of L ------------------------");

        int[] doubleCircularLeftShiftofL = SubkeysGenerator.circularLeftShift(
                SubkeysGenerator.circularLeftShift(circularLeftShiftofL)
        );
        Util.printBits("Double circular left shift of L: ", doubleCircularLeftShiftofL);

        int[] doubleCircularLeftShiftofR = SubkeysGenerator.circularLeftShift(
                SubkeysGenerator.circularLeftShift(circularLeftShiftofR)
        );
        Util.printBits("Double circular left shift of R: ", doubleCircularLeftShiftofR);

        int[] previousP8K2 = Util.mergeArrays(circularLeftShiftofL, circularLeftShiftofR);
        Util.printBits("Double circular left shift of R + Double circular left shift of L: ",
                previousP8K2);

        System.out.println("------------------------ Finally, we apply P8 to obtain subkey K2" +
                " ------------------------");

        int[] K2 = SubkeysGenerator.permutation8(previousP8K2);
        Util.printBits("K2: ", K2);

        System.out.println("---------------- Keys --------------------");
        Util.printBits("K1: ", K1);
        Util.printBits("K2: ", K2);
    }
}