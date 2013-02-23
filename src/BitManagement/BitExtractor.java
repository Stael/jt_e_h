package BitManagement;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:46
 */
public class BitExtractor extends BitArray {
    private int currentBit;
    private int currentByte;
    private StringBuffer string;

    private int state = 0;

    public BitExtractor(byte[] ba) {
        super();
        lastByte = ba.length-1;
        lastBit = 8;
        this.bitArray = ba;
        currentBit = 0;
        currentByte = 0;
        string = new StringBuffer();
    }

    public void newString() {
        string.setLength(0);
    }

    public String next() {
        if(!hasNext()) {
            System.out.println("lololol");
        }

        string.append(this.isBitSet(currentByte, 7-currentBit) ? 1 : 0);

        currentByte = currentBit == 7 ? currentByte + 1 : currentByte;
        currentBit =  currentBit == 7 ? 0 : currentBit + 1;

        return string.toString();
    }

    public boolean hasNext() {
        return currentByte < lastByte || (currentByte == lastByte && currentBit <= lastBit);
    }

    public void printState() {
        System.out.println("LastByte : " + lastByte + " -- LastBit : " + lastBit);
        System.out.println("CurrentByte : " + currentByte + " -- CurrentBit : " + currentBit);
    }
}
