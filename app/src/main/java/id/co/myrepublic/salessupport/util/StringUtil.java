package id.co.myrepublic.salessupport.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by myrepublicid on 28/9/17.
 */

public class StringUtil {

    public static Map<String,String> extractCookie(String cookieStr) {
        Map<String,String> cookieMap = new HashMap<String,String>();
        try {
            String[] cookies = cookieStr.split(";");
            for(String cookie : cookies) {
                String[] keyValuePair = cookie.trim().split("=");
                cookieMap.put(keyValuePair[0],keyValuePair[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cookieMap;
    }
}
