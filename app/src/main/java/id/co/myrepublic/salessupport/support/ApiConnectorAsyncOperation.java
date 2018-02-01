package id.co.myrepublic.salessupport.support;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.URLConnector;
import id.co.myrepublic.salessupport.util.UrlParam;
import id.co.myrepublic.salessupport.util.UrlResponse;

/**
 * Download operation with async operation
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
            String result = urlResponse.getResultValue();

            if(result != null) {
                // if resultKey not defined , the Object will only store defaultKey
                String resultKey = DEFAULT_RESULT_KEY;
                if (!StringUtil.isEmpty(urlParam.getResultKey())) {
                    resultKey = urlParam.getResultKey();
                }
                urlResponse.setResultKey(resultKey);
            }
            responseList.add(urlResponse);
            if(isCancelled()) {
                break;
            }

        }

        return responseList;
    }
}
