package id.co.myrepublic.salessupport.listener;

/**
 * Callback operation when asynctask is performed
 */

public interface AsyncTaskListener<T> {

    /**
     * Invoked before aysncTask is performed
     * @param taskName
     */
    public void onAsynTaskStart(String taskName);

    /**
     * Invoked when asynctask is complete
     * @param result
     * @param taskName
     */
    public void onAsyncTaskComplete(T result, String taskName);
}
