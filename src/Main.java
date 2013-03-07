import HuffmanCompressor.HuffmanCompressor;

public class Main {

    public static void main(String[] args) {

        /*
            Modèle :
                1er argument : encode / decode
                2ème argument : fichier à encoder / décoder
                3ème argument : fichié encodé / décodé
         */

        if(args.length == 3) {
            if(!args[1].matches("^//")) {
                args[1] = System.getProperty("user.dir") + "/" + args[1];
            }

            if(!args[2].matches("^//")) {
                args[2] = System.getProperty("user.dir") + "/" + args[2];
            }

            if(args[0].equals("encode")) {
                HuffmanCompressor.compress(args[1], args[2]);
            }
            else if(args[0].equals("decode")) {
                HuffmanCompressor.decompress(args[1], args[2]);
            }
            else {
                System.out.println("Premier argument invalide");
            }
        }
        else {
            System.out.println("Nombre d'arguments invalide !");
        }
    }
}
