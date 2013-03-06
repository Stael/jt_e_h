package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:01
 */

/*
    Thread de création de la version compressé du text
 */
class CharEncoderThread extends NotifyingThread {
    private byte[] textToEncode;
    private BitArray encodedText;
    private CharFrequency[] charFrequency;
    private int threadNumber;

    public CharEncoderThread(byte[] textToEncode, CharFrequency[] charFrequency, int threadNumber) {
        this.textToEncode = textToEncode;
        this.encodedText = new BitArray(8192);
        this.charFrequency = charFrequency;
        this.threadNumber = threadNumber;
    }

    public void doRun() {
        encode();
    }

    /*
        Méthode de réalisation de la compression d'une sous partie
        (ou de tout le fichier dans le cas d'une compression "mono-thread")
        du fichier compressé
        Parcours de chaque octet (qui représente un caractère)
        Ajout dans le BitArray du code du caractère
     */
    public void encode() {
        int length = textToEncode.length;

        // Parcours de chaque octet
        for (int i = 0; i < length; i++) {
            // Cas des caractères non ASCII
            if ((int) textToEncode[i] >= 0)
                encodedText.add(charFrequency[(int) textToEncode[i]].getCode());
        }
        encodedText.finalise();
    }

    public BitArray getEncodedText() {
        return encodedText;
    }

    public int getThreadNumber() {
        return threadNumber;
    }
}
