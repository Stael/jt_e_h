package CharUtils.CharCounter; /**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:44
 */

import CharUtils.CharFrequency;
import ThreadUtils.NotifyingThread;

import java.util.HashMap;

public class CharCounterThread extends NotifyingThread {

    private String s = "";
    //private HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
    private HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();

    public CharCounterThread(String s) {
        this.s = s;
    }

    public void doRun() {
        this.count();
    }

    public void count() {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);

            CharFrequency t = characterMap.get(c);
            if (t != null) {
                t.up();
            } else {
                t = new CharFrequency(c);
                characterMap.put(c, t);
            }
        }
    }

    public HashMap<Character, CharFrequency> getCharacterMap() {
        return characterMap;
    }
}
