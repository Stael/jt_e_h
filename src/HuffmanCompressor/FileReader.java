package HuffmanCompressor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

class FileReader {
    /*
        Lecture de tout un fichier et sauvegarde de ce fichier
        dans un tableau de byte
     */
    public static final byte[] readFile(String file) {

        ByteBuffer buffer = new ByteBuffer();
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                buffer.put(buf, len);
            }
            in.close();
        } catch (final IOException e) {
            System.out.println("Une erreur est survenue à la lecture du fichier");
            System.exit(-1);
        }
        return buffer.toByteArray();
    }
}