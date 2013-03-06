package HuffmanCompressor;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * User: thibaultramires
 * Date: 05/03/13
 * Time: 21:10
 */
public class HuffmanCompressor {
    /*
        Compression de Huffman avec les chemins des fichiers source et destination
        Les chemins peuvent être absolu ou relatif
     */
    public static void compress(String pathOfTheFileToCompress, String pathOfTheCompressedFile) {
        if (Files.notExists(Paths.get(pathOfTheFileToCompress))) {
            System.out.println("Le fichier source n'existe pas");
        } else {
            Encoder encoder = new Encoder(pathOfTheFileToCompress, pathOfTheCompressedFile);
            encoder.encode();
        }
    }

    /*
        Compression de Huffman avec le chemin du fichier source
        Le chemin peut être absolu ou relatif
     */
    public static void compress(String pathOfTheFileToCompress) {
        compress(pathOfTheFileToCompress, pathOfTheFileToCompress + ".compressed");
    }

    /*
        Décompression de Huffman avec les chemins des fichiers source et destination
        Les chemins peuvent être absolu ou relatif
     */
    public static void decompress(String pathOfTheCompressedFile, String pathOfTheDecodedFile) {
        Decoder decoder = new Decoder(pathOfTheCompressedFile, pathOfTheDecodedFile);
        decoder.decode();
    }

    /*
        Décompression de Huffman avec le chemin du fichier source
        Le chemin peut être absolu ou relatif
     */
    public static void decompress(String pathOfTheCompressedFile) {
        if (Files.notExists(Paths.get(pathOfTheCompressedFile))) {
            System.out.println("Le fichier source n'existe pas");
        } else {
            decompress(pathOfTheCompressedFile, pathOfTheCompressedFile + ".compressed");
        }
    }
}
