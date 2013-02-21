/**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import java.util.HashMap;
import java.util.Map;

public class CharacterCounter implements ThreadCompleteListener {
    String s = "";
    int nbThread = Runtime.getRuntime().availableProcessors();
    long start = 0;
    HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();


    public CharacterCounter(String s) {
        this.s = s;
    }

    public CharacterCounter(String s, long start) {
        this.s = s;
        this.start = start;
    }

    public void countMono() {
        CharacterCounterThread cct = new CharacterCounterThread(s);
        cct.count();

        extractResults(cct);

        printNbCharInDictionnary();

        System.out.println("Final time : " + (System.currentTimeMillis() - start) + " ms");
    }

    public void countMulti() {
        if (start == 0) start = System.currentTimeMillis();
        String ns;
        int stringLength = (int) Math.ceil(s.length() / nbThread);
        int threadsToLaunch = nbThread;

        for (int i = 0; i < threadsToLaunch; i++) {
            // Problème de la fin du dernier
            ns = s.substring(i * stringLength, i + 1 == threadsToLaunch ? s.length() : i * stringLength + stringLength);
            CharacterCounterThread cct = new CharacterCounterThread(ns);
            cct.addListener(this);
            cct.start();
        }
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        nbThread--;

        extractResults(((CharacterCounterThread) thread));

        if (nbThread == 0) {
            //printNbCharInDictionnary();

            System.out.println("Exec Time - Advanced : " + (System.currentTimeMillis() - start) + " ms");
            System.out.println("Ram used : " + Runtime.getRuntime().totalMemory() / 1000000 + " M");
        }

    }

    private void printNbCharInDictionnary() {
        int res = 0;
        for (Map.Entry<Character, Integer> e : characterMap.entrySet()) {
            res += e.getValue();
        }
        System.out.println("Nb chars : " + res);
    }

    private void extractResults(CharacterCounterThread cct) {
        HashMap<Character, CharacterCounterThread.CharFrequency> resultHm = cct.getCharacterMap();
        for (Map.Entry<Character, CharacterCounterThread.CharFrequency> e : resultHm.entrySet()) {
            if (characterMap.containsKey(e.getKey())) {
                characterMap.put(e.getKey(), characterMap.get(e.getKey()) + e.getValue().getNb());
            } else {
                characterMap.put(e.getKey(), e.getValue().getNb());
            }
        }
    }
}
