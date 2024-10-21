using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
public class BitArrayOperations
{
    public static BitArray Permute(BitArray input, int[] permTable)
    {
        BitArray output = new BitArray(permTable.Length);
        for (int i = 0; i < permTable.Length; i++)
        {
            if (permTable[i] - 1 < input.Length)
            {
                output[i] = input[permTable[i] - 1];
            }
        }
        return output;
    }
    public static BitArray LeftShift(BitArray input, int shifts)
    {
        BitArray output = new BitArray(input.Length);
        for (int i = 0; i < input.Length; i++)
        {
            output[i] = input[(i + shifts) % input.Length];
        }
        return output;
    }
    public static void PrintBitArray(BitArray bitArray)
    {
        for (int i = 0; i < bitArray.Length; i++)
        {
            Console.Write(bitArray[i] ? "1" : "0");
            if ((i + 1) % 4 == 0) Console.Write(" ");
        }
        Console.WriteLine();
    }
    public static void PrintBitArrayInMatrixForm(BitArray bitArray, int rows, int columns)
    {
        if (bitArray.Length < rows * columns)
        {
            throw new ArgumentException("El BitArray no tiene suficientes elementos para llenar la matriz.");
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                int index = i * columns + j;
                Console.Write(bitArray[index] ? "1" : "0");
                if (j < columns - 1)
                {
                    Console.Write(" ");
                }
            }
            Console.WriteLine();
        }
    }

    public static BitArray HexStringToBitArray(string hex)
    {
        BitArray bitArray = new BitArray(hex.Length * 4);
        for (int i = 0; i < hex.Length; i++)
        {
            int value = Convert.ToInt32(hex[i].ToString(), 16);
            for (int j = 0; j < 4; j++)
            {
                bitArray[(i * 4) + (3 - j)] = (value & (1 << j)) != 0;
            }
        }

        return bitArray;
    }

    public static BitArray XorBitArrays(BitArray array1, BitArray array2)
    {
        if (array1.Length != array2.Length)
        {
            throw new ArgumentException("Los BitArrays deben tener la misma longitud para realizar XOR.");
        }

        BitArray result = new BitArray(array1);
        result.Xor(array2);

        return result;
    }

    public static string BitArrayToHexString(BitArray bitArray)
    {
        if (bitArray.Length % 4 != 0)
        {
            throw new ArgumentException("El número de bits debe ser múltiplo de 4 para convertir a hexadecimal.");
        }

        string hexString = string.Empty;

        for (int i = 0; i < bitArray.Length; i += 4)
        {
            int value = 0;

            for (int j = 0; j < 4; j++)
            {
                if (bitArray[i + j])
                {
                    value |= (1 << (3 - j));
                }
            }

            hexString += value.ToString("X");
        }

        return hexString;
    }
}

