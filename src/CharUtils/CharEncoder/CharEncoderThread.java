package CharUtils.CharEncoder;

import BitManagement.BitArray;
import CharUtils.CharFrequency;
import ThreadUtils.NotifyingThread;
import Utils.StatusPrinter;

import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:01
 */
public class CharEncoderThread extends NotifyingThread {
    private String textToEncode;
    private BitArray encodedText;
    private HashMap<Character, CharFrequency> characterMap;
    private int threadNumber;

    public CharEncoderThread(String s, HashMap<Character, CharFrequency> characterMap, int threadNumber) {
        this.textToEncode = s;
        this.encodedText = new BitArray();
        this.characterMap = characterMap;
        this.threadNumber = threadNumber;
    }

    public void doRun() {
        encode();
    }

    public void encode() {
        //long start = System.currentTimeMillis();
        for(int i = 0; i < textToEncode.length(); i++) {
            CharFrequency cf = characterMap.get(textToEncode.charAt(i));
            if(cf == null) {
                System.out.println("Yolo FAIL !");
            }
            encodedText.add(cf.getByteCode());
        }
        //StatusPrinter.printStatus("Fin encoding - thread : " + threadNumber, start);
    }

    public BitArray getEncodedText() {
        return encodedText;
    }

    public int getThreadNumber() {
        return threadNumber;
    }
}
