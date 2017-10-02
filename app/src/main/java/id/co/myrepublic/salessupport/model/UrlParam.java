package id.co.myrepublic.salessupport.model;

import java.util.Map;

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
}
