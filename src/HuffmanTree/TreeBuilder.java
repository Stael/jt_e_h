package HuffmanTree;

import CharUtils.CharFrequency;
import HuffmanTree.HuffmanTree;

import java.util.*;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 19:14
 */
public class TreeBuilder {

    public static TreeSet<CharFrequency> buildTree(HashMap<Character, CharFrequency> characterMap) {

        PriorityQueue<HuffmanTree> prioQueue = new PriorityQueue<HuffmanTree>();
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            prioQueue.add(new HuffmanTree(e.getValue()));
        }

        while (prioQueue.size() > 1) {
            HuffmanTree left = prioQueue.poll();
            HuffmanTree right = prioQueue.poll();

            HuffmanTree nt = new HuffmanTree(left, right);

            prioQueue.add(nt);
        }

        HuffmanTree finalTree = prioQueue.poll();
        finalTree.generateCode();

        PriorityQueue<CharFrequency> pq = new PriorityQueue<CharFrequency>();
        for (Map.Entry<Character, CharFrequency> e : characterMap.entrySet()) {
            pq.add(e.getValue());
        }

        while(!pq.isEmpty()) {
            CharFrequency cf = pq.poll();
            System.out.println(cf);
        }

        return null;
    }
}
