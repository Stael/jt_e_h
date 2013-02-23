package CharUtils;

import BitManagement.BitArray;
import CharUtils.CharDecoder.CharDecoder;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:37
 */
public class Decoder {

    public void decode(BitArray encodedText, HashMap<Character, CharFrequency> characterMap) {
        CharDecoder cd = new CharDecoder(encodedText.toByteArray(), characterMap);
        String res = cd.decode();
    }

    public void postDecode(String decodedText) {
        try {
            FileOutputStream out = new FileOutputStream("final");
            out.write(decodedText.getBytes());
        }
        catch (Exception e) {
            System.out.println("Yolo !");
        }
    }
}
