package CharUtils.CharEncoder;


import BitManagement.BitArray;
import CharUtils.CharCounter.CharCounterThread;
import CharUtils.CharFrequency;
import CharUtils.Encoder;
import ThreadUtils.ThreadCompleteListener;

import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 21:48
 */
public class CharEncoder implements ThreadCompleteListener {
    private int nbThread = Runtime.getRuntime().availableProcessors();
    private int remainingThreads = Runtime.getRuntime().availableProcessors();
    private String textToEncode;
    private HashMap<Character, CharFrequency> characterMap;
    private long start = 0;
    private BitArray encodedText;
    private BitArray[] partialEncodedTexts = new BitArray[nbThread];
    private Encoder encoder;


    public CharEncoder(String s, int size, HashMap<Character, CharFrequency> characterMap, Encoder encoder) {
        this.textToEncode = s;
        this.characterMap = characterMap;
        this.encoder = encoder;
    }

    public void encodeMono() {
        CharEncoderThread cet = new CharEncoderThread(textToEncode, characterMap, 0);
        cet.encode();

        encoder.postEncoding(cet.getEncodedText());
    }

    public void encodeMulti() {
        if (start == 0) start = System.currentTimeMillis();

        int stringLength = (int) Math.ceil(textToEncode.length() / nbThread);

        for (int i = 0; i < nbThread; i++) {
            String ns = textToEncode.substring(i * stringLength, i + 1 == nbThread ? textToEncode.length() : i * stringLength + stringLength);
            CharEncoderThread cet = new CharEncoderThread(ns, characterMap, i);
            cet.addListener(this);
            cet.start();
        }
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        partialEncodedTexts[((CharEncoderThread) thread).getThreadNumber()] = ((CharEncoderThread) thread).getEncodedText();

        remainingThreads--;

        if (remainingThreads == 0) {
            int length = 0;
            for(int i = 0; i < nbThread; i++) {
                length += partialEncodedTexts[i].length();
            }
            encodedText = new BitArray(length);
            for(int i = 0; i < nbThread; i++) {
                encodedText.add(partialEncodedTexts[i]);
            }
            encoder.postEncoding(encodedText);
        }
    }
}
