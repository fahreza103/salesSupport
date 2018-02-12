package id.co.myrepublic.salessupport.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.constant.AppConstant;

/**
 * Url Parameter for requesting API
 *
 * @author Fahreza Tamara
 */

public class UrlParam {

    private String url;
    private String resultKey;
    private File file;
    private Map<Object,Object> paramMap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<Object, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<Object, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static UrlParam createParamCheckSession(String sessionId) {
        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.CHECK_SESSION_URL);
        urlParam.setParamMap(paramMap);
        return urlParam;
    }
}
