package id.co.myrepublic.salessupport.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import id.co.myrepublic.salessupport.constant.AppConstant;

/**
 * Created by myrepublicid on 27/9/17.
 */

public class URLConnector {

    public static String doConnect(String request, Map<Object,Object> paramMap) {
        String urlParameters = "";
        int i = 0;
        for (Map.Entry<Object, Object> entry : paramMap.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if(i>0) {
                urlParameters += "&";
            }
            urlParameters += key + "=" + value;
            i++;
        }

        try {
            byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
            int postDataLength = postData.length;

            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            int code =conn.getResponseCode();
            System.out.println(code);

            conn.disconnect();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main (String[] args) {
        String URL = AppConstant.GET_CLUSTERDETAIL_API_URL;
        Map<Object,Object> paramMap = new HashMap<>();

        paramMap.put("session_id","59d93d13-c91b-4f57-82eb-7b5a9482bf9d");
        paramMap.put("cluster_name","AMERICA");

        String json = URLConnector.doConnect(URL, paramMap);
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeFactory t = TypeFactory.defaultInstance();
            Object model = mapper.readValue(json,Object.class);
            System.out.print(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
