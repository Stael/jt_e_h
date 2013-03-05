package HuffmanCompressor.CharUtils.CharCounter;
/**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:44
 */

import HuffmanCompressor.CharUtils.CharFrequency;
import HuffmanCompressor.ThreadUtils.NotifyingThread;

import java.util.HashMap;

public class CharCounterThread extends NotifyingThread {

    private int threadNumber;
    private CharFrequency[] charFrequency = new CharFrequency[256];
    private byte[] textWhereToCountNumberOfChar;

    public CharCounterThread(byte[] textWhereToCountNumberOfChar, int threadNumber) {
        this.textWhereToCountNumberOfChar = textWhereToCountNumberOfChar;
        this.threadNumber = threadNumber;

        for(int i = 0; i < 256; i++) {
            charFrequency[i] = new CharFrequency((char) i);
        }
    }

    public void doRun() {
        this.count();
    }

    public void count() {
        int length = textWhereToCountNumberOfChar.length;
        for (int i = 0; i < length; i++) {
            if((int) textWhereToCountNumberOfChar[i] >= 0)
            charFrequency[(int) textWhereToCountNumberOfChar[i]].up();
        }
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public CharFrequency[] getCharFrequency() {
        return charFrequency;
    }
}
