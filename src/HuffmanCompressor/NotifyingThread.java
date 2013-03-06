package HuffmanCompressor; /**
 * User: thibaultramires
 * Date: 17/02/13
 * Time: 18:18
 */

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

abstract class NotifyingThread extends Thread {
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    /*
        Ajout d'un listener à notifier en fin d'execution
     */
    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    /*
        Retrait d'un listener
     */
    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    /*
        Notification des listener
     */
    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }

    /*
        Execution du thread, et notification du listener à la fin de l'execution
     */
    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }

    public abstract void doRun();
}
