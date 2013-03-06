package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 19:01
 */

/*
    Classe de management des caractère
    On stock le code de Huffman dans un tableau de booléens pour de meilleurs performances
    On garde également le code sous forme de string par faute de temps pour le retirer
 */
class CharFrequency implements Comparable<CharFrequency> {
    private int nbOccurence = 0;
    private char character;
    private String byteCode;
    private boolean[] code;

    public CharFrequency(char character) {
        this.character = character;
    }

    /*
        Augmentation du nombre d'occurence de ce caractère
     */
    public void up() {
        nbOccurence++;
    }

    /*
        Stockage du code de Huffman du caractère
     */
    public void setByteCode(String code) {
        // Stockage sous forme de string
        this.byteCode = code;

        // Stockage sous forme de tableau de booléens
        this.code = new boolean[code.length()];
        for (int i = 0; i < code.length(); i++) {
            this.code[i] = code.charAt(i) == '1' ? true : false;
        }
    }

    /*
        Comparaison de 2 caractères entre eux en fonction de leur fréquence
     */
    @Override
    public int compareTo(CharFrequency cf) {
        return nbOccurence - cf.getNbOccurence();
    }

    public String getByteCode() {
        return byteCode;
    }

    public int getNbOccurence() {
        return nbOccurence;
    }

    public char getCharacter() {
        return character;
    }

    public boolean[] getCode() {
        return code;
    }
}