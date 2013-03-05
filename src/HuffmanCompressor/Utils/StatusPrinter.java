package HuffmanCompressor.Utils;

/**
 * User: thibaultramires
 * Date: 23/02/13
 * Time: 21:43
 */
public class StatusPrinter {
    public static void printStatus(String status, long start) {
        System.out.println("Statut : " + status);
        System.out.println("Exec Time : " + (System.currentTimeMillis() - start) + " ms");
        System.out.println("Used Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " Mo");
        System.out.println();
    }
}
