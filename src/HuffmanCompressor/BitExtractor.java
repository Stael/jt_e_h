package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:46
 */

/*
    Classe d'extraction des bits d'un BitArray
 */
class BitExtractor extends BitArray {
    private int currentBit;
    private int currentByte;

    public BitExtractor(byte[] bitArray, int lastByte, int lastBit, int currentByte) {
        super(bitArray, lastByte, lastBit);
        this.currentBit = 0;
        this.currentByte = currentByte;
    }

    /*
        Extraction du prochain bit à lire
     */
    public boolean next() {
        boolean i = this.isBitSet(currentByte, 7 - currentBit);

        currentByte = currentBit == 7 ? currentByte + 1 : currentByte;
        currentBit = currentBit == 7 ? 0 : currentBit + 1;

        return i;
    }

    /*
        Vérification si il y a encore des bits à lire
     */
    public boolean hasNext() {
        return currentByte < lastByte || (currentByte == lastByte && currentBit < lastBit);
    }
}
