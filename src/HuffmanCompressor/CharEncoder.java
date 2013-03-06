package HuffmanCompressor;


import java.util.List;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 21:48
 */
class CharEncoder implements ThreadCompleteListener {
    private List<byte[]> textToEncode;
    private CharFrequency[] charFrequency;
    private BitArray[] partialEncodedTexts;

    private int nbThread;


    public CharEncoder(List<byte[]> textToEncode, CharFrequency[] charFrequency) {
        this.textToEncode = textToEncode;
        this.charFrequency = charFrequency;
        this.nbThread = textToEncode.size();
    }

    /*
        Compression en restant dans le "thread principal"
     */
    public BitArray[] encodeMono() {
        partialEncodedTexts = new BitArray[nbThread];

        for (int i = 0; i < nbThread; i++) {
            CharEncoderThread cet = new CharEncoderThread(textToEncode.get(i), charFrequency, i);
            cet.doRun();
            partialEncodedTexts[i] = cet.getEncodedText();
        }

        return partialEncodedTexts;
    }

    /*
        Compression multi-threadé
     */
    public BitArray[] encodeMulti() {
        // Création et lancement des threads

        partialEncodedTexts = new BitArray[nbThread];
        CharEncoderThread[] threads = new CharEncoderThread[nbThread];

        for (int i = 0; i < nbThread; i++) {
            threads[i] = new CharEncoderThread(textToEncode.get(i), charFrequency, i);
            threads[i].addListener(this);
            threads[i].start();
        }

        // Attente de la fin de l'execution de chaque thread
        try {
            for (int i = 0; i < nbThread; i++) {
                threads[i].join();
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue");
            System.exit(-1);
        }

        return partialEncodedTexts;
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        partialEncodedTexts[((CharEncoderThread) thread).getThreadNumber()] = ((CharEncoderThread) thread).getEncodedText();
    }
}
