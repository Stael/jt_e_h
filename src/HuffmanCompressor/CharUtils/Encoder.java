package HuffmanCompressor.CharUtils;

import HuffmanCompressor.BitManagement.BitArray;
import HuffmanCompressor.CharUtils.CharCounter.CharCounter;
import HuffmanCompressor.CharUtils.CharEncoder.CharEncoder;
import HuffmanCompressor.HuffmanTree.HuffmanTree;
import HuffmanCompressor.IO.FileReader;
import HuffmanCompressor.Utils.StatusPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 13:02
 */
public class Encoder {
    private String pathOfTheFileToEncode;
    private String pathOfTheEncodedFile;
    private String serializedTree;
    private List<byte[]> textToEncode;

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
        start = System.currentTimeMillis();

        System.gc();

        StatusPrinter.printStatus("Fin GC", start);
        start = System.currentTimeMillis();

        textToEncode = explodeByteArray(byteArray);
        byteArray = null;

        StatusPrinter.printStatus("Fin de la découpe du fichier", start);
        start = System.currentTimeMillis();

        System.gc();

        StatusPrinter.printStatus("Fin GC", start);
        start = System.currentTimeMillis();

        CharFrequency[] charFrequency = countNumberOfCharacters(textToEncode);

        StatusPrinter.printStatus("Fin du comptage du nombre d'occurences", start);
        start = System.currentTimeMillis();

        HuffmanTree huffmanTree = new HuffmanTree(charFrequency);
        serializedTree = huffmanTree.charAndLength();

        StatusPrinter.printStatus("Fin de la création de l'arbre", start);
        start = System.currentTimeMillis();

        BitArray[] encodedText = encodeText(charFrequency);

        StatusPrinter.printStatus("Fin de l'encodage du fichier", start);
        start = System.currentTimeMillis();

        System.gc();

        StatusPrinter.printStatus("Fin GC", start);
        start = System.currentTimeMillis();

        saveCompressedFile(encodedText);

        StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);
        StatusPrinter.printStatus("Fin de l'encodage", initialStart);
    }

    private List<byte[]> explodeByteArray(byte[] byteArray) {
        int byteArrayLength = byteArray.length;

        int nbThread = 8;
        List<byte[]> textToEncode = new ArrayList<byte[]>();

        int startIndice = 0;
        for(int i = 0; i < nbThread; i++) {
            int stopIndice = startIndice + (int) Math.ceil(byteArrayLength/nbThread) + 1;
            stopIndice = stopIndice > byteArrayLength ? byteArrayLength : stopIndice;
            textToEncode.add(Arrays.copyOfRange(byteArray, startIndice, stopIndice));
            startIndice = stopIndice;
        }
        return textToEncode;
    }

    public CharFrequency[] countNumberOfCharacters(List<byte[]> textToEncode) {
        start = System.currentTimeMillis();

        CharCounter cc = new CharCounter(textToEncode);
        return cc.countMono();

    }

    public BitArray[] encodeText(CharFrequency[] charFrequency) {
        CharEncoder ce = new CharEncoder(textToEncode, charFrequency);
        textToEncode = null;

        return ce.encodeMulti();
    }

    public void saveCompressedFile(BitArray[] encodedText) {
        try {
            File f = new File(pathOfTheEncodedFile);
            while(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                f = f.getParentFile();
            }

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
    }
}
