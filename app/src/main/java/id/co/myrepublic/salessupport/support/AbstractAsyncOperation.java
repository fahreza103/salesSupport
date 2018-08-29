package id.co.myrepublic.salessupport.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.activity.ActivityLogin;
import id.co.myrepublic.salessupport.activity.ActivityMain;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.AsyncTaskListener;
import id.co.myrepublic.salessupport.listener.DialogListener;
import id.co.myrepublic.salessupport.listener.ProgressListener;
import id.co.myrepublic.salessupport.response.MainResponse;
import id.co.myrepublic.salessupport.util.GlobalVariables;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.util.UrlResponse;

/**
 * Abstract class for async operation, standard operation is show loading UI on preExecute, and hide them after done
 * for implementation of doInBackground, extends this class
 *
 * Description for the 3 Objects :
 * Object => Any object  to supply the background process
 * Integer => Used to update progress
 * List<UrlResponse> , used to map taskKey per URL if this asyncTask performed more than one connection, and returned multiple result
 *
 * @author Fahreza Tamara
 */

public abstract class AbstractAsyncOperation extends AsyncTask<Object, Integer, List<UrlResponse>> implements ProgressListener{

    public static final String DEFAULT_RESULT_KEY = "defaultKey";

    protected AsyncUiDisplayType uiType;
    protected String taskName;
    protected String cookie;
    protected Context context;
    protected ProgressDialog progressDialog;
    protected TextView progressTextView;

    private String dialogMsg;
    private AsyncTaskListener listener;

    private Boolean isInLoginActivity = false;


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
            if (ActivityMain.loadingFrame != null) {
                ActivityMain.loadingFrame.setVisibility(View.VISIBLE);

                Animation fadeIn = gvar.getFadeInAnim();
                fadeIn.setDuration(500);
                ActivityMain.loadingFrame.startAnimation(gvar.getFadeInAnim());
            }

            if(ActivityMain.loadingRocket != null && context != null) {
                Glide.with(context)
                        .load(R.mipmap.loading_rocket)
                        .into(ActivityMain.loadingRocket);
            }

        } else if(uiType == AsyncUiDisplayType.DIALOG) {
            // Show progress dialog
            progressDialog = DialogBuilder.getInstance().createProgressDialog(this.context,this.dialogMsg);

        } else if(uiType == AsyncUiDisplayType.NONE) {
            // No action
        }


    }


    @Override
    public void onAsyncProgressUpdate(Object progress) {
        if(progress instanceof Integer) {
            publishProgress((Integer)progress);
        }
    }


    protected void onProgressUpdate(Integer... progress) {
        if(this.uiType == AsyncUiDisplayType.SCREEN && ActivityMain.loadingTextView != null &&progress.length > 0) {
            ActivityMain.loadingTextView.setText(this.context.getResources().getText(R.string.activity_loading)+ " (" + progress[0] + "%)");
        } else if(this.uiType == AsyncUiDisplayType.DIALOG && progress.length > 0) {
            progressDialog.setMessage(getDialogMsg()+"\nProgress"+ " (" + progress[0] + "%)");
        } else {
            if (progressTextView != null && progress.length > 0) {
                String text = this.context.getResources().getText(R.string.status_progress) + " (" + progress[0] + "%)";
                progressTextView.setText(text);
            }
        }
    }

    protected void onCancelled(){
        // Do something when async task is cancelled
    }

    /**
     * Invoked when asyncTask complete the operation, after doInBackground process
     * @param urlResponseList, result list for each response
     */
    protected void onPostExecute(final List<UrlResponse> urlResponseList) {
        final Map<String, MainResponse> result = new HashMap<String,MainResponse>();
        int i = 2;
        String errorMsg = "";
        // Convert result into MainResponse
        for(UrlResponse urlResponse : urlResponseList) {
            // if result class defined, directly convert into object, otherwise null
            MainResponse model = StringUtil.convertStringToMainResponse(urlResponse.getResultValue(),urlResponse.getResultClass());
            if(model != null) {
                // if resultKey has same value for each response, add the postfix number into the key, so the map will store the response in different key (not replacing)
                // it can happen when resultKey not defined so all response will using defaultKey, or you purposely set the resultKey with same value
                if(result.get(urlResponse.getResultKey()) == null) {
                    result.put(urlResponse.getResultKey(), model);
                } else {
                    result.put(urlResponse.getResultKey()+"_"+i, model);
                }
                // if success = false and error contains expired, will be redirect to login
                if(!model.getSuccess() && !StringUtil.isEmpty(model.getError()) && model.getError().toLowerCase().contains("session")) {
                    errorMsg = null;

                    // Redirect to LoginActivity, only if not in LoginActivity, otherwise it just cause endless loop
                    if(!isInLoginActivity) {
                        //showErrorDialog(model.getError()+"\n Press OK to re-login",UrlResponse.RESULT_ERR_SESSION_EXPIRED);
                        Intent intent = new Intent(context, ActivityLogin.class);
                        context.startActivity(intent);
                        AppCompatActivity activity = (AppCompatActivity) context;
                        activity.finish();
                    }
                    // break the process, session expired means other connection will be failed too
                    break;
                }
            } else {
                // Error in connection, not getting response from API, append the error message, because only 1 dialog will shown
                errorMsg += StringUtil.isEmpty(urlResponse.getErrorMessage()) ? "" : urlResponse.getErrorMessage()+"\n";
            }
            i++;
        }

        // has error, show error dialog
        if(!StringUtil.isEmpty(errorMsg)) {
            showErrorDialog(errorMsg,UrlResponse.RESULT_ERR_FATAL);
        }

        // Hide everything that shows progress UI
        if(this.progressDialog != null) {
            this.progressDialog.dismiss();
        }

        GlobalVariables gvar = GlobalVariables.getInstance();
        if(ActivityMain.loadingFrame!= null &&
                ActivityMain.loadingFrame.getVisibility() == View.VISIBLE && ActivityMain.loadingFrame.isShown()) {
            ActivityMain.loadingTextView.setText(this.context.getResources().getText(R.string.activity_loading));
            Animation fadeOut = gvar.getFadeOutAnim();
            fadeOut.setDuration(500);
            ActivityMain.loadingFrame.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    ActivityMain.loadingFrame.setVisibility(View.GONE);
                    // if listener defined , do callback
                    if(listener != null) {
                        listener.onAsyncTaskComplete(result, taskName);
                    }
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        } else {
            // if listener defined , do callback
            if(listener != null) {
                listener.onAsyncTaskComplete(result, this.taskName);
            }
        }

    }

    private void showErrorDialog(String message ,final int errorCode) {
        DialogBuilder builder = DialogBuilder.getInstance();
        builder.setDialogListener(new DialogListener() {
            @Override
            public void onDialogOkPressed(DialogInterface dialog, int which, Object... result) {
                if(errorCode == UrlResponse.RESULT_ERR_SESSION_EXPIRED) {
                    Intent intent = new Intent(context, ActivityLogin.class);
                    context.startActivity(intent);
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.finish();
                }
            }
            @Override
            public void onDialogCancelPressed(DialogInterface dialog, int which) {}
        });
        builder.createAlertDialog(this.context,"ERROR", message);
    }

    protected void initResultKey(UrlResponse urlResponse,UrlParam urlParam) {
        String result = urlResponse.getResultValue();

        if(result != null) {
            // if resultKey not defined , the Object will  store defaultKey
            String resultKey = DEFAULT_RESULT_KEY;
            if (!StringUtil.isEmpty(urlParam.getResultKey())) {
                resultKey = urlParam.getResultKey();
            }
            urlResponse.setResultKey(resultKey);
        }
    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public void setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
    }

    public Boolean getInLoginActivity() {
        return isInLoginActivity;
    }

    public void setInLoginActivity(Boolean inLoginActivity) {
        isInLoginActivity = inLoginActivity;
    }


    public TextView getProgressTextView() {
        return progressTextView;
    }

    public void setProgressTextView(TextView progressTextView) {
        this.progressTextView = progressTextView;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
