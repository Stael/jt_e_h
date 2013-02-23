package CharUtils.CharEncoder;


import BitManagement.BitArray;
import CharUtils.CharFrequency;

import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 21:48
 */
public class CharEncoder {
    private String s;
    private BitArray ba;
    private HashMap<Character, CharFrequency> characterMap;

    public CharEncoder(String s, int size, HashMap<Character, CharFrequency> characterMap) {
        this.s = s;
        this.ba = new BitArray(size);
        this.characterMap = characterMap;
    }

    public BitArray encode() {
        CharFrequency cf;
        for(int i = 0; i < s.length(); i++) {
            cf = characterMap.get(s.charAt(i));
            if(cf == null) {
                System.out.println("Yolo FAIL !");
            }
            ba.add(cf.getByteCode());
        }

        return ba;
    }
}
