package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:42
 */
class CharDecoder implements ThreadCompleteListener {
    private BitExtractor[] arrayOfBitExtractor;
    private HuffmanTree tree;
    private BitArray[] decodedText;

    private int nbThread;

    public CharDecoder(BitExtractor[] arrayOfBitExtractor, HuffmanTree tree) {
        this.arrayOfBitExtractor = arrayOfBitExtractor;
        this.tree = tree;
        nbThread = arrayOfBitExtractor.length;
        decodedText = new BitArray[nbThread];
    }

    /*
        Décompression multi-threadé
     */
    public BitArray[] decodeMulti() {
        // Création et lancement des threads
        CharDecoderThread[] threads = new CharDecoderThread[nbThread];

        for (int i = 0; i < nbThread; i++) {
            // Création du thread
            threads[i] = new CharDecoderThread(arrayOfBitExtractor[i], tree, i);
            // On ajoute cette classe comme listener
            threads[i].addListener(this);
            // On lance le thread
            threads[i].start();
        }
        arrayOfBitExtractor = null;

        // Attente de la fin de l'execution de chaque thread
        try {
            for (int i = 0; i < nbThread; i++) {
                threads[i].join();
            }
        }
        catch (Exception e) {
            System.out.println("Une erreur est survenue");
            System.exit(-1);
        }

        return decodedText;
    }

    public void notifyOfThreadComplete(final Thread thread) {
        decodedText[((CharDecoderThread) thread).getThreadNumber()] = ((CharDecoderThread) thread).getDecodedText();
    }
}
