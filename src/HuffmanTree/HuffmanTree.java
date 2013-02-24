package HuffmanTree;

import CharUtils.CharFrequency;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 20:01
 */
public class HuffmanTree implements Comparable<HuffmanTree> {
    private CharFrequency cf;
    private HuffmanTree left;
    private HuffmanTree right;

    public HuffmanTree(CharFrequency cf) {
        this.cf = cf;
    }

    public HuffmanTree(HuffmanTree left, HuffmanTree right) {
        this.left = left;
        this.right = right;
    }

    public void generateCode() {
        generateCode("");
    }

    private void generateCode(String s) {
        if (cf == null) {
            left.generateCode(s + "0");
            right.generateCode(s + "1");
        } else {
            cf.setByteCode(s);
        }
    }

    @Override
    public int compareTo(HuffmanTree ht) {
        return this.frequency() - ht.frequency();
    }

    private int frequency() {
        return cf == null ? left.frequency() + right.frequency() : cf.getNb();
    }
}
