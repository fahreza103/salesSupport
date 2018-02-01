package id.co.myrepublic.salessupport.listener;

/**
 * Callback operation when asynctask is performed
 *
 * @author Fahreza Tamara
 */

public interface AsyncTaskListener<T> {


    /**
     * Invoked when asynctask is complete
     * @param result
     * @param taskName
     */
    public void onAsyncTaskComplete(T result, String taskName);
}
