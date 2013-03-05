package HuffmanCompressor.CharUtils;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 19:01
 */
public class CharFrequency implements Comparable<CharFrequency> {
    private int nbOccurence = 1;
    private char character;
    private String byteCode = "";

    public CharFrequency(char character) {
        this.character = character;
    }

    public void up() {
        nbOccurence++;
    }

    public int getNbOccurence() {
        return nbOccurence;
    }

    public void add(int nb) {
        this.nbOccurence += nb;
    }

    public void setByteCode(String code) {
        this.byteCode = code;
    }

    public String getByteCode() {
        return byteCode;
    }

    @Override
    public int compareTo(CharFrequency cf) {
        return nbOccurence - cf.getNbOccurence();
    }

    public char getCharacter() {
        return character;
    }
}