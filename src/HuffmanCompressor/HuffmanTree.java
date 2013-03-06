package HuffmanCompressor;

import java.util.PriorityQueue;

/**
 * User: thibaultramires
 * Date: 27/02/13
 * Time: 18:02
 */
class HuffmanTree {
    private HuffmanNode root;

    /*
        Création de l'arbre de Huffman grâce à sa version "texte"
     */
    public HuffmanTree(String serializedTree) {
        root = new HuffmanNode();

        int i = 0;
        while (i < serializedTree.length()) {

            char c = serializedTree.charAt(i);
            i++;

            StringBuffer length = new StringBuffer();
            while (i < serializedTree.length() && serializedTree.charAt(i) != '.') {
                length.append(serializedTree.charAt(i));
                i++;
            }
            i++;

            int charLength = Integer.parseInt(length.toString());

            CharFrequency cf = new CharFrequency(c);

            root.regenerate(cf, charLength, 0);
        }

        generateCode();
    }

    /*
        Création de l'arbre de Huffman à partir du tableau de fréquences
     */
    public HuffmanTree(CharFrequency[] charFrequency) {
        PriorityQueue<HuffmanNode> prioQueue = new PriorityQueue<HuffmanNode>();
        for (int i = 0; i < 256; i++) {
            if (charFrequency[i].getNbOccurence() > 0)
                prioQueue.add(new HuffmanNode(charFrequency[i]));
        }

        while (prioQueue.size() > 1) {
            HuffmanNode left = prioQueue.poll();
            HuffmanNode right = prioQueue.poll();

            HuffmanNode nt = new HuffmanNode(left, right);

            prioQueue.add(nt);
        }

        root = prioQueue.poll();
        generateCode();
    }

    /*
        Génération du code de chaque caractère
     */
    public void generateCode() {
        root.generateCode("");
    }

    /*
        Génération de la version "texte" d'un arbre
     */
    public String charAndLength() {
        return root.charAndLength();
    }


    public HuffmanNode getRoot() {
        return root;
    }
}
