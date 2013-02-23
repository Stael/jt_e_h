package CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:29
 */

import BitManagement.BitArray;
import CharUtils.CharDecoder.CharDecoder;
import CharUtils.CharEncoder.CharEncoder;
import CharUtils.CharFrequency;
import HuffmanTree.TreeBuilder;
import ThreadUtils.ThreadCompleteListener;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CharCounter implements ThreadCompleteListener {
    String s = "";
    int nbThread = Runtime.getRuntime().availableProcessors();
    long start = 0;
    HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();


    public CharCounter(String s) {
        this.s = s;
    }

    public CharCounter(String s, long start) {
        this.s = s;
        this.start = start;
    }

    public void countMono() {
        CharCounterThread cct = new CharCounterThread(s);
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
            CharCounterThread cct = new CharCounterThread(ns);
            cct.addListener(this);
            cct.start();
        }
    }

    public synchronized void notifyOfThreadComplete(final Thread thread) {
        nbThread--;

        extractResults(((CharCounterThread) thread));

        if (nbThread == 0) {
            //printNbCharInDictionnary();

            printState("fin calcul nbr occurences");

            TreeBuilder.buildTree(characterMap);

            printState("fin génération arbre");

            CharEncoder ce = new CharEncoder(s, 10, characterMap);

            BitArray be = ce.encode();

            System.out.println("Nb bits : " + be.length());


            System.out.println("\n\n");

            printState("fin encodage ");

            try {
                FileOutputStream out = new FileOutputStream("testfile");
                out.write(be.toByteArray());
            }
            catch(Exception e) {
                System.out.println("Yolo !");
            }

            printState("fin save fichier encodé ");

            CharDecoder cd = new CharDecoder(be.toByteArray(), characterMap);
            String res = cd.decode();

            printState("fin décodage ");

            try {
                FileOutputStream out = new FileOutputStream("final");
                out.write(res.getBytes());
            }
            catch(Exception e) {
                System.out.println("Yolo !");
            }

            printState("fin save fichier décodé ");

            printState("Fin ");
        }

    }

    private void printState(String status) {
        System.out.println("Statut : " + status);
        System.out.println("Exec Time - Advanced : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " Mo");
        System.out.println();
        start = System.currentTimeMillis();
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
