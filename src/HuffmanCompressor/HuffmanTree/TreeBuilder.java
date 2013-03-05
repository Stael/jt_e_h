package HuffmanCompressor.HuffmanTree;

import HuffmanCompressor.CharUtils.CharFrequency;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 19:14
 */
public class TreeBuilder {

    public static String buildTree(HashMap<Character, CharFrequency> characterMap) {
        return null;
    }

    public static HashMap<Character, CharFrequency> regenerateTree(String serializedTree) {
        HuffmanTree tree = new HuffmanTree();
        return tree.regeneration(serializedTree);
    }

    public static HuffmanTree regenerateTreeT(String serializedTree) {
        HuffmanTree tree = new HuffmanTree();
        tree.regeneration(serializedTree);
        return tree;
    }
}
