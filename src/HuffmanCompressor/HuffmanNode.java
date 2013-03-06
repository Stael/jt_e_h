package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 20:01
 */
class HuffmanNode implements Comparable<HuffmanNode> {
    private CharFrequency cf;
    private HuffmanNode left;
    private HuffmanNode right;

    /*
        Création d'un noeud "vierge"
     */
    public HuffmanNode() {
    }

    /*
        Création d'une feuille
     */
    public HuffmanNode(CharFrequency cf) {
        this.cf = cf;
    }

    /*
        Création d'un noeud binaire
     */
    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.left = left;
        this.right = right;
    }

    /*
        Regénération de l'arbre grâce à sa version "texte"
        On cherche récursivement à placer la valeur du noeud sur une feuille la plus
        à gauche possible à la bonne hauteur
     */
    public boolean regenerate(CharFrequency cf, int hauteur, int currentHauteur) {
        if (isLeaf()) {
            return false;
        }

        if (currentHauteur == hauteur - 1) {
            if (left == null) {
                left = new HuffmanNode(cf);
                return true;
            } else if (right == null) {
                right = new HuffmanNode(cf);
                return true;
            } else {
                return false;
            }
        } else {
            if (left == null) {
                left = new HuffmanNode();
                left.regenerate(cf, hauteur, currentHauteur + 1);
                return true;
            } else {
                if (left.regenerate(cf, hauteur, currentHauteur + 1)) {
                    return true;
                } else {
                    if (right == null) {
                        right = new HuffmanNode();
                        right.regenerate(cf, hauteur, currentHauteur + 1);
                        return true;
                    } else {
                        return right.regenerate(cf, hauteur, currentHauteur + 1);
                    }
                }
            }
        }
    }

    /*
        Test si un noeud est une feuille
     */
    public boolean isLeaf() {
        return cf != null;
    }

    /*
        Création récursive de la version "texte" de l'arbre
     */
    public String charAndLength() {
        StringBuffer res = new StringBuffer();

        if (this.isLeaf()) {
            res.append(cf.getCharacter());
            res.append(cf.getByteCode().length());
            res.append('.');
        } else {
            res.append(left.charAndLength());
            res.append(right.charAndLength());
        }

        return res.toString();
    }

    /*
        Génération du code associé au caractère
        en fonction de sa position dans l'arbre
     */
    public void generateCode(String s) {
        if (cf == null) {
            left.generateCode(s + "0");
            if (right != null) {
                right.generateCode(s + "1");
            }
        } else {
            cf.setByteCode(s);
        }
    }

    /*
        Comparaison de la fréquence de 2 noeuds entre eux
     */
    @Override
    public int compareTo(HuffmanNode ht) {
        return this.frequency() - ht.frequency();
    }

    /*
        Calcul de la fréquence d'un noeud
     */
    private int frequency() {
        return cf == null ? left.frequency() + right.frequency() : cf.getNbOccurence();
    }


    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public CharFrequency getCf() {
        return cf;
    }
}
