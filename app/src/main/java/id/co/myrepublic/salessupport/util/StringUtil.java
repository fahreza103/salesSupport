package id.co.myrepublic.salessupport.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.model.MainModel;

/**
 * Util class for String operations
 */
public class StringUtil {

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

    /**
     * get cookies value from string, currently the format is
     * value=xxx;value2=xxx
     * @param cookieStr
     * @return Map based on cookie string
     */
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

    /**
     * using jackson, convert json string into java object
     * @param jsonString, must be json format string
     * @param clazz, if response is array, please defined the class with array type, example Cluster[].class
     * @return java object
     */
    public static MainModel convertStringToObject (String jsonString, Class<?> clazz) {
        if (jsonString == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory t = TypeFactory.defaultInstance();
            MainModel model = mapper.readValue(jsonString,MainModel.class);
            if(model != null && clazz != null) {
                if(model.getResponse() instanceof ArrayNode) {
                    Object[] response = (Object[])mapper.convertValue(model.getResponse(), clazz);
                    model.setListObject(Arrays.asList(response));
                } else {
                    Object response = mapper.convertValue(model.getResponse(), clazz);
                    model.setObject(response);
                }
            }

            System.out.print(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Using jackson , convert java obj to json string
     * @param obj
     * @return String represent the object
     */
    public static String convertObjectToString (Object obj) {
        String jsonInString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }
}
