package id.co.myrepublic.salessupport.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.ResponseUserSelect;

/**
 * Utility to connect this app to specified URL location, and return the response
 */

public class URLConnector {

    /**
     * Connect to specified URL
     * @param request
     * @param paramMap
     * @return
     */
    public static UrlResponse doConnect(String request, Map<Object,Object> paramMap) {
        UrlResponse urlResponse = new UrlResponse();
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
            conn.setConnectTimeout(40000);
            conn.setReadTimeout(40000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);

            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            Map<String, List<String>> headers = conn.getHeaderFields();
            List headerValues = conn.getHeaderFields().get("Content-Length");
            int length = conn.getContentLength();


            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            int code = conn.getResponseCode();
            System.out.println(code);

            conn.disconnect();

            urlResponse.setResultValue(stringBuilder.toString());
            urlResponse.setResultCode(UrlResponse.RESULT_SUCCESS);
            return urlResponse;
        } catch (SocketTimeoutException e) {
            urlResponse.setResultCode(UrlResponse.RESULT_ERR_TIMEOUT);
            urlResponse.setErrorMessage("Connection Time out, server not responding");
            e.printStackTrace();
        } catch (Exception e) {
            urlResponse.setResultCode(UrlResponse.RESULT_ERR_FATAL);
            urlResponse.setErrorMessage("Connection error, please check your internet connection");
            e.printStackTrace();
        }

        return urlResponse;
    }

    public static void main (String[] args) {
//        //String URL = "https://boss-st.myrepublic.co.id/api/auth/check_session";
//        String URL = "https://boss-st.myrepublic.co.id/api/user/select";
//        Map<Object,Object> paramMap = new HashMap<>();
//
//        paramMap.put("session_id","3614ca48-794a-4ddc-9aa2-3052f18cf76b");
//        //paramMap.put("cluster_name","AMERICA");
//        paramMap.put("user_id","168489");
//
//        String json = URLConnector.doConnect(URL, paramMap);
//        MainModel<ResponseUserSelect> model = StringUtil.convertStringToObject(json,ResponseUserSelect.class);
//        ResponseUserSelect sr =  model.getObject();

    }

}
