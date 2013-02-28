package CharUtils.CharDecoder;

import BitManagement.BitArray;
import BitManagement.BitExtractor;
import CharUtils.CharEncoder.CharEncoderThread;
import CharUtils.Decoder;
import HuffmanTree.HuffmanTree;
import HuffmanTree.HuffmanNode;
import ThreadUtils.ThreadCompleteListener;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:42
 */
public class CharDecoder implements ThreadCompleteListener {
    private BitExtractor[] arrayOfBitExtractor;
    private HuffmanTree tree;
    private String[] decodedText;

    private int nbThread = Runtime.getRuntime().availableProcessors();
    private int remainingThreads = Runtime.getRuntime().availableProcessors();
    private Decoder decoder;

    public CharDecoder(BitExtractor[] arrayOfBitExtractor, HuffmanTree tree, Decoder decoder) {
        this.arrayOfBitExtractor = arrayOfBitExtractor;
        this.tree = tree;
        remainingThreads = arrayOfBitExtractor.length;
        nbThread = arrayOfBitExtractor.length;
        decodedText = new String[nbThread];
        this.decoder = decoder;
    }

    public void decodeMulti() {
        for (int i = 0; i < nbThread; i++) {
            CharDecoderThread cdt = new CharDecoderThread(arrayOfBitExtractor[i], tree, i);
            cdt.addListener(this);
            cdt.start();
        }

        arrayOfBitExtractor = null;
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        decodedText[((CharDecoderThread) thread).getThreadNumber()] = ((CharDecoderThread) thread).getDecodedText();

        remainingThreads--;

        if(remainingThreads == 0) {
            decoder.postDecode(decodedText);
        }
    }
}
