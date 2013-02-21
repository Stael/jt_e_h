package CharUtils;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 19:01
 */
public class CharFrequency implements Comparable<CharFrequency> {
    private int nb = 1;
    private char character;
    private byte[] byteCode;

    public CharFrequency(char c) {
        this.character = c;
    }

    public void up() {
        nb++;
    }

    public int getNb() {
        return nb;
    }

    public void add(int nb) {
        this.nb += nb;
    }

    public void setByteCode(String code) {
        this.byteCode = code.getBytes();
    }

    @Override
    public int compareTo(CharFrequency cf) {
        return nb - cf.getNb();
    }

    @Override
    public String toString() {
        if (character == '\n') {
            return "Character : '\\n' - \t\t\t Frequence : " + nb + " - \t\t\t Code : " + new String(byteCode);
        } else if (character == '\t') {
            return "Character : '\\t' - \t\t\t Frequence : " + nb + " - \t\t\t Code : " + new String(byteCode);
        } else {
            return "Character : '" + character + "' - \t\t\t Frequence : " + nb + " - \t\t\t Code : " + new String(byteCode);
        }
    }
}