using System;
using System.Collections;

class DES
{
    static int[] initialPermutationTable = {
    58, 50, 42, 34, 26, 18, 10,  2,
    60, 52, 44, 36, 28, 20, 12,  4,
    62, 54, 46, 38, 30, 22, 14,  6,
    64, 56, 48, 40, 32, 24, 16,  8,
    57, 49, 41, 33, 25, 17,  9,  1,
    59, 51, 43, 35, 27, 19, 11,  3,
    61, 53, 45, 37, 29, 21, 13,  5,
    63, 55, 47, 39, 31, 23, 15,  7
    };

    static int[] expansionPermutationTable = {
    32,  1,  2,  3,  4,  5,
     4,  5,  6,  7,  8,  9,
     8,  9, 10, 11, 12, 13,
    12, 13, 14, 15, 16, 17,
    16, 17, 18, 19, 20, 21,
    20, 21, 22, 23, 24, 25,
    24, 25, 26, 27, 28, 29,
    28, 29, 30, 31, 32,  1
    };

    static int[] permutationPTable = {
    16,  7, 20, 21,
    29, 12, 28, 17,
     1, 15, 23, 26,
     5, 18, 31, 10,
     2,  8, 24, 14,
    32, 27,  3,  9,
    19, 13, 30,  6,
    22, 11,  4, 25
    };

    static int[] inverseInitialPermutationTable = {
    40,  8, 48, 16, 56, 24, 64, 32,
    39,  7, 47, 15, 55, 23, 63, 31,
    38,  6, 46, 14, 54, 22, 62, 30,
    37,  5, 45, 13, 53, 21, 61, 29,
    36,  4, 44, 12, 52, 20, 60, 28,
    35,  3, 43, 11, 51, 19, 59, 27,
    34,  2, 42, 10, 50, 18, 58, 26,
    33,  1, 41,  9, 49, 17, 57, 25
};



    static void Main(string[] args)
    {
        String plainText = "0000000000000000";
        String key = "0000000000000000";
        int rounds = 16;
        BitArray bitKey = plainTextToHEX(key);
        List<BitArray> subkeys = generateSubkeys(bitKey, rounds);
        BitArray bitPlainText = plainTextToHEX(plainText);
        Console.WriteLine("------------------------ Initial Permutation ------------");
        BitArray IP = BitArrayOperations.Permute(bitPlainText, initialPermutationTable);
        BitArrayOperations.PrintBitArrayInMatrixForm(IP, 8, 8);

        BitArray L = new BitArray(32);
        BitArray R = new BitArray(32);
        for (int i = 0; i < 32; i++) L[i] = IP[i];
        for (int i = 0; i < 32; i++) R[i] = IP[i + 32];

        for (int round = 0; round < rounds; round++)
        {
            Console.WriteLine($"------------------------ Round {round + 1} ------------------------");

            Console.WriteLine("------------------------ Expansion Permutation ----------");
            BitArray expandedR = BitArrayOperations.Permute(R, expansionPermutationTable);
            BitArrayOperations.PrintBitArrayInMatrixForm(expandedR, 8, 6);

            Console.WriteLine($"------------------------ XOR between expanded R y K{round + 1} ----");
            BitArray xorBetweenExpandedRAndKey = BitArrayOperations.XorBitArrays(expandedR, subkeys[round]);
            BitArrayOperations.PrintBitArrayInMatrixForm(xorBetweenExpandedRAndKey, 8, 6);

            Console.WriteLine("------------------------ Apply SBoxes -------------------");
            BitArray resultOfSBoxes = SBoxes.ApplySboxes(xorBetweenExpandedRAndKey);

            Console.WriteLine("------------------------ Apply P Permutation ------------");
            BitArray resultPermutationP = BitArrayOperations.Permute(resultOfSBoxes, permutationPTable);
            BitArrayOperations.PrintBitArrayInMatrixForm(resultPermutationP, 4, 8);
            BitArray newL = R;
            BitArray newR = BitArrayOperations.XorBitArrays(L, resultPermutationP);

            Console.WriteLine($"------------------------ L{round + 1} -----------------------------");
            BitArrayOperations.PrintBitArrayInMatrixForm(newL, 4, 8);
            Console.WriteLine($"------------------------ R{round + 1} -----------------------------");
            BitArrayOperations.PrintBitArrayInMatrixForm(newR, 4, 8);

            L = newL;
            R = newR;
        }
        if (rounds == 16) {
            BitArray temp = L;
            L = R;
            R = temp;
            BitArray swapResult = new BitArray(64);
            for (int i = 0; i < 32; i++)
            {
                swapResult[i] = L[i];   
                swapResult[i + 32] = R[i]; 
            }
            Console.WriteLine("------------------------ Apply Inverse Initial Permutation ---------------------");
            BitArray resultInversePermutation = BitArrayOperations.Permute(swapResult, inverseInitialPermutationTable);
            Console.WriteLine("------------------------ Ciphertext in bits ------------------------------------");
            BitArrayOperations.PrintBitArrayInMatrixForm(resultInversePermutation, 8, 8);
            Console.WriteLine("------------------------ Ciphertext in HEX ------------------------------------");
            Console.WriteLine(BitArrayOperations.BitArrayToHexString(resultInversePermutation));
        }

    }


    static BitArray plainTextToHEX(String plainText) {
        BitArray bitArray = BitArrayOperations.HexStringToBitArray(plainText);
        return bitArray;
    }

    static List<BitArray> generateSubkeys(BitArray key, int rounds) {
        SubkeysGeneration subkeysGeneration = new SubkeysGeneration(key,rounds);
        List<BitArray> subkeys = subkeysGeneration.GetSubkeys();
        return subkeys;
    }
}

