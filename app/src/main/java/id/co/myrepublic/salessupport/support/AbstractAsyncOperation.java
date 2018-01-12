package id.co.myrepublic.salessupport.support;

import android.os.AsyncTask;
import android.view.View;

import id.co.myrepublic.salessupport.activity.MainActivity;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.util.GlobalVariables;

/**
 * Abstract class for async operation
 */

public abstract class AbstractAsyncOperation extends AsyncTask<Object, Integer, String> {

    protected String taskName;
    private String jsonResult;
    // -1 = failed , 0 = not yet run, 1 = success
    private int asyncStatus = 0;
    private AsyncTaskListener listener;


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

        GlobalVariables gvar = GlobalVariables.getInstance();
        // Should be set in MainActivity
        if(MainActivity.txtLoading != null) {
            MainActivity.txtLoading.setVisibility(View.VISIBLE);
            MainActivity.txtLoading.startAnimation(gvar.getPopupAnim());
        }

        if(MainActivity.progressBar != null) {
            MainActivity.progressBar.setVisibility(View.VISIBLE);
            MainActivity.progressBar.startAnimation(gvar.getPopupAnim());
        }

        if(MainActivity.progressIcon != null) {
            MainActivity.progressIcon.setVisibility(View.VISIBLE);
            MainActivity.progressIcon.startAnimation(gvar.getPopupAnim());
        }
    }


    protected void onProgressUpdate(Integer... progress) {}

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

        if(MainActivity.progressIcon != null) {
            MainActivity.progressIcon.setVisibility(View.GONE);
        }

        // if listener defined , do callback
        if(listener != null) {
            listener.onAsyncTaskComplete(result, this.taskName);
        }
    }
}
