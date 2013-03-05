package HuffmanCompressor.CharUtils.CharEncoder;

import HuffmanCompressor.BitManagement.BitArray;
import HuffmanCompressor.CharUtils.CharFrequency;
import HuffmanCompressor.ThreadUtils.NotifyingThread;

import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:01
 */
public class CharEncoderThread extends NotifyingThread {
    private byte[] textToEncode;
    private BitArray encodedText;
    private CharFrequency[] charFrequency;
    private int threadNumber;

    public CharEncoderThread(byte[] textToEncode, CharFrequency[] charFrequency, int threadNumber) {
        this.textToEncode = textToEncode;
        this.encodedText = new BitArray();
        this.charFrequency = charFrequency;
        this.threadNumber = threadNumber;
    }

    public void doRun() {
        encode();
    }

    public void encode() {
        int length = textToEncode.length;

        for(int i = 0; i < length; i++) {
            if((int) textToEncode[i] >= 0)
            encodedText.add(charFrequency[(int) textToEncode[i]].getByteCode());
        }
    }

    public BitArray getEncodedText() {
        return encodedText;
    }

    public int getThreadNumber() {
        return threadNumber;
    }
}
