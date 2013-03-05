package HuffmanCompressor.CharUtils.CharDecoder;

import HuffmanCompressor.BitManagement.BitExtractor;
import HuffmanCompressor.HuffmanTree.HuffmanTree;
import HuffmanCompressor.HuffmanTree.HuffmanNode;
import HuffmanCompressor.ThreadUtils.NotifyingThread;
import HuffmanCompressor.Utils.StatusPrinter;

/**
 * User: thibaultramires
 * Date: 28/02/13
 * Time: 17:28
 */
public class CharDecoderThread extends NotifyingThread {
    private BitExtractor bitExtractor;
    private HuffmanTree tree;
    private String decodedText;
    private int threadNumber;

    public CharDecoderThread(BitExtractor bitExtractor, HuffmanTree tree, int threadNumber) {
        this.bitExtractor = bitExtractor;
        this.tree = tree;
        this.threadNumber = threadNumber;
    }

    public void doRun() {
        decode();
    }

    public void decode() {
        long start = System.currentTimeMillis();

        StringBuffer res = new StringBuffer();
        HuffmanNode parc = tree.getRoot();
        while(bitExtractor.hasNext()) {

            if(bitExtractor.next() == 0) {
                parc = parc.getLeft();
            }
            else {
                parc = parc.getRight();
            }

            if(parc.isLeaf()) {
                res.append(parc.getCf().getCharacter());
                parc = tree.getRoot();
            }
        }
        decodedText = res.toString();

        StatusPrinter.printStatus("Fin thread : " + threadNumber, start);
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public String getDecodedText() {
        return decodedText;
    }
}
