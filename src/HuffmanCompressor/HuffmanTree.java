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
            format : char hauteur . (sans les espaces)
            Aurait pu être optimisé pour retirer le . en travaillant directement sur le tableau de byte
            Et en considérant la hauteur comme un seul byte

            De même on aurait pu remplacer le string buffer par un integer directement et faire des
            décalages de 10 comme fait dans le décoder pour retrouver lastByte
     */
    public HuffmanTree(String serializedTree) {
        root = new HuffmanNode();

        int i = 0;
        while (i < serializedTree.length()) {

            // On récupère le caractère
            char c = serializedTree.charAt(i);
            i++;

            // On extrait la hauteur tant qu'on a pas atteint un point
            StringBuffer length = new StringBuffer();
            while (i < serializedTree.length() && serializedTree.charAt(i) != '.') {
                length.append(serializedTree.charAt(i));
                i++;
            }
            i++;

            // On converti le string portant la longueur en int
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
        // On insère notre classe de management dans la file de priorité
        PriorityQueue<HuffmanNode> prioQueue = new PriorityQueue<HuffmanNode>();
        for (int i = 0; i < 256; i++) {
            if (charFrequency[i].getNbOccurence() > 0)
                prioQueue.add(new HuffmanNode(charFrequency[i]));
        }

        while (prioQueue.size() > 1) {
            // On dépile 2 éléments (racine ou noeuds)
            HuffmanNode left = prioQueue.poll();
            HuffmanNode right = prioQueue.poll();

            // On les ajoute à un nouveau noeud
            HuffmanNode nt = new HuffmanNode(left, right);

            prioQueue.add(nt);
        }

        // Quand il ne reste plus qu'un élément, c'est notre racine
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
