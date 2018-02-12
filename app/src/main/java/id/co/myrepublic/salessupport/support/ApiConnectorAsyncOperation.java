package id.co.myrepublic.salessupport.support;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.util.URLConnector;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.util.UrlResponse;

/**
 * Request operation with async operation, using form data encoded
 *
 * @author Fahreza Tamara
 */

public class ApiConnectorAsyncOperation extends AbstractAsyncOperation {


    public ApiConnectorAsyncOperation(Context context, String taskName, AsyncUiDisplayType uiType) {
        this.uiType = uiType;
        this.taskName = taskName;
        this.context  = context;
    }

    protected List<UrlResponse> doInBackground(Object... objects) {
        List<UrlResponse> responseList = new ArrayList<UrlResponse>();
        for(Object object : objects) {
            UrlParam urlParam = (UrlParam) object;
            UrlResponse urlResponse = URLConnector.doConnect(urlParam.getUrl(),urlParam.getParamMap());
            initResultKey(urlResponse,urlParam);
            responseList.add(urlResponse);
            if(isCancelled()) {
                break;
            }

        }

        return responseList;
    }
}
