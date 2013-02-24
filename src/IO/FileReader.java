package IO;

import CharUtils.Encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {
    public static byte[] extractTextFromFile(String fileName) {
        Path path = Paths.get(fileName);
        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(path);

        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return byteArray;
    }
}