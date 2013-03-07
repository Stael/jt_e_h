package HuffmanCompressor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:37
 */
class Decoder {
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
        try {
            initialStart = System.currentTimeMillis();
            start = System.currentTimeMillis();

            byte[] byteArray = FileReader.readFile(pathOfTheFileToDecode);

            StatusPrinter.printStatus("Fin de la lecture du fichier à décoder", start);
            start = System.currentTimeMillis();

            decodeHeader(byteArray);

            StatusPrinter.printStatus("Fin du décodage du header", start);
            start = System.currentTimeMillis();

            BitArray[] decodedText = decodeText();

            StatusPrinter.printStatus("Fin du décodage du fichier", start);
            start = System.currentTimeMillis();

            postDecode(decodedText);

            StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);
            StatusPrinter.printStatus("Fin du décodage", initialStart);
        }
        catch (Exception e) {
            System.out.println("Une exception est survenue lors de la compression");
            System.exit(-1);
        }
    }

    /*
        Décodage du header
        Le header est en 4 parties :
            - 1ère : sur 2 octets la longueur du 1er header
            - 2ème : le 1er header, qui concerne la gestion du nombre de byte et du nombre de bit de chque sous partie du fichier
                        format : lastByte:lastBit
            - 3ème : sur 2 octets la longueur du 2nd header
            - 4ème : le 2nd header : l'arbre de Huffman serializer
     */
    public void decodeHeader(byte[] byteArray) {
        // Calcul des indices des 2 headers
        int firstPartHeaderLength = byteArray[0] * 256 + (byteArray[1] & 0xFF);
        int secondPartHeaderLength = byteArray[firstPartHeaderLength + 2] * 256 + (byteArray[firstPartHeaderLength + 3] & 0xFF);

        // Extraction du premier header
        // Compatage du nombre de sous parties
        int nbPart = 0;
        for (int i = 2; i < firstPartHeaderLength + 2; i++) {
            char c = (char) byteArray[i];
            if (c == ':') nbPart++;
        }
        ArrayOfBitExtractor = new BitExtractor[nbPart];

        // Extraction du contenu du header
        int partNumber = 0;
        int i = 2;
        int start = firstPartHeaderLength + secondPartHeaderLength + 4;
        int stop;
        while (i < firstPartHeaderLength + 2) {
            int lastByte = 0;

            // On extrait lastByte
            char c = (char) byteArray[i];
            while (c != ':') {
                lastByte = lastByte * 10 + Character.digit(c, 10);
                i++;
                c = (char) byteArray[i];
            }
            i++;

            // On extrait lastBit
            int lastBit = Character.digit(byteArray[i], 10);
            i++;

            // On extrait la sous partie associée à lastByte et lastBit
            // Sans avoir à découper le tableau de byte représentant le sous fichier en sous tableaux
            stop = start + lastByte;
            BitExtractor ba = new BitExtractor(byteArray, stop, lastBit, start);

            ArrayOfBitExtractor[partNumber] = ba;

            partNumber++;

            start = stop + 1;
        }

        // Extraction du header contenant l'arbre serializer
        // et reconstruction de l'arbre
        String treeHeader = new String(Arrays.copyOfRange(byteArray, firstPartHeaderLength + 4, firstPartHeaderLength + secondPartHeaderLength + 3));
        tree = new HuffmanTree(treeHeader);
    }

    public BitArray[] decodeText() {
        CharDecoder cd = new CharDecoder(ArrayOfBitExtractor, tree);
        return cd.decodeMulti();
    }

    public void postDecode(BitArray[] decodedText) {
        try {
            File f = new File(pathOfTheDecodedFile);
            while (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                f = f.getParentFile();
            }

            FileOutputStream out = new FileOutputStream(pathOfTheDecodedFile);
            for (int i = 0; i < decodedText.length; i++) {
                out.write(decodedText[i].toByteArray());
            }
        }
        catch (Exception e) {
            System.out.println("Une erreur est survenue à l'écriture du fichier");
            System.exit(-1);
        }
    }
}