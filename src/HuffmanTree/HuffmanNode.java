package HuffmanTree;

import CharUtils.CharFrequency;

/**
 * User: thibaultramires
 * Date: 21/02/13
 * Time: 20:01
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private CharFrequency cf;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(CharFrequency cf) {
        this.cf = cf;
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.left = left;
        this.right = right;
    }

    public HuffmanNode() {}

    public void regenerate(CharFrequency cf, int hauteur) {
        if(left == null) {
            left = new HuffmanNode();
            left.regenerate(cf, hauteur, 1);
        }
        else {
            if(!left.regenerate(cf, hauteur, 1)) {
                if(right == null) {
                    right = new HuffmanNode();
                }
                right.regenerate(cf, hauteur, 1);
            }
        }
    }

    public boolean regenerate(CharFrequency cf, int hauteur, int currentHauteur) {
        if(isLeaf()) {
            return false;
        }
        if(currentHauteur == hauteur-1) {
            if(left == null) {
                left = new HuffmanNode(cf);
                return true;
            }
            else if(right == null) {
                right = new HuffmanNode(cf);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(left == null) {
                left = new HuffmanNode();
                left.regenerate(cf, hauteur, currentHauteur+1);
                return true;
            }
            else {
                if(left.regenerate(cf, hauteur, currentHauteur+1)) {
                    return true;
                }
                else {
                    if(right == null) {
                        right = new HuffmanNode();
                        right.regenerate(cf, hauteur, currentHauteur+1);
                        return true;
                    }
                    else {
                        return right.regenerate(cf, hauteur, currentHauteur+1);
                    }
                }
            }
        }
    }

    public boolean isLeaf() {
        return cf != null;
    }

    public String charAndLength() {
        StringBuffer res = new StringBuffer();

        if(this.isLeaf()) {
            res.append(cf.getCharacter());
            res.append(cf.getByteCode().length());
            res.append('.');
        }
        else {
            res.append(left.charAndLength());
            res.append(right.charAndLength());
        }

        return res.toString();
    }

    public void generateCode(String s) {
        if (cf == null) {
            left.generateCode(s + "0");
            if(right != null) {
                right.generateCode(s + "1");
            }
        } else {
            cf.setByteCode(s);
        }
    }

    @Override
    public int compareTo(HuffmanNode ht) {
        return this.frequency() - ht.frequency();
    }

    private int frequency() {
        return cf == null ? left.frequency() + right.frequency() : cf.getNb();
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
