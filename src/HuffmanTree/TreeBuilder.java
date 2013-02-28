package HuffmanTree;

import CharUtils.CharFrequency;

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

        PriorityQueue<HuffmanNode> prioQueue = new PriorityQueue<HuffmanNode>();
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            prioQueue.add(new HuffmanNode(e.getValue()));
        }

        while (prioQueue.size() > 1) {
            HuffmanNode left = prioQueue.poll();
            HuffmanNode right = prioQueue.poll();

            HuffmanNode nt = new HuffmanNode(left, right);

            prioQueue.add(nt);
        }

        HuffmanNode root = prioQueue.poll();
        HuffmanTree tree = new HuffmanTree(root);
        tree.generateCode();

        return tree.charAndLength();
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
