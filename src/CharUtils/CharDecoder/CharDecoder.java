package CharUtils.CharDecoder;

import BitManagement.BitExtractor;
import CharUtils.CharFrequency;

import java.util.HashMap;
import java.util.Map;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:42
 */
public class CharDecoder {
    private BitExtractor bitArray;
    private HashMap<Character, CharFrequency> characterMap;
    private HashMap<String, CharFrequency> stringMap;

    public CharDecoder(byte[] byteArray, HashMap<Character, CharFrequency> characterMap) {
        stringMap = new HashMap<String, CharFrequency>();
        this.bitArray = new BitExtractor(byteArray);
        this.characterMap = characterMap;
        convertCharacterMap();
    }

    private void convertCharacterMap() {
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            stringMap.put(e.getValue().getByteCode(), e.getValue());
        }
    }

    public String decode() {
        StringBuffer res = new StringBuffer();
        while(bitArray.hasNext()) {
            String binaryWord = bitArray.next();
            //System.out.println("Binary word : " + binaryWord);
            //bitArray.printState();
            CharFrequency cf = stringMap.get(binaryWord);
            if(cf != null) {
                System.out.println(" new !! " + cf.getCharacter());
                bitArray.newString();
                res.append(cf.getCharacter());
            }
        }

        return res.toString();
    }

}
