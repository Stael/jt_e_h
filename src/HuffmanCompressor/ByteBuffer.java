package HuffmanCompressor;

/**
 * User: thibaultramires
 * Date: 05/03/13
 * Time: 23:01
 */

/*
    Classe de buffer de byte utilisé pour la lecture d'un fichier
    Aurait du / pu être fusionnée avec BitArray
 */
class ByteBuffer {

    public byte[] buffer = new byte[8192];

    public int write;

    public void put(byte[] buf, int len) {
        ensure(len);
        System.arraycopy(buf, 0, buffer, write, len);
        write += len;
    }

    public byte[] toByteArray() {
        byte[] temp = new byte[write + 1];
        System.arraycopy(buffer, 0, temp, 0, write);
        return temp;
    }

    private void ensure(int amt) {
        int req = write + amt;
        if (buffer.length <= req) {
            byte[] temp = new byte[req * 2];
            System.arraycopy(buffer, 0, temp, 0, write);
            buffer = temp;
        }
    }

}
