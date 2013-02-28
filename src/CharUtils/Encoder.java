package CharUtils;

import BitManagement.BitArray;
import CharUtils.CharCounter.CharCounter;
import CharUtils.CharEncoder.CharEncoder;
import HuffmanTree.TreeBuilder;
import IO.FileReader;
import Utils.StatusPrinter;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:02
 */
public class Encoder {
    private String pathOfTheFileToEncode;
    private String pathOfTheEncodedFile;
    private String serializedTree;

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
        start = System.currentTimeMillis();

        byte[] byteArray = FileReader.extractTextFromFile(pathOfTheFileToEncode);

        StatusPrinter.printStatus("Fin de la lecture du fichier à encoder", start);

        countNumberOfCharacters(byteArray);
    }

    public void countNumberOfCharacters(byte[] byteArray) {
        start = System.currentTimeMillis();
        textToEncode = new String(byteArray);

        CharCounter cc = new CharCounter(textToEncode, this);
        cc.countMulti();
    }

    public void postCountNumberOfCharacters(HashMap<Character, CharFrequency> characterMap) {
        StatusPrinter.printStatus("Fin du comptage du nombre d'occurences", start);
        this.characterMap = characterMap;

        start = System.currentTimeMillis();

        serializedTree = TreeBuilder.buildTree(characterMap);

        StatusPrinter.printStatus("Fin de la création de l'arbre", start);

        encodeText();
    }

    public void encodeText() {
        start = System.currentTimeMillis();
        CharEncoder ce = new CharEncoder(textToEncode, 10, characterMap, this);
        ce.encodeMulti();

        textToEncode = null;
    }

    public void postEncoding(BitArray[] encodedText) {
        StatusPrinter.printStatus("Fin de l'encodage du fichier", start);

        start = System.currentTimeMillis();
        try {
            FileOutputStream out = new FileOutputStream(pathOfTheEncodedFile);

            StringBuffer textPartHeader = new StringBuffer();
            for(int i = 0; i < encodedText.length; i++) {
                textPartHeader.append('>');
                textPartHeader.append(encodedText[i].getLastByte());
                textPartHeader.append(':');
                textPartHeader.append(encodedText[i].getLastBit());
                textPartHeader.append('.');
            }
            textPartHeader.append("--");

            out.write(textPartHeader.toString().getBytes());

            out.write(serializedTree.getBytes());

            for(int i = 0; i < encodedText.length; i++) {
                out.write(encodedText[i].toByteArray());
            }

            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);

        StatusPrinter.printStatus("Fin de l'encodage", initialStart);

        Decoder d = new Decoder(pathOfTheEncodedFile, "final.txt");
        d.decode();
    }
}
