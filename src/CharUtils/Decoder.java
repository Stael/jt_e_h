package CharUtils;

import BitManagement.BitArray;
import CharUtils.CharDecoder.CharDecoder;
import IO.FileReader;
import Utils.StatusPrinter;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:37
 */
public class Decoder {
    private String pathOfTheFileToDecode;
    private String pathOfTheDecodedFile;

    private String textToDecode;
    private HashMap<Character, CharFrequency> characterMap;

    private long start;
    private long initialStart;

    public Decoder(String pathOfTheFileToDecode, String pathOfTheDecodedFile) {
        this.pathOfTheFileToDecode = pathOfTheFileToDecode;
        this.pathOfTheDecodedFile = pathOfTheDecodedFile;
    }

    public void decode() {
        initialStart = System.currentTimeMillis();
        start = System.currentTimeMillis();

        byte[] byteArray = FileReader.extractTextFromFile(pathOfTheFileToDecode);

        StatusPrinter.printStatus("Fin de la lecture du fichier à décoder", start);

        start = System.currentTimeMillis();

        CharDecoder cd = new CharDecoder(byteArray, characterMap);
        String res = cd.decode();

        StatusPrinter.printStatus("Fin du décodage du fichier", start);

        postDecode(res);
    }

    public void postDecode(String decodedText) {
        start = System.currentTimeMillis();
        try {
            FileOutputStream out = new FileOutputStream("final");
            out.write(decodedText.getBytes());
        }
        catch (Exception e) {
            System.out.println("Yolo !");
        }
        StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);
        StatusPrinter.printStatus("Fin du décodage", initialStart);
    }

    public void setCharacterMap(HashMap<Character, CharFrequency> characterMap) {
        this.characterMap = characterMap;
    }
}
