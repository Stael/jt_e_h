package BitManagement;

import java.util.Arrays;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 12:39
 */
public class BitArray {
    byte[]	bitArray;
    int		lastByte = 0;
    int		lastBit = 0;

    public BitArray() {
        this(100);
    }

    public BitArray(int size) {
        bitArray = new byte[size];
        for (int i = 0; i < bitArray.length; i++) {
            bitArray[i] = 0;
        }
    }

    public BitArray(byte[] bitArray, int lastByte, int lastBit) {
        this.bitArray = bitArray;
        this.lastByte = lastByte;
        this.lastBit = lastBit;
    }

    public byte[] toByteArray() {
        return Arrays.copyOfRange(bitArray, 0, lastByte+1);
    }

    public void add(String bits) {
        for(int i = 0; i < bits.length(); i++) {
            add(bits.charAt(i) == '1');
        }
    }

    public void add(boolean bit) {
        if (bit) {
            bitArray[lastByte] = (byte) (bitArray[lastByte] | 1 << (7 - lastBit));
        }

        lastByte = lastBit == 7 ? lastByte + 1 : lastByte;
        lastBit =  lastBit == 7 ? 0 : lastBit + 1;

        if (lastByte == bitArray.length) {
            extendCapacity(2);
        }
    }

    private void extendCapacity(int extendCoef) {
        byte[] extendedBitArray = new byte[extendCoef * bitArray.length];

        for (int i = 0; i < bitArray.length; i++) {
            extendedBitArray[i] = bitArray[i];
        }

        bitArray = extendedBitArray;
    }

    protected boolean isBitSet(int byteIndex, int bitIndex)
    {
        return (bitArray[byteIndex] & (1 << bitIndex)) != 0;
    }

    public int length() {
        return lastByte * 8 + lastBit ;
    }

    private void finalise() {
        while(lastBit != 7) {
            add(false);
        }
    }

    public BitArray clone() {
        return new BitArray(bitArray, lastByte, lastBit);
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i <= lastByte; i++) {
            for(int j = 7; j >= 0; j--) {
                res.append(isBitSet(i, j) ? 1 : 0);
            }
            res.append('.');
        }
        return res.toString();
    }
}
