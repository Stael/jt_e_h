package CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import CharUtils.CharFrequency;
import HuffmanTree.TreeBuilder;
import ThreadUtils.ThreadCompleteListener;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class CharacterCounter implements ThreadCompleteListener {
    String s = "";
    int nbThread = Runtime.getRuntime().availableProcessors();
    long start = 0;
    HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();


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
            System.out.println("Used Memory: "+ (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024) + " Mo");

            TreeSet<CharFrequency> t = TreeBuilder.buildTree(characterMap);
        }

    }

    private void printNbCharInDictionnary() {
        int res = 0;
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            res += e.getValue().getNb();
        }
        System.out.println("Nb chars : " + res);
    }

    private void extractResults(CharacterCounterThread cct) {
        HashMap<Character, CharFrequency> resultHm = cct.getCharacterMap();
        for (Map.Entry<Character, CharFrequency> e : resultHm.entrySet()) {

            CharFrequency cf = characterMap.get(e.getKey());

            if(cf != null) {
                cf.add(e.getValue().getNb());
            }
            else {
                characterMap.put(e.getKey(), e.getValue());
            }
        }
    }
}
