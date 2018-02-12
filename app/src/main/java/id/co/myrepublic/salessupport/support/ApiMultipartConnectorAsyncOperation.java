package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.listener.ProgressListener;
import id.co.myrepublic.salessupport.util.URLConnector;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.util.UrlResponse;

/**
 * Request API operation with async, using multipart for file request
 *
 * @author Fahreza Tamara
 */

public class ApiMultipartConnectorAsyncOperation  extends AbstractAsyncOperation implements ProgressListener {

    private String cookie;
    private TextView progressTextView;

    public ApiMultipartConnectorAsyncOperation(Context context, String taskName, AsyncUiDisplayType uiType) {
        this.uiType = uiType;
        this.taskName = taskName;
        this.context  = context;
    }

    protected List<UrlResponse> doInBackground(Object... objects) {
        List<UrlResponse> responseList = new ArrayList<UrlResponse>();
        for(Object object : objects) {
            UrlParam urlParam = (UrlParam) object;
            UrlResponse urlResponse = URLConnector.doConnectMultipart(urlParam.getUrl(),urlParam.getParamMap(),urlParam.getFile(),cookie,this);
            initResultKey(urlResponse,urlParam);
            responseList.add(urlResponse);
            if(isCancelled()) {
                break;
            }

        }

        return responseList;
    }

    @Override
    public void onAsyncProgressUpdate(Object progress) {
        if(progress instanceof Integer) {
            publishProgress((Integer)progress);
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        if(progressTextView != null && progress.length > 0) {
            String text = this.context.getResources().getText(R.string.status_progress)+" ("+progress[0]+"%)";
            progressTextView.setText(text);
        }
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
