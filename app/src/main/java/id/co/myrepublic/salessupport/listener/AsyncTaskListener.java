package id.co.myrepublic.salessupport.listener;

/**
 * Created by myrepublicid on 28/9/17.
 */

public interface AsyncTaskListener<T> {

    public void onAsyncTaskComplete(T result);
}
