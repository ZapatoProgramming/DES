package SDES;

import java.util.Random;

public class SubkeysGenerator {
    public static boolean debug = false;
    public static int[] generateKey(int[] userDefinedKey){
        if(debug) Util.printBits("Provided key: ",userDefinedKey);
        return userDefinedKey;
    }

    public static int[] generateKey() {
        Random random = new Random();
        int key = random.nextInt(1024);

        String BitString = String.format("%10s", Integer.toBinaryString(key))
                .replace(' ', '0');
        if(debug) System.out.printf("Generated key: %s%n", BitString);

        int[] bitArray = new int[10];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = Character.getNumericValue(BitString.charAt(i));
        }

        return bitArray;
    }

    public static int[] permutation10(int[] key) {
        int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        int[] permutedKey = new int[10];

        for (int i = 0; i < P10.length; i++) {
            permutedKey[i] = key[P10[i] - 1];
        }

        if(debug) Util.printBits("P10 applied: ", permutedKey);

        return permutedKey;
    }

    public static int[] circularLeftShift(int[] key) {
        int firstElement = key[0];

        for (int i = 0; i < key.length - 1; i++) {
            key[i] = key[i + 1];
        }

        key[key.length - 1] = firstElement;

        return key;
    }

    public static int[] permutation8(int[] key) {
        int[] P8 = {6,3,7,4,8,5,10,9};
        int[] permutedKey = new int[8];

        for (int i = 0; i < P8.length; i++) {
            permutedKey[i] = key[P8[i] - 1];
        }

        if(debug) Util.printBits("P8 applied: ", permutedKey);

        return permutedKey;
    }
}