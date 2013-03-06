package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 28/02/13
 * Time: 17:28
 */

/*
    Thread de création de la version décompressé du fichier
 */
class CharDecoderThread extends NotifyingThread {
    private BitExtractor bitExtractor;
    private HuffmanTree tree;
    private BitArray decodedText;
    private int threadNumber;

    public CharDecoderThread(BitExtractor bitExtractor, HuffmanTree tree, int threadNumber) {
        this.bitExtractor = bitExtractor;
        this.tree = tree;
        this.threadNumber = threadNumber;
        decodedText = new BitArray(bitExtractor.lastByte);
    }

    public void doRun() {
        decode();
    }

    /*
        Décompression d'une sous partie du fichier compressé
     */
    public void decode() {
        HuffmanNode parc = tree.getRoot();
        while (bitExtractor.hasNext()) {

            // On se déplace dans l'arbre en fonction de la valeur de bit extrait
            if (bitExtractor.next()) {
                parc = parc.getLeft();
            } else {
                parc = parc.getRight();
            }

            // Quand on est arrivé à la racine, on ajoute le caractère dans un BitArray
            // et on recommence
            if (parc.isLeaf()) {
                decodedText.add(parc.getCf().getCharacter());
                parc = tree.getRoot();
            }
        }
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public BitArray getDecodedText() {
        return decodedText;
    }
}
