using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public class SubkeysGeneration
{
    int[] PC1 = {
        57, 49, 41, 33, 25, 17,  9,
        1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
    };

    int[] PC2 = {
        14, 17, 11, 24,  1,  5,  3, 28,
        15,  6, 21, 10, 23, 19, 12,  4,
        26,  8, 16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55, 30, 40,
        51, 45, 33, 48, 44, 49, 39, 56,
        34, 53, 46, 42, 50, 36, 29, 32
    };

    static int[] shiftsPerRound = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    List<BitArray> subkeys;
    public List<BitArray> GetSubkeys() { return subkeys; }
    public SubkeysGeneration(BitArray keyBitArray, int rounds = 16) {
        Console.WriteLine("----------------------- KEY -----------------------------");
        BitArrayOperations.PrintBitArrayInMatrixForm(keyBitArray,8,8);
        subkeys = new List<BitArray>();
        BitArray key56bit = BitArrayOperations.Permute(keyBitArray, PC1);
        Console.WriteLine("----------------------- PC - 1 --------------------------");
        BitArrayOperations.PrintBitArrayInMatrixForm(keyBitArray, 8, 7);

        BitArray C = new BitArray(28);
        BitArray D = new BitArray(28);
        for (int i = 0; i < 28; i++)
        {
            C[i] = key56bit[i];
            D[i] = key56bit[i + 28];
        }
        Console.WriteLine("---------------------------------------------------------");
        Console.Write("C0: ");
        BitArrayOperations.PrintBitArray(C);
        Console.Write("D0: ");
        BitArrayOperations.PrintBitArray(D);
        Console.WriteLine("----------------------- Left circular shift to both -----");

        for (int round = 0; round < rounds; round++)
        {
            C = BitArrayOperations.LeftShift(C, shiftsPerRound[round]);
            D = BitArrayOperations.LeftShift(D, shiftsPerRound[round]);
            BitArray combinedKey = new BitArray(56);
            for (int i = 0; i < 28; i++)
            {
                combinedKey[i] = C[i];
                combinedKey[i + 28] = D[i];
            }
            BitArray subkey = BitArrayOperations.Permute(combinedKey, PC2);
            subkeys.Add(subkey);
            Console.Write("C"+(round + 1)+": ");
            BitArrayOperations.PrintBitArray(C);
            Console.Write("D"+(round + 1)+": ");
            BitArrayOperations.PrintBitArray(D);
        Console.WriteLine("----------------------- PC - 2 --------------------------");
            BitArrayOperations.PrintBitArrayInMatrixForm(subkey, 8, 6);
        }


    }
}
