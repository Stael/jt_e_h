/**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:44
 */

import java.util.HashMap;

public class CharacterCounterThread extends NotifyingThread {

    private String s = "";
    //private HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
    private HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();

    public class CharFrequency {
        private int nb = 1;

        public void up() {
            nb++;
        }

        public int getNb() {
            return nb;
        }
    }

    public CharacterCounterThread(String s) {
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
                t = new CharFrequency();
                characterMap.put(c, t);
            }


            /*
            Integer r = characterMap.get(c);
            if(r != null) {
                characterMap.put(c, r+1);
            }
            else {
                characterMap.put(c, 1);
            }
            */

            /*
            if (characterMap.containsKey(c)) {
                characterMap.put(c, characterMap.get(c) + 1);
            } else {
                characterMap.put(c, 1);
            }
            */
        }
    }

    public HashMap<Character, CharFrequency> getCharacterMap() {
        return characterMap;
    }
}
