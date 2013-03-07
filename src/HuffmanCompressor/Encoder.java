package HuffmanCompressor;

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
class Encoder {
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

    /*
        Encodage du fichier
     */
    public void encode() {
        try {
            initialStart = System.currentTimeMillis();
            start = System.currentTimeMillis();

            byte[] byteArray = FileReader.readFile(pathOfTheFileToEncode);

            StatusPrinter.printStatus("Fin de la lecture du fichier à encoder", start);
            start = System.currentTimeMillis();

            textToEncode = explodeByteArray(byteArray);

            StatusPrinter.printStatus("Fin de la découpe du fichier", start);
            start = System.currentTimeMillis();

            CharFrequency[] charFrequency = countNumberOfCharacters(byteArray);

            StatusPrinter.printStatus("Fin du comptage du nombre d'occurences", start);
            start = System.currentTimeMillis();

            HuffmanTree huffmanTree = new HuffmanTree(charFrequency);
            serializedTree = huffmanTree.charAndLength();

            StatusPrinter.printStatus("Fin de la création de l'arbre", start);
            start = System.currentTimeMillis();

            BitArray[] encodedText = encodeText(charFrequency);

            StatusPrinter.printStatus("Fin de l'encodage du fichier", start);
            start = System.currentTimeMillis();

            saveCompressedFile(encodedText);

            StatusPrinter.printStatus("Fin de l'écriture du fichier encodé", start);
            StatusPrinter.printStatus("Fin de l'encodage", initialStart);
        }
        catch (Exception e) {
            System.out.println("Une exception est survenue lors de la compression");
            System.exit(-1);
        }
    }

    /*
        On éclate notre fichier en sous parties.
        Cela pourait être évité en manipulant uniquement le tableau de byte représentant le fichier
        et en jouant directement sur les indices, comme fait dans le décodage.
        Cela n'a pu être optimisé faute de temps
     */
    private List<byte[]> explodeByteArray(byte[] byteArray) {
        int byteArrayLength = byteArray.length;

        // On découpe le fichier en le nombre de coeurs disponibles.
        // On aurait pu optimiser le nombre de threads en fonction de la taille du fichier
        int nbThread = Runtime.getRuntime().availableProcessors();
        List<byte[]> textToEncode = new ArrayList<byte[]>();

        // Découpe du fichier
        int startIndice = 0;
        for (int i = 0; i < nbThread; i++) {
            // Calcul des indices de début et de fin de la sous partie
            int stopIndice = startIndice + (int) Math.ceil(byteArrayLength / nbThread) + 1;
            stopIndice = stopIndice > byteArrayLength ? byteArrayLength : stopIndice;
            // Copie de la sous partie
            textToEncode.add(Arrays.copyOfRange(byteArray, startIndice, stopIndice));

            startIndice = stopIndice;
        }
        return textToEncode;
    }

    /*
        Comptage de la du nombre d'occurence de chaque carractère
     */
    public CharFrequency[] countNumberOfCharacters(byte[] byteArray) {
        // Initialisation du tableau de fréquence
        CharFrequency[] charFrequency = new CharFrequency[256];
        for (int i = 0; i < 256; i++) {
            charFrequency[i] = new CharFrequency((char) i);
        }

        // Comptage du nombre d'occurence
        int length = byteArray.length;
        for (int i = 0; i < length; i++) {
            if ((int) byteArray[i] >= 0)
                charFrequency[(int) byteArray[i]].up();
        }

        return charFrequency;

    }

    /*
        Encodage multi threadé du fichier
     */
    public BitArray[] encodeText(CharFrequency[] charFrequency) {
        CharEncoder ce = new CharEncoder(textToEncode, charFrequency);
        textToEncode = null;

        return ce.encodeMulti();
    }

    /*
        Sauvegarde de la version compressée du fichier
     */
    public void saveCompressedFile(BitArray[] encodedText) {
        try {
            // On crée les dossiers parents si ils n'existent pas
            File f = new File(pathOfTheEncodedFile);
            while (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
                f = f.getParentFile();
            }

            FileOutputStream out = new FileOutputStream(pathOfTheEncodedFile);

            StringBuffer textPartHeader = createFirstPartOfTheHeader(encodedText);

            // On écrit la taille de la 1ère partie de l'en-tête
            out.write(intToByteArray(textPartHeader.toString().length()));

            // On écrit la première partie de l'en-tête
            out.write(textPartHeader.toString().getBytes());

            // On écrit la taille de la 2nd partie de l'en-tête
            out.write(intToByteArray(serializedTree.getBytes().length));

            // On écrit la seconde partie de l'en-tête
            out.write(serializedTree.getBytes());

            // On écrit toutes les sous parties du texte compressé
            // Cela permet d'éviter d'avoir à les fusionner en un seul tableau de byte
            // ce qui réduirait très fortement les performances
            for (int i = 0; i < encodedText.length; i++) {
                out.write(encodedText[i].toByteArray());
            }

            out.close();

        } catch (Exception e) {
            System.out.println("Une erreur est survenue à l'écriture du fichier");
            System.exit(-1);
        }
    }

    /*
        Création de la première partie de l'en-tête
        qui contient les caractéristiques des sous partie de la version
        encodée du texte
     */
    private StringBuffer createFirstPartOfTheHeader(BitArray[] encodedText) {
        StringBuffer textPartHeader = new StringBuffer();
        for (int i = 0; i < encodedText.length; i++) {
            textPartHeader.append(encodedText[i].getLastByte());
            textPartHeader.append(':');
            textPartHeader.append(encodedText[i].getLastBit());
        }
        return textPartHeader;
    }

    /*
        Conversion d'un int (d'une taille maximal de 2^16 - 1)
        en tableau de byte
     */
    public byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 8),
                (byte) value
        };
    }
}
