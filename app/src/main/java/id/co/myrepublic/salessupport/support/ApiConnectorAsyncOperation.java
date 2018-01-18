package id.co.myrepublic.salessupport.support;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.constant.AsyncUiDisplayType;
import id.co.myrepublic.salessupport.util.StringUtil;
import id.co.myrepublic.salessupport.util.URLConnector;
import id.co.myrepublic.salessupport.util.UrlParam;

/**
 * Download operation with async operation
 */

public class ApiConnectorAsyncOperation extends AbstractAsyncOperation {

    public ApiConnectorAsyncOperation(String taskName, AsyncUiDisplayType uiType) {
        this.uiType = uiType;
        this.taskName = taskName;
    }

    public ApiConnectorAsyncOperation(Context context, String taskName, AsyncUiDisplayType uiType) {
        this.uiType = uiType;
        this.taskName = taskName;
        this.context  = context;
    }

    protected Map<String,String> doInBackground(Object... objects) {
        Map<String,String> resultMap = new HashMap<String,String>();
        for(Object object : objects) {
            UrlParam urlParam = (UrlParam) object;
            String result = URLConnector.doConnect(urlParam.getUrl(),urlParam.getParamMap());

            // if resultKey not defined , the map will only store defaultKey, so only one size will be stored no matter how much you supply the URLParam
            if(result != null) {
                if (!StringUtil.isEmpty(urlParam.getResultKey())) {
                    resultMap.put(urlParam.getResultKey(), result);
                } else {
                    resultMap.put(DEFAULT_RESULT_KEY, result);
                }
            }
        }

        return resultMap;
    }
}
