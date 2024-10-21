package Bruteforce;

import ESDES.Methods;
import ESDES.Util;
import SDES.SDES;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bruteforce {
    public static void main(String[] args) {

        int[] nums = {1, 2, 3, 4, 5};
        List<List<Integer>> permutations = new ArrayList<>();
        permute(nums, 0, permutations);

        String cipherText = "Æs]þüsþ»6þÔþþþþþþúÈ»ÍÆ";
        int keySize = 10;

        SDES sdes = new SDES();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            for (int key = 0; key < (1 << keySize); key++) {
                int[] testKey = generateKey(key, keySize);
                StringBuilder tempCipherText = new StringBuilder(cipherText);
                StringBuilder decryptedText = sdes.justDecryptEachBlock(tempCipherText, testKey);

                writer.write("Clave probada: " + key + " -> Texto descifrado: " + decryptedText + "\n");

                char[][] decryptedMatrix = Util.fillMatrix(String.valueOf(decryptedText), nums.length);
                String decryptedShifted = Methods.shiftRowsStage(decryptedMatrix, false);
                char[][] decryptedShiftedMatrix = Util.fillMatrix(decryptedShifted, nums.length);

                for (List<Integer> perm : permutations) {
                    int[] columnOrder = perm.stream().mapToInt(i -> i).toArray();
                    char[][] decryptedTransposedMatrix = Methods.inverseColumnTranspositionMultipleRounds(decryptedShiftedMatrix, columnOrder, 2);

                    writer.write("Permutación probada: " + Arrays.toString(columnOrder) + "\n");
                    writer.write("Texto final descifrado del Enhanced S-DES es: " + Util.matrixToString(decryptedTransposedMatrix) + "\n");
                }
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] generateKey(int key, int keySize) {
        int[] keyBits = new int[keySize];
        for (int i = keySize - 1; i >= 0; i--) {
            keyBits[i] = (key >> i) & 1;
        }
        return keyBits;
    }

    // Función recursiva para generar permutaciones
    public static void permute(int[] nums, int index, List<List<Integer>> result) {
        if (index == nums.length) {
            List<Integer> currentPermutation = new ArrayList<>();
            for (int num : nums) {
                currentPermutation.add(num);
            }
            result.add(currentPermutation);
            return;
        }

        for (int i = index; i < nums.length; i++) {
            swap(nums, i, index);
            permute(nums, index + 1, result);
            swap(nums, i, index);  // Deshacer el cambio
        }
    }

    // Función para intercambiar elementos en el arreglo
    public static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}


