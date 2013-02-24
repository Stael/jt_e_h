package CharUtils;

import BitManagement.BitArray;
import CharUtils.CharCounter.CharCounter;
import CharUtils.CharEncoder.CharEncoder;
import HuffmanTree.TreeBuilder;
import Utils.StatusPrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:02
 */
public class Encoder {
    private String pathOfTheFileToEncode;
    private String pathOfTheEncodedFile;

    private String textToEncode;
    private HashMap<Character, CharFrequency> characterMap;

    private long start;
    private long initialStart;

    public Encoder(String pathOfTheFileToEncode, String pathOfTheEncodedFile) {
        this.pathOfTheFileToEncode = pathOfTheFileToEncode;
        this.pathOfTheEncodedFile = pathOfTheEncodedFile;
    }

    public void encode() {
        initialStart = System.currentTimeMillis();

        byte[] byteArray = extractTextFromFile();

        countNumberOfCharacters(byteArray);
    }

    public byte[] extractTextFromFile() {
        start = System.currentTimeMillis();

        Path path = Paths.get(pathOfTheFileToEncode);
        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(path);

        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StatusPrinter.printStatus("Fin de la lecture du fichier à encoder", start);
        return byteArray;
    }

    public void countNumberOfCharacters(byte[] byteArray) {
        start = System.currentTimeMillis();
        textToEncode = new String(byteArray);

        CharCounter cc = new CharCounter(textToEncode, System.currentTimeMillis(), this);
        cc.countMulti();
    }

    public void postCountNumberOfCharacters(HashMap<Character, CharFrequency> characterMap) {
        StatusPrinter.printStatus("Fin du comptage du nombre d'occurences", start);
        this.characterMap = characterMap;

        start = System.currentTimeMillis();

        TreeBuilder.buildTree(characterMap);

        StatusPrinter.printStatus("Fin de la création de l'arbre", start);

        encodeText();
    }

    public void encodeText() {
        start = System.currentTimeMillis();
        CharEncoder ce = new CharEncoder(textToEncode, 10, characterMap, this);
        ce.encodeMulti();
    }

    public void postEncoding(BitArray[] encodedText) {
        StatusPrinter.printStatus("Fin de l'encodage du fichier", start);

        start = System.currentTimeMillis();
        try {
            FileOutputStream out = new FileOutputStream(pathOfTheEncodedFile);

            //out.write(encodedText.toByteArray());

            for(int i = 0; i < encodedText.length; i++) {
                out.write(encodedText[i].byteArrayToWrite());
                while(encodedText[i].hasNextBitToWrite()) {
                    out.write(encodedText[i].nextBitToWrite());
                }
            }
        }
        catch (Exception e) {
            System.out.println("Yolo !");
        }

        StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);

        StatusPrinter.printStatus("Fin de l'encodage", initialStart);
    }
}
