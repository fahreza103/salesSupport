package id.co.myrepublic.salessupport.util;

import android.os.AsyncTask;
import android.view.View;

import id.co.myrepublic.salessupport.MainActivity;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;

/**
 * Created by myrepublicid on 28/9/17.
 */

public class AsyncOperation extends AsyncTask<UrlParam, Integer, String> {

    private String taskName;
    private String jsonResult;
    // -1 = failed , 0 = not yet run, 1 = success
    private int asyncStatus = 0;
    private AsyncTaskListener listener;


    public AsyncOperation(String taskName) {
        this.taskName = taskName;
    }

    public String getJsonResult() {
        return jsonResult;
    }


    public int getasyncStatus() {
        return asyncStatus;
    }

    public AsyncTaskListener getListener() {
        return listener;
    }

    public void setListener(AsyncTaskListener listener) {
        this.listener = listener;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    protected void onPreExecute() {
        // Empty previous result
        this.asyncStatus = 0;
        this.jsonResult = null;
        // Should be set in MainActivity
        if(MainActivity.txtLoading != null) {
            MainActivity.txtLoading.setVisibility(View.VISIBLE);
        }

        if(MainActivity.progressBar != null) {
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected String doInBackground(UrlParam... urlParams) {
        String result = URLConnector.doConnect(urlParams[0].getUrl(),urlParams[0].getParamMap());

        return result;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {
        // Null return, failed
        if(result == null) {
            this.asyncStatus = -1;
        } else {
            this.asyncStatus = 1;
            this.jsonResult = result;
        }

        if(MainActivity.txtLoading!= null) {
            MainActivity.txtLoading.setVisibility(View.GONE);
        }

        if(MainActivity.progressBar != null) {
            MainActivity.progressBar.setVisibility(View.GONE);
        }

        // if listener defined , do callback
        if(listener != null) {
            listener.onAsyncTaskComplete(result, this.taskName);
        }
    }
}
