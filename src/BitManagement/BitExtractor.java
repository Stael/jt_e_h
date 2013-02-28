package BitManagement;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:46
 */
public class BitExtractor extends BitArray {
    private int currentBit;
    private int currentByte;

    public BitExtractor(byte[] bitArray, int lastByte, int lastBit) {
        super(bitArray, lastByte, lastBit);
        this.currentBit = 0;
        this.currentByte = 0;
    }

    public int next() {
        int i = this.isBitSet(currentByte, 7-currentBit) ? 1 : 0;

        currentByte = currentBit == 7 ? currentByte + 1 : currentByte;
        currentBit =  currentBit == 7 ? 0 : currentBit + 1;

        return i;
    }

    public boolean hasNext() {
        return currentByte < lastByte || (currentByte == lastByte && currentBit < lastBit);
    }

    public void printState() {
        System.out.println("LastByte : " + lastByte + " -- LastBit : " + lastBit);
        System.out.println("CurrentByte : " + currentByte + " -- CurrentBit : " + currentBit);
    }
}
