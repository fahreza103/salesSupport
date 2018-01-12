package id.co.myrepublic.salessupport.support;

import id.co.myrepublic.salessupport.util.URLConnector;
import id.co.myrepublic.salessupport.util.UrlParam;

/**
 * Download operation with async operation
 */

public class DownloadAsyncOperation extends AbstractAsyncOperation {

    public DownloadAsyncOperation(String taskName) {
        this.taskName = taskName;
    }

    protected String doInBackground(Object... urlParams) {
        UrlParam urlParam = (UrlParam) urlParams[0];
        String result = URLConnector.doConnect(urlParam.getUrl(),urlParam.getParamMap());

        return result;
    }
}
