package HuffmanCompressor.CharUtils.CharEncoder;


import HuffmanCompressor.BitManagement.BitArray;
import HuffmanCompressor.CharUtils.CharCounter.CharCounterThread;
import HuffmanCompressor.CharUtils.CharFrequency;
import HuffmanCompressor.CharUtils.Encoder;
import HuffmanCompressor.ThreadUtils.ThreadCompleteListener;
import HuffmanCompressor.Utils.StatusPrinter;

import java.util.HashMap;
import java.util.List;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 21:48
 */
public class CharEncoder implements ThreadCompleteListener {
    private List<byte[]> textToEncode;
    private CharFrequency[] charFrequency;
    private BitArray[] partialEncodedTexts;

    private int nbThread;


    public CharEncoder(List<byte[]> textToEncode, CharFrequency[] charFrequency) {
        this.textToEncode = textToEncode;
        this.charFrequency = charFrequency;
        this.nbThread = textToEncode.size();
    }

    public BitArray[] encodeMono() {
        partialEncodedTexts = new BitArray[nbThread];

        for (int i = 0; i < nbThread; i++) {
            CharEncoderThread cet = new CharEncoderThread(textToEncode.get(i), charFrequency, i);
            cet.doRun();
            partialEncodedTexts[i] = cet.getEncodedText();
        }

        return partialEncodedTexts;
    }

    public BitArray[] encodeMulti() {
        partialEncodedTexts = new BitArray[nbThread];
        CharEncoderThread[] threads = new CharEncoderThread[nbThread];

        for (int i = 0; i < nbThread; i++) {
            threads[i] =  new CharEncoderThread(textToEncode.get(i), charFrequency, i);
            threads[i].addListener(this);
            threads[i].start();
        }

        textToEncode = null;

        try {
            for (int i = 0; i < nbThread; i++) {
                threads[i].join();
            }
        }
        catch (Exception e) {
            System.out.println("yolo");
        }

        return partialEncodedTexts;
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        partialEncodedTexts[((CharEncoderThread) thread).getThreadNumber()] = ((CharEncoderThread) thread).getEncodedText();
    }
}
