package HuffmanTree;

import CharUtils.CharFrequency;

import java.util.HashMap;

/**
 * User: thibaultramires
 * Date: 27/02/13
 * Time: 18:02
 */
public class HuffmanTree {
    private HuffmanNode root;
    private HashMap<Character, CharFrequency> characterMap = new HashMap<Character, CharFrequency>();

    public HuffmanTree(HuffmanNode root) {
        this.root = root;
    }

    public HuffmanTree() {
    }

    public void generateCode() {
        root.generateCode("");
    }

    public String charAndLength() {
        return root.charAndLength() + "..";
    }

    public HashMap<Character, CharFrequency> regeneration(String serializedTree) {
        root = new HuffmanNode();

        int i = 0;
        while(i < serializedTree.length()) {

            char c = serializedTree.charAt(i);
            i++;

            StringBuffer length = new StringBuffer();
            while(i < serializedTree.length() && serializedTree.charAt(i) != '.') {
                length.append(serializedTree.charAt(i));
                i++;
            }
            i++;
            int charLength = Integer.parseInt(length.toString());

            CharFrequency cf = new CharFrequency(c);

            characterMap.put(c, cf);
            root.regenerate(cf, charLength, 0);
        }

        generateCode();

        return characterMap;
    }

    public HuffmanNode getRoot() {
        return root;
    }
}
