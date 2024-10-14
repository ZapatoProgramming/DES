package SDES;

public class Encryption {
    public static int[] initialPermutation(int[] eightBlockBits){

        Util.printBits("8-bit block: ", eightBlockBits);

        int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};

        int[] permutedBlock = new int[eightBlockBits.length];
        for(int i = 0; i < eightBlockBits.length; i++){
            permutedBlock[i] = eightBlockBits[IP[i] - 1];
        }

        Util.printBits("Initial Permutation of 8-bit block: ", permutedBlock);
        return permutedBlock;
    }
    public static int[] expansionPermutation(int[] block){
        int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};

        int[] expandedPermutedBlock = new int[EP.length];
        for(int i = 0; i < EP.length; i++){
            expandedPermutedBlock[i] = block[EP[i] - 1];
        }

        Util.printBits("Expanded Permutation: ", expandedPermutedBlock);
        return expandedPermutedBlock;
    }
    public static int[] XOR(int[] EP, int[] K1) {
        int[] result = new int[EP.length];

        for (int i = 0; i < EP.length; i++) {
            result[i] = EP[i] ^ K1[i];
        }

        Util.printBits("Result of XOR operation: ", result);

        return result;
    }
    public static int[] f_k(int[] L, int[] R, int[] sk) {
        System.out.println("------------------------ First get F(R,SK)" +
                " -----------");
        int[] EP = Encryption.expansionPermutation(R);

        int[] resultXOR = Encryption.XOR(EP, sk);

        int[][] matrix = new int[][]{
                {resultXOR[0], resultXOR[1], resultXOR[2], resultXOR[3]},
                {resultXOR[4], resultXOR[5], resultXOR[6], resultXOR[7]},
        };
        Util.printMatrix("Matrix to fed S0 and S1 box: ",matrix);

        int[][] S0 = {
                {1, 0, 3, 2},
                {3, 2, 1, 0},
                {0, 2, 1, 3},
                {3, 1, 3, 2}
        };
        int[] resultS0 = sBox(matrix[0], S0);
        Util.printBits("Result of Sbox0: ", resultS0);

        int[][] S1 = {
                {0, 1, 2, 3},
                {2, 0, 1, 3},
                {3, 0, 1, 0},
                {2, 1, 0, 3}
        };
        int[] resultS1 = sBox(matrix[1], S1);
        Util.printBits("Result of Sbox1: ", resultS1);

        int[] previousP4 = Util.mergeArrays(resultS0, resultS1);
        Util.printBits("Apply P4 to: ", previousP4);

        int[] resultP4 = permutation4(previousP4);
        Util.printBits("Result of F(R,SK): ", resultP4);

        System.out.println("------------------------ Do XOR of L and F(R,SK)" +
                " -----------");

        return Util.mergeArrays(XOR(L, resultP4), R);
    }
    public static int[] sBox(int[] p, int[][] sbox){

        int row = (p[0] << 1) | p[3];
        int col = (p[1] << 1) | p[2];

        int sboxValue = sbox[row][col];

        return new int[] { (sboxValue >> 1) & 1, sboxValue & 1 };
    }

    public static int[] permutation4(int[] block){
        int[] P4 = {2, 4, 3, 1};

        int[] permutedBlock = new int[block.length];
        for(int i = 0; i < block.length; i++){
            permutedBlock[i] = block[P4[i] - 1];
        }

        Util.printBits("Permutation 4 applied: ", permutedBlock);

        return permutedBlock;
    }

    public static int[] inversedIP(int[] block) {
        int[] IP_INVERSE = {4, 1, 3, 5, 7, 2, 8, 6};

        int[] permutedBlock = new int[8];
        for (int i = 0; i < 8; i++) {
            permutedBlock[i] = block[IP_INVERSE[i] - 1];
        }

        return permutedBlock;
    }

}
