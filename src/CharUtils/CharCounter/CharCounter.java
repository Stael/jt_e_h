package CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import CharUtils.CharFrequency;
import CharUtils.Encoder;
import ThreadUtils.ThreadCompleteListener;

import java.util.HashMap;
import java.util.Map;

public class CharCounter implements ThreadCompleteListener {
    private String textWhereToCountNumberOfChar;
    private int nbThread = Runtime.getRuntime().availableProcessors();
    private int remainingThreads = Runtime.getRuntime().availableProcessors();
    private long start = 0;
    private HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();
    private Encoder encoder;


    public CharCounter(String textWhereToCountNumberOfChar) {
        this.textWhereToCountNumberOfChar = textWhereToCountNumberOfChar;
    }

    public CharCounter(String textWhereToCountNumberOfChar, long start, Encoder encoder) {
        this.textWhereToCountNumberOfChar = textWhereToCountNumberOfChar;
        this.start = start;
        this.encoder = encoder;
    }

    public void countMono() {
        nbThread = 1;
        remainingThreads = 1;
        countMulti();
    }

    public void countMulti() {
        if (start == 0) start = System.currentTimeMillis();
        int stringLength = (int) Math.ceil(textWhereToCountNumberOfChar.length() / nbThread);

        for (int i = 0; i < nbThread; i++) {
            // Problème de la fin du dernier
            String ns = textWhereToCountNumberOfChar.substring(i * stringLength, i + 1 == nbThread ? textWhereToCountNumberOfChar.length() : i * stringLength + stringLength);
            CharCounterThread cct = new CharCounterThread(ns);
            cct.addListener(this);
            cct.start();
        }
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        remainingThreads--;

        extractResults(((CharCounterThread) thread));

        if (remainingThreads == 0) {
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
