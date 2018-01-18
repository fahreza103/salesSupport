package id.co.myrepublic.salessupport.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.Map;

import id.co.myrepublic.salessupport.activity.ActivityMain;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.UrlParam;

/**
 * Abstract class for async operation, standard operation is show loading spinner on preExecute, and hide them after done
 * for implementation of doInBackground, extends this class
 *
 * Description for the 3 Objects :
 * Object => Any object  to supply the background process
 * Integer => Used to update progress
 * Map<String,String> , used to map taskKey per URL if this asyncTask performed more than one connection, and returned multiple result
 *
 * @author Fahreza Tamara
 */

public abstract class AbstractAsyncOperation extends AsyncTask<Object, Integer, Map<String,String>> {

    public static final String DEFAULT_RESULT_KEY = "defaultKey";

    protected AsyncUiDisplayType uiType;
    protected String taskName;
    protected Context context;

    private String dialogMsg;
    private AsyncTaskListener listener;
    private ProgressDialog progressDialog;


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
        if(uiType == AsyncUiDisplayType.SCREEN) {
            // Hide all item inside main layout and show progressbar
            // Should be set in ActivityMain
            if (ActivityMain.txtLoading != null) {
                ActivityMain.txtLoading.setVisibility(View.VISIBLE);
            }

            if (ActivityMain.progressBar != null) {
                ActivityMain.progressBar.setVisibility(View.VISIBLE);
                ActivityMain.progressBar.startAnimation(gvar.getPopupAnim());
            }

            if (ActivityMain.progressIcon != null) {
                ActivityMain.progressIcon.setVisibility(View.VISIBLE);
                ActivityMain.progressIcon.startAnimation(gvar.getPopupAnim());
            }
        } else if(uiType == AsyncUiDisplayType.DIALOG) {
            // Show progress dialog
            progressDialog = DialogBuilder.getInstance().createProgressDialog(this.context,this.dialogMsg);

        } else if(uiType == AsyncUiDisplayType.NONE) {
            // No action
        }

        // For custom UI Modification
        if(listener != null) {
            listener.onAsynTaskStart(this.taskName);
        }
    }


    protected void onProgressUpdate(Integer... progress) {}

    protected void onPostExecute(Map<String,String> result) {
        // Hide everything that shows progress UI
        if(ActivityMain.txtLoading!= null) {
            ActivityMain.txtLoading.setVisibility(View.GONE);
        }

        if(ActivityMain.progressBar != null) {
            ActivityMain.progressBar.setVisibility(View.GONE);
        }

        if(ActivityMain.progressIcon != null) {
            ActivityMain.progressIcon.setVisibility(View.GONE);
        }

        if(this.progressDialog != null) {
            this.progressDialog.dismiss();
        }

        // if listener defined , do callback
        if(listener != null) {
            listener.onAsyncTaskComplete(result, this.taskName);
        }


    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public void setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
    }
}
