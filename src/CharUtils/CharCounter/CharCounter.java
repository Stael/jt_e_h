package CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import BitManagement.BitArray;
import CharUtils.CharDecoder.CharDecoder;
import CharUtils.CharEncoder.CharEncoder;
import CharUtils.CharFrequency;
import CharUtils.Encoder;
import HuffmanTree.TreeBuilder;
import ThreadUtils.ThreadCompleteListener;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CharCounter implements ThreadCompleteListener {
    private String s = "";
    private int nbThread = Runtime.getRuntime().availableProcessors();
    private long start = 0;
    private HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();
    private Encoder encoder;


    public CharCounter(String s) {
        this.s = s;
    }

    public CharCounter(String s, long start, Encoder encoder) {
        this.s = s;
        this.start = start;
        this.encoder = encoder;
    }

    public void countMono() {
        CharCounterThread cct = new CharCounterThread(s);
        cct.count();

        extractResults(cct);

        encoder.postCountNumberOfCharacters(characterMap);
    }

    public void countMulti() {
        if (start == 0) start = System.currentTimeMillis();
        String ns;
        int stringLength = (int) Math.ceil(s.length() / nbThread);
        int threadsToLaunch = nbThread;

        for (int i = 0; i < threadsToLaunch; i++) {
            // Problème de la fin du dernier
            ns = s.substring(i * stringLength, i + 1 == threadsToLaunch ? s.length() : i * stringLength + stringLength);
            CharCounterThread cct = new CharCounterThread(ns);
            cct.addListener(this);
            cct.start();
        }
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        nbThread--;

        extractResults(((CharCounterThread) thread));

        if (nbThread == 0) {
            encoder.postCountNumberOfCharacters(characterMap);
        }

    }

    private void printNbCharInDictionnary() {
        int res = 0;
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            res += e.getValue().getNb();
        }
        System.out.println("Nb chars : " + res);
    }

    private void extractResults(CharCounterThread cct) {
        HashMap<Character, CharFrequency> resultHm = cct.getCharacterMap();
        for (Map.Entry<Character, CharFrequency> e : resultHm.entrySet()) {

            CharFrequency cf = characterMap.get(e.getKey());

            if (cf != null) {
                cf.add(e.getValue().getNb());
            } else {
                characterMap.put(e.getKey(), e.getValue());
            }
        }
    }
}
