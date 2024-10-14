package SDES;

import java.util.Arrays;

public class Main {

    public static int[] GlobalK1;
    public static int[] GlobalK2;

    public static void main(String[] args) {
        subkeysGeneration();
        Encryption();

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

        int[] previousP8K2 = Util.mergeArrays(doubleCircularLeftShiftofL, doubleCircularLeftShiftofR);
        Util.printBits("Double circular left shift of R + Double circular left shift of L: ",
                previousP8K2);

        System.out.println("------------------------ Finally, we apply P8 to obtain subkey K2" +
                " ------------------------");

        int[] K2 = SubkeysGenerator.permutation8(previousP8K2);
        Util.printBits("K2: ", K2);

        System.out.println("---------------- Keys --------------------");
        Util.printBits("K1: ", K1);
        Util.printBits("K2: ", K2);
        GlobalK1 = K1;
        GlobalK2 = K2;
    }

    public static void Encryption(){
        System.out.println("------------------------ ENCRYPTION PROCESS" +
                " ------------------------");

        System.out.println("------------------------ Apply Initial Permutation" +
                " ------------------------");
        int[] permutedBlock = Encryption.initialPermutation(new int[]{1,0,1,1,1,1,0,1});

        int[] L = Arrays.copyOfRange(permutedBlock, 0, permutedBlock.length/2);
        Util.printBits("L: ", L);
        int[] R = Arrays.copyOfRange(permutedBlock, permutedBlock.length/2,
                permutedBlock.length);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K2" +
                " ---------------------------------");
        int[] resultFk = Encryption.f_k(L,R,GlobalK1);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Swap L and R" +
                " ---------------------------------");
        L = Arrays.copyOfRange(resultFk, resultFk.length / 2, resultFk.length);
        Util.printBits("L: ", L);
        R = Arrays.copyOfRange(resultFk, 0, resultFk.length / 2);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K2" +
                " ---------------------------------");
        resultFk = Encryption.f_k(L,R,GlobalK2);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Finally apply Inversed IP" +
                " ---------------------------------");
        int[] encrypted8BitBlock = Encryption.inversedIP(resultFk);
        Util.printBits("Inversed IP applied: ", encrypted8BitBlock);
        Util.printBits("Encrypted 8-bit block: ", encrypted8BitBlock);

    }

}