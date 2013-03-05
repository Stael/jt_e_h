package HuffmanCompressor.CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import HuffmanCompressor.CharUtils.CharFrequency;
import HuffmanCompressor.ThreadUtils.ThreadCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class CharCounter implements ThreadCompleteListener {
    private List<byte[]> textWhereToCountNumberOfChar;
    private List<CharFrequency[]> partialCharFrequency;

    private int nbThread;

    public CharCounter(List<byte[]> textWhereToCountNumberOfChar) {
        this.textWhereToCountNumberOfChar = textWhereToCountNumberOfChar;
        this.nbThread = textWhereToCountNumberOfChar.size();
        this.partialCharFrequency = new ArrayList<CharFrequency[]>(nbThread);
    }

    public CharFrequency[] countMono() {
        CharCounterThread cct = new CharCounterThread(textWhereToCountNumberOfChar.get(0), 0);
        cct.doRun();

        return cct.getCharFrequency();
    }

    public CharFrequency[] countMulti() {
        CharCounterThread[] threads = new CharCounterThread[nbThread];
        for (int i = 0; i < nbThread; i++) {
            threads[i] = new CharCounterThread(textWhereToCountNumberOfChar.get(i), i);
            threads[i].addListener(this);
            threads[i].start();
        }

        try {
            for (int i = 0; i < nbThread; i++) {
                threads[i].join();
            }
        }
        catch (Exception e) {
            System.out.println("yolo");
        }

        CharFrequency[] charFrequency = partialCharFrequency.get(0);
        for(int i = 1; i < partialCharFrequency.size(); i++) {
            for(int j = 0; j < 256; j++) {
                charFrequency[j].add(partialCharFrequency.get(i)[j].getNbOccurence());
            }
        }

        return charFrequency;
    }

    public void notifyOfThreadComplete(final Thread thread) {
        partialCharFrequency.add(((CharCounterThread) thread).getCharFrequency());
    }
}
