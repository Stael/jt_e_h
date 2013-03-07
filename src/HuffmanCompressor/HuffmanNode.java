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
        // Si le noeud est déjà une feuille, on ne peut pas insérer ici
        if (isLeaf()) {
            return false;
        }

        // Si on a atteint un père possible
        if (currentHauteur == hauteur - 1) {
            // Si le fils gauche est disponible on s'insère dedans
            if (left == null) {
                left = new HuffmanNode(cf);
                return true;
            }
            // Sinon, si le fils droit est disponible on s'insère dedans
            else if (right == null) {
                right = new HuffmanNode(cf);
                return true;
            }
            // Sinon, c'est qu'on ne peut pas s'insérer ici
            else {
                return false;
            }
        } else {
            // Si on est pas sur un père possible
            // On essaye de descendre à gauche
            // Si le fils gauche n'existe pas, on le crée, et on est sur de pouvoir s'insérer ensuite dans un de ses fils
            if (left == null) {
                left = new HuffmanNode();
                left.regenerate(cf, hauteur, currentHauteur + 1);
                return true;
            }
            // Si le fils gauche existe
            else {
                // On regarde si on peut s'insérer dans un de ses fils
                if (left.regenerate(cf, hauteur, currentHauteur + 1)) {
                    return true;
                }
                // Si on ne peut pas s'insérer dans un des fils du fils gauche, on essaye à droite
                else {
                    // Si le fils droit n'existe pas, on le crée, et on est sur de pouvoir s'insérer ensuite dans un de ses fils
                    if (right == null) {
                        right = new HuffmanNode();
                        right.regenerate(cf, hauteur, currentHauteur + 1);
                        return true;
                    }
                    // Sinon, on s'insère dans un des fils du fils droit
                    else {
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
