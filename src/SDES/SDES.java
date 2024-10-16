package SDES;

import java.util.*;

public class SDES {
    static boolean debug = false;
    public int[] GlobalK1;
    public int[] GlobalK2;
    static List<int[]> keys;
    static StringBuilder decryptedTextBuilder;
    boolean sameKey = false;
    boolean predefinedKey = false;
    int[] predefinedKeyValue;

    public SDES(){
        keys = new ArrayList<>();
        decryptedTextBuilder = new StringBuilder();
    }

    public StringBuilder getDecryptedText(){
        return decryptedTextBuilder;
    }

    public void setSameKeyTrue(){
        sameKey = true;
    }

    public void setPredefinedKeyTrue(){
        predefinedKey = true;
    }

    public void setPredefinedKeyValue(int [] predefinedKeyValue){
        this.predefinedKeyValue = predefinedKeyValue;
    }

    public void init(String plainText) {
        StringBuilder plainTextBuilder = new StringBuilder(plainText);
        StringBuilder cipherTextBuilder = encryptEachBlock(plainTextBuilder);
        decryptEachBlock(cipherTextBuilder);
        System.out.println("Plaintext: "+plainTextBuilder);
        System.out.println("Ciphertext: "+cipherTextBuilder);
        System.out.println("Keys: ");
        for (int[] key : keys) {
            System.out.println(Arrays.toString(key));
        }
        System.out.println("Plaintext using decryption: "+decryptedTextBuilder);
    }

    public void init() {
        System.out.println("Put the plainText to be encrypted: ");
        Scanner scanner = new Scanner(System.in);
        String plainText = scanner.nextLine();
        StringBuilder plainTextBuilder = new StringBuilder(plainText);
        StringBuilder cipherTextBuilder = encryptEachBlock(plainTextBuilder);
        decryptEachBlock(cipherTextBuilder);
        System.out.println("Plaintext: "+plainTextBuilder);
        System.out.println("Ciphertext: "+cipherTextBuilder);
        System.out.println("Keys: ");
        for (int[] key : keys) {
            System.out.println(Arrays.toString(key));
        }
        System.out.println("Plaintext using decryption: "+decryptedTextBuilder);
    }

    public StringBuilder encryptEachBlock(StringBuilder plainTextBuilder){
        if(sameKey) {
            if(predefinedKey) {
                keys.add(predefinedKeyValue);
                subkeysGeneration(predefinedKeyValue);
            }else{
                int[] key = SubkeysGenerator.generateKey();
                keys.add(key);
                subkeysGeneration(key);
            }
        }
        StringBuilder cipherTextBuilder = new StringBuilder();
        for(int i = 0; i < plainTextBuilder.length(); i++){
            if(!sameKey) {
                int[] key = SubkeysGenerator.generateKey();
                keys.add(key);
                subkeysGeneration(key);
            }
            char character = plainTextBuilder.charAt(i);
            int[] currentBlock = Util.charToEightBitBlock(character);
            int[] encryptedEightBitBlock = encryption(currentBlock);
            char cipherChar = Util.eightBitBlockToChar(encryptedEightBitBlock);
            cipherTextBuilder.append(cipherChar);
        }
        return cipherTextBuilder;
    }

    public void decryptEachBlock(StringBuilder cipherTextBuilder){
        for(int i = 0; i < cipherTextBuilder.length(); i++){
            char character = cipherTextBuilder.charAt(i);
            int[] currentBlock = Util.charToEightBitBlock(character);
            int[] decryptedEightBitBlock = decryption(currentBlock,i);
            char decryptedChar = Util.eightBitBlockToChar(decryptedEightBitBlock);
            decryptedTextBuilder.append(decryptedChar);
        }
    }

    public StringBuilder justDecryptEachBlock(StringBuilder cipherTextBuilder, int[] key){
        StringBuilder justDecryptedTextBuilder = new StringBuilder();
        for(int i = 0; i < cipherTextBuilder.length(); i++){
            char character = cipherTextBuilder.charAt(i);
            int[] currentBlock = Util.charToEightBitBlock(character);
            int[] decryptedEightBitBlock = justDecryption(currentBlock,key);
            char decryptedChar = Util.eightBitBlockToChar(decryptedEightBitBlock);
            justDecryptedTextBuilder.append(decryptedChar);
        }
        //System.out.println(justDecryptedTextBuilder);
        return justDecryptedTextBuilder;
    }

    public void subkeysGeneration(int[] key){
        if(debug) System.out.println("------------------------ First, apply P10 to the key and circular left shift to L and R" +
                " of the result ------------------------");

        int[] P10 = SubkeysGenerator.permutation10(key);

        int[] L = Arrays.copyOfRange(P10, 0, key.length / 2);
        if(debug) Util.printBits("L: ", L);
        int[] circularLeftShiftofL = SubkeysGenerator.circularLeftShift(L);
        if(debug) Util.printBits("Circular left shift of L: ", circularLeftShiftofL);

        int[] R = Arrays.copyOfRange(P10, key.length / 2, key.length);
        if(debug) Util.printBits("R: ", R);
        int[] circularLeftShiftofR = SubkeysGenerator.circularLeftShift(R);
        if(debug) Util.printBits("Circular left shift of R: ", circularLeftShiftofR);

        int[] previousP8K1 = Util.mergeArrays(circularLeftShiftofL, circularLeftShiftofR);
        if(debug) Util.printBits("Circular left shift of R + Circular left shift of L: ",
                previousP8K1);

        if(debug) System.out.println("------------------------ Then apply P8 to obtain subkey K1" +
                " ------------------------");

        int[] K1 = SubkeysGenerator.permutation8(previousP8K1);
        if(debug) Util.printBits("K1: ", K1);

        if(debug) System.out.println("------------------------ After that, we do a double circular left shif to" +
                " Circular left shift of R and Circular left shift of L ------------------------");

        int[] doubleCircularLeftShiftofL = SubkeysGenerator.circularLeftShift(
                SubkeysGenerator.circularLeftShift(circularLeftShiftofL)
        );
        if(debug) Util.printBits("Double circular left shift of L: ", doubleCircularLeftShiftofL);

        int[] doubleCircularLeftShiftofR = SubkeysGenerator.circularLeftShift(
                SubkeysGenerator.circularLeftShift(circularLeftShiftofR)
        );
        if(debug) Util.printBits("Double circular left shift of R: ", doubleCircularLeftShiftofR);

        int[] previousP8K2 = Util.mergeArrays(doubleCircularLeftShiftofL, doubleCircularLeftShiftofR);
        if(debug) Util.printBits("Double circular left shift of R + Double circular left shift of L: ",
                previousP8K2);

        if(debug) System.out.println("------------------------ Finally, we apply P8 to obtain subkey K2" +
                " ------------------------");

        int[] K2 = SubkeysGenerator.permutation8(previousP8K2);
        if(debug) Util.printBits("K2: ", K2);

        if(debug) System.out.println("---------------- Keys --------------------");
        if(debug) Util.printBits("K1: ", K1);
        if(debug) Util.printBits("K2: ", K2);
        GlobalK1 = K1;
        GlobalK2 = K2;

    }

    public int[] encryption(int [] currentBlock){
        System.out.println("------------------------ ENCRYPTION PROCESS" +
                " ------------------------");

        System.out.println("------------------------ Apply Initial Permutation" +
                " ------------------------");
        int[] permutedBlock = Methods.initialPermutation(currentBlock);

        int[] L = Arrays.copyOfRange(permutedBlock, 0, permutedBlock.length/2);
        Util.printBits("L: ", L);
        int[] R = Arrays.copyOfRange(permutedBlock, permutedBlock.length/2,
                permutedBlock.length);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K1" +
                " ---------------------------------");
        int[] resultFk = Methods.f_k(L,R,GlobalK1);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Swap L and R" +
                " ---------------------------------");
        L = Arrays.copyOfRange(resultFk, resultFk.length / 2, resultFk.length);
        Util.printBits("L: ", L);
        R = Arrays.copyOfRange(resultFk, 0, resultFk.length / 2);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K2" +
                " ---------------------------------");
        resultFk = Methods.f_k(L,R,GlobalK2);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Finally apply Inversed IP" +
                " ---------------------------------");
        int[] encrypted8BitBlock = Methods.inversedIP(resultFk);
        Util.printBits("Inversed IP applied: ", encrypted8BitBlock);
        Util.printBits("Encrypted 8-bit block: ", encrypted8BitBlock);

        return encrypted8BitBlock;
    }

    public int[] justDecryption(int[] currentBlock, int[] key){
        subkeysGeneration(key);
        int[] permutedBlock = Methods.initialPermutation(currentBlock);
        int[] L = Arrays.copyOfRange(permutedBlock, 0, permutedBlock.length/2);
        int[] R = Arrays.copyOfRange(permutedBlock, permutedBlock.length/2,
                permutedBlock.length);
        int [] resultFk = Methods.f_k(L,R,GlobalK2);
        L = Arrays.copyOfRange(resultFk, resultFk.length / 2, resultFk.length);
        R = Arrays.copyOfRange(resultFk, 0, resultFk.length / 2);
        resultFk = Methods.f_k(L,R,GlobalK1);

        return Methods.inversedIP(resultFk);
    }

    public int[] decryption(int[] currentBlock, int blockIndex){
        System.out.println("------------------------ DECRYPTION PROCESS" +
                " ------------------------");

        System.out.println("------------------------ Obtain K1 and K2" +
                " ------------------------");
        if(keys.size() > 1) subkeysGeneration(keys.get(blockIndex));
        else subkeysGeneration(keys.getFirst());

        System.out.println("------------------------ Apply Initial Permutation" +
                " ------------------------");
        int[] permutedBlock = Methods.initialPermutation(currentBlock);

        int[] L = Arrays.copyOfRange(permutedBlock, 0, permutedBlock.length/2);
        Util.printBits("L: ", L);
        int[] R = Arrays.copyOfRange(permutedBlock, permutedBlock.length/2,
                permutedBlock.length);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K2" +
                " ---------------------------------");
        int [] resultFk = Methods.f_k(L,R,GlobalK2);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Swap L and R" +
                " ---------------------------------");
        L = Arrays.copyOfRange(resultFk, resultFk.length / 2, resultFk.length);
        Util.printBits("L: ", L);
        R = Arrays.copyOfRange(resultFk, 0, resultFk.length / 2);
        Util.printBits("R: ", R);

        System.out.println("------------------------ Apply f_k with SK = K1" +
                " ---------------------------------");
        resultFk = Methods.f_k(L,R,GlobalK1);
        Util.printBits("Result of fk: ", resultFk);

        System.out.println("------------------------ Finally apply Inversed IP" +
                " ---------------------------------");
        int[] decrypted8BitBlock = Methods.inversedIP(resultFk);
        Util.printBits("Inversed IP applied: ", decrypted8BitBlock);
        Util.printBits("decrypted 8-bit block: ", decrypted8BitBlock);

        return decrypted8BitBlock;
    }

}