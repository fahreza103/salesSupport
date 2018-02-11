package id.co.myrepublic.salessupport.listener;

/**
 * Used to callback to main asyncTask class when progress updated
 *
 * @author Fahreza Tamara
 */

public interface ProgressListener {

    /**
     * Invoked when progress update
     * @param progress
     */
    public void onAsyncProgressUpdate(Object progress);
}
