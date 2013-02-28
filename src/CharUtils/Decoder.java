package CharUtils;

import BitManagement.BitArray;
import BitManagement.BitExtractor;
import CharUtils.CharDecoder.CharDecoder;
import HuffmanTree.HuffmanTree;
import HuffmanTree.TreeBuilder;
import IO.FileReader;
import Utils.StatusPrinter;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:37
 */
public class Decoder {
    private String pathOfTheFileToDecode;
    private String pathOfTheDecodedFile;

    private HuffmanTree tree;
    private BitExtractor[] ArrayOfBitExtractor;

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

        decodeHeader(byteArray);
    }

    public void decodeHeader(byte[] byteArray) {
        start = System.currentTimeMillis();

        int i = 1;
        String header;
        while(true) {
            header = new String(Arrays.copyOfRange(byteArray, 0, 1000*i));
            if(header.indexOf("--") != -1 && header.indexOf("...") != -1) {
                break;
            }
            i++;
        }

        int headerLength = header.substring(0, header.indexOf("...")+3).getBytes().length;

        // On récupère la taille des parties du fichier
        String patternString1 = ">([0-9]*):([0-9]*)\\.";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(header);

        int partLength = 0;
        while (matcher.find()) {
            partLength++;
        }
        ArrayOfBitExtractor = new BitExtractor[partLength];

        matcher = pattern.matcher(header);

        int partNumber = 0;
        int startIndex = headerLength;
        while(matcher.find()) {
            int lastByte = Integer.parseInt(matcher.group(1));
            int lastBit = Integer.parseInt(matcher.group(2));
            int stopIndex = startIndex + Integer.parseInt(matcher.group(1)) + 1;
            byte[] part = Arrays.copyOfRange(byteArray, startIndex, stopIndex);
            BitExtractor ba = new BitExtractor(part, lastByte, lastBit);
            ArrayOfBitExtractor[partNumber] = ba;
            partNumber++;
            startIndex = stopIndex;
        }

        // On regenére l'arbe de Huffamn
        String treeHeader = header.substring(header.indexOf("--")+2, header.indexOf("...")+1);
        tree = TreeBuilder.regenerateTreeT(treeHeader);

        StatusPrinter.printStatus("Fin du décodage du header", start);
        decodeText();
    }

    public void decodeText() {
        start = System.currentTimeMillis();

        CharDecoder cd = new CharDecoder(ArrayOfBitExtractor, tree, this);
        cd.decodeMulti();
    }

    public void postDecode(String[] decodedText) {
        StatusPrinter.printStatus("Fin du décodage du fichier", start);
        start = System.currentTimeMillis();

        try {
            FileOutputStream out = new FileOutputStream(pathOfTheDecodedFile);
            for(int i = 0; i < decodedText.length; i++) {
                out.write(decodedText[i].getBytes());
            }
        }
        catch (Exception e) {
            System.out.println("Yolo !");
        }

        StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);
        StatusPrinter.printStatus("Fin du décodage", initialStart);
    }
}
