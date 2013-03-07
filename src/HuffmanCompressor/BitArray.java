package HuffmanCompressor;

import java.util.Arrays;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 12:39
 */

/*
    Classe encapsulant la gestion des tableaux de byte
 */
class BitArray {
    protected byte[] bitArray;
    protected int lastByte = 0;
    protected int lastBit = 0;
    protected int bitArrayLength;

    public BitArray() {
        this(100);
    }

    public BitArray(int size) {
        bitArray = new byte[size];
        bitArrayLength = bitArray.length;
    }

    public BitArray(byte[] bitArray, int lastByte, int lastBit) {
        this.bitArray = bitArray;
        this.lastByte = lastByte;
        this.lastBit = lastBit;
        bitArrayLength = lastByte + 1;
    }

    /*
        Extraction de la partie utile du tableau de byte
     */
    public byte[] toByteArray() {
        return Arrays.copyOfRange(bitArray, 0, lastByte + 1);
    }

    /*
        Ajout d'un char
     */
    public void add(char c) {
        bitArray[lastByte] = (byte) c;
        lastByte++;

        if (lastByte == bitArrayLength) {
            extendCapacity(2);
        }
    }

    /*
        Ajout d'un code de Huffman représenté par un tableau de booléens
     */
    public void add(boolean[] bits) {
        for (int i = 0; i < bits.length; i++) {
            add(bits[i]);
        }
    }

    /*
        Ajout d'un bit
     */
    public void add(boolean bit) {

        // On décale nos bits et on ajout le nouveau bit
        bitArray[lastByte] = (byte) ((bitArray[lastByte] << 1) | (bit ? 1 : 0));

        // En cas de fin de byte, on passe au byte suivant
        if (lastBit == 7) {

            lastByte++;
            lastBit = 0;

            // Si on a atteint la dernier byte, on double la capacité
            if (lastByte == bitArrayLength) {
                extendCapacity(2);
            }
        } else {
            lastBit++;
        }

    }

    /*
        On positionne correctement les bits du dernier byte
     */
    public void finalise() {
        bitArray[lastByte] = (byte) (bitArray[lastByte] << (8 - lastBit));
    }

    /*
        Augmentation de la capacité du tableau de byte
     */
    private void extendCapacity(int extendCoef) {
        byte[] extendedBitArray = new byte[extendCoef * bitArrayLength];

        System.arraycopy(bitArray, 0, extendedBitArray, 0, lastByte);

        bitArray = extendedBitArray;
        bitArrayLength = extendCoef * bitArrayLength;
    }

    /*
        Test si un bit est à 1 ou à 0
     */
    protected boolean isBitSet(int byteIndex, int bitIndex) {
        return (bitArray[byteIndex] & (1 << bitIndex)) != 0;
    }

    public BitArray clone() {
        return new BitArray(bitArray, lastByte, lastBit);
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i <= lastByte; i++) {
            for (int j = 7; j >= 0; j--) {
                res.append(isBitSet(i, j) ? 1 : 0);
            }
            res.append('.');
        }
        return res.toString();
    }

    public int getLastByte() {
        return lastByte;
    }

    public int getLastBit() {
        return lastBit;
    }
}
