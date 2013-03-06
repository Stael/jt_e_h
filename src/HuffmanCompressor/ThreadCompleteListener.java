package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 17/02/13
 * Time: 18:19
 */

interface ThreadCompleteListener {
    /*
        Interface permettant de transformer une classe en listener
     */
    void notifyOfThreadComplete(final Thread thread);
}