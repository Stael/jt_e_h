/**
 * User: thibaultramires
 * Date: 20/02/13
 * Time: 20:17
 */

import CharUtils.CharCounter.CharCounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Reader {

    public void exec() {
        String s = "files/pgtest.txt";
        //this.naive(s);
        this.advanced(s);
    }

    public void naive(String s) {
        long start = System.currentTimeMillis();
        Path path = Paths.get(s);
        final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
        try {
            BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());
            char c;
            while (reader.ready()) {
                c = (char) reader.read();
                if (characterMap.containsKey(c)) {
                    characterMap.put(c, characterMap.get(c) + 1);
                } else {
                    characterMap.put(c, 1);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Exec Time - Naive : " + (System.currentTimeMillis() - start) + " ms");
    }

    public void advanced(String s) {
        long start = System.currentTimeMillis();
        Path path = Paths.get(s);

        final HashMap<Character, Integer> characterMap = new HashMap<Character, Integer>();
        try {
            byte[] byteArray = Files.readAllBytes(path);

            String res = new String(byteArray);
            //System.out.println("Initials chars : " + res.length());

            CharCounter cc = new CharCounter(res, start);
            cc.countMulti();

            //System.out.println("Ram used : " + Runtime.getRuntime().totalMemory() / 1000000 + " M");

        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
