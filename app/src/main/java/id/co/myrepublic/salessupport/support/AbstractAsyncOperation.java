package id.co.myrepublic.salessupport.support;

import android.os.AsyncTask;
import android.view.View;

import id.co.myrepublic.salessupport.activity.ActivityMain;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.util.GlobalVariables;

/**
 * Abstract class for async operation, standard operation is show loading spinner on preExecute, and hide them after done
 * for implementation of doInBackground, extends this class
 *
 * @author Fahreza Tamara
 */

public abstract class AbstractAsyncOperation extends AsyncTask<Object, Integer, String> {

    protected String taskName;
    private AsyncTaskListener listener;


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

        GlobalVariables gvar = GlobalVariables.getInstance();
        // Should be set in ActivityMain
        if(ActivityMain.txtLoading != null) {
            ActivityMain.txtLoading.setVisibility(View.VISIBLE);
            ActivityMain.txtLoading.startAnimation(gvar.getPopupAnim());
        }

        if(ActivityMain.progressBar != null) {
            ActivityMain.progressBar.setVisibility(View.VISIBLE);
            ActivityMain.progressBar.startAnimation(gvar.getPopupAnim());
        }

        if(ActivityMain.progressIcon != null) {
            ActivityMain.progressIcon.setVisibility(View.VISIBLE);
            ActivityMain.progressIcon.startAnimation(gvar.getPopupAnim());
        }
    }


    protected void onProgressUpdate(Integer... progress) {}

    protected void onPostExecute(String result) {

        if(ActivityMain.txtLoading!= null) {
            ActivityMain.txtLoading.setVisibility(View.GONE);
        }

        if(ActivityMain.progressBar != null) {
            ActivityMain.progressBar.setVisibility(View.GONE);
        }

        if(ActivityMain.progressIcon != null) {
            ActivityMain.progressIcon.setVisibility(View.GONE);
        }

        // if listener defined , do callback
        if(listener != null) {
            listener.onAsyncTaskComplete(result, this.taskName);
        }
    }
}
