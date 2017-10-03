package id.co.myrepublic.salessupport.util;

import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.constant.AppConstant;

/**
 * Created by myrepublicid on 28/9/17.
 */

public class UrlParam {

    private String url;
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

    public static UrlParam createParamCheckSession(String sessionId) {
        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", sessionId);

        UrlParam urlParam = new UrlParam();
        urlParam.setUrl(AppConstant.CHECK_SESSION_URL);
        urlParam.setParamMap(paramMap);
        return urlParam;
    }
}
