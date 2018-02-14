package id.co.myrepublic.salessupport.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import id.co.myrepublic.salessupport.listener.ProgressListener;
import id.co.myrepublic.salessupport.model.MainModel;
import id.co.myrepublic.salessupport.model.Order;

/**
 * Utility to connect this app to specified URL location, and return the response
 */

public class URLConnector {

    private static final int TIMEOUT_DURATION = 40000;

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
            conn.setConnectTimeout(TIMEOUT_DURATION);
            conn.setReadTimeout(TIMEOUT_DURATION);
            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(postData.length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            //conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);

            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));

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
            if(StringUtil.isEmpty(e.getMessage())) {
                urlResponse.setErrorMessage("Server returned non-OK status or connection failure");
            } else {
                urlResponse.setErrorMessage(e.getMessage());
            }

            e.printStackTrace();
        }

        return urlResponse;
    }

    public static UrlResponse doConnectMultipart(String request, Map<Object,Object> paramMap, File requestFile,String cookie, ProgressListener progressListener) {
        UrlResponse urlResponse = new UrlResponse();
        try {
            MultipartUtility multipart = new MultipartUtility(request,cookie, TIMEOUT_DURATION);
            multipart.setProgressListener(progressListener);
            multipart.addFilePart("file",requestFile);
            //Add form value
            for (Map.Entry<Object, Object> entry : paramMap.entrySet()) {
                multipart.addFormField(entry.getKey().toString(), ""+entry.getValue());
            }

            String serverResponse = multipart.doConnect();

            urlResponse.setResultValue(serverResponse);
            urlResponse.setResultCode(UrlResponse.RESULT_SUCCESS);

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            urlResponse.setResultCode(UrlResponse.RESULT_ERR_TIMEOUT);
            urlResponse.setErrorMessage("Connection Time out, server not responding");
        } catch (Exception e) {
            e.printStackTrace();
            urlResponse.setResultCode(UrlResponse.RESULT_ERR_FATAL);
            urlResponse.setErrorMessage("Server returned non-OK status or connection failure");
        }

        return urlResponse;
    }

    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }

    public static void main (String[] args) {
        File file = new File("D:\\Design\\wall.jpg");
        byte[] data = null;
        String encoded = null;
        try {
            data = FileUtils.readFileToByteArray(file);
             encoded = FileUtils.readFileToString(file);
            //data =compress(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String URL = "https://boss-st.myrepublic.co.id/api/order/upload_file";
        //String URL = AppConstant.UPLOAD_ORDER_DOCUMENT_API_URL+"/167805/1";
        Map<Object,Object> paramMap = new HashMap<Object,Object>();
        paramMap.put("session_id", "1d52ffe7-1def-4f19-a324-35c6131b9ab3");
        paramMap.put("document[name]", "wall.jpg");
        paramMap.put("document[document_type_id]", "1");
        paramMap.put("document[size]", file.length());
        //paramMap.put("document[contents]", Base64.);
        paramMap.put("order_id", "167804");


//        paramMap.put("rep_id", "1");
//        paramMap.put("event_rep_id", "test");
//        paramMap.put("homepassdetailid", "CBR.100.0501.LWI.ARCO.019.00082");
//        paramMap.put("customer[type]", "RES");
//        paramMap.put("customer[referrer_customer_id]","test");
//
//        // CUSTOMER-PROFILE
//        paramMap.put("customer[users][0][contact_type_id]", "1");
//        paramMap.put("customer[users][0][salutation]", "Mr");
//        paramMap.put("customer[users][0][first_name]", "Fahreza");
//        paramMap.put("customer[users][0][last_name]", "Tamara");
//        paramMap.put("customer[users][0][gender]", "M");
//        paramMap.put("customer[users][0][nric]", "trs");
//        paramMap.put("customer[users][0][date_of_birth]", "1990-08-07");
//        paramMap.put("customer[users][0][nationality]", "IDN");
//        paramMap.put("customer[users][0][work_phone]", "0218764444");
//        paramMap.put("customer[users][0][home_phone]", "0218764444");
//        paramMap.put("customer[users][0][mobile]", "081584142002");
//        paramMap.put("customer[users][0][email]", "animers103@gmail.com");
//
//        // COMPANY-INFO
//        paramMap.put("customer[company_info][contacts][0][contact_type_id]", "1");
//        paramMap.put("customer[company_info][contacts][0][salutation]", "Mr");
//        paramMap.put("customer[company_info][contacts][0][first_name]", "Fahreza");
//        paramMap.put("customer[company_info][contacts][0][last_name]", "Tamara");
//        paramMap.put("customer[company_info][contacts][0][gender]", "M");
//        paramMap.put("customer[company_info][contacts][0][nric]", "trs");
//        paramMap.put("customer[company_info][contacts][0][date_of_birth]", "1990-08-07");
//        paramMap.put("customer[company_info][contacts][0][office_phone]", "0218764444");
//        paramMap.put("customer[company_info][contacts][0][mobile]", "081584142002");
//        paramMap.put("customer[company_info][contacts][0][email]", "animers103@gmail.com");
//
//
//        paramMap.put("customer[payment_info][payment_type_id]", "15");
//        paramMap.put("customer[payment_info][status_id]", "4");
//        paramMap.put("customer[company_info][business_type]", "SMG");
//        paramMap.put("customer[company_info][business_segment]", "HRB");
//
//        // BILLING-ADDRESS
//        paramMap.put("subscription[service_type]", "RES");
//        paramMap.put("subscription[tp_status]", "30");
//        paramMap.put("subscription[net_co]", "ON");
//        paramMap.put("subscription[addresses][0][type]", "Billing");
//        paramMap.put("subscription[addresses][0][province]", "DKI Jakarta");
//        paramMap.put("subscription[addresses][0][city]", "101");
//        paramMap.put("subscription[addresses][0][developer_cluster]", "tes");
//        paramMap.put("subscription[addresses][0][dwelling_type]", "");
//        paramMap.put("subscription[addresses][0][block]", "");
//        paramMap.put("subscription[addresses][0][home_number]", "33");
//        paramMap.put("subscription[addresses][0][street]", null);
//        paramMap.put("subscription[addresses][0][floor]", "");
//        paramMap.put("subscription[addresses][0][unit]", "");
//        paramMap.put("subscription[addresses][0][village]", null);
//        paramMap.put("subscription[addresses][0][postal_code]", "32322");
//        paramMap.put("subscription[addresses][0][rw]", "");
//        paramMap.put("subscription[addresses][0][rt]", "");
//        paramMap.put("subscription[addresses][0][address_prefix]","Desa");
//        paramMap.put("subscription[addresses][0][building_ready]", false);
//        paramMap.put("subscription[addresses][0][building_name]", "");
//        paramMap.put("subscription[addresses][0][developer_complex]", "");
//        paramMap.put("subscription[addresses][0][developer_sector]", "");
//        paramMap.put("subscription[addresses][0][homepassdetailid]", "CBR.100.0501.LWI.ARCO.019.00082");
//        paramMap.put("subscription[addresses][0][country_code]", "IDN");
//
//        // SERVICE-ADDRESS
//        paramMap.put("subscription[addresses][1][type]", "Service");
//        paramMap.put("subscription[addresses][1][province]", "DKI Jakarta");
//        paramMap.put("subscription[addresses][1][city]", "Jakarta");
//        paramMap.put("subscription[addresses][1][developer_cluster]", "ACROPOLIS SELATAN");
//        paramMap.put("subscription[addresses][1][dwelling_type]", "SDU");
//        paramMap.put("subscription[addresses][1][block]", "CA");
//        paramMap.put("subscription[addresses][1][home_number]", "15");
//        paramMap.put("subscription[addresses][1][street]", "ACROPOLIS SELATAN");
//        paramMap.put("subscription[addresses][1][floor]", null);
//        paramMap.put("subscription[addresses][1][unit]", null);
//        //paramMap.put("subscription[addresses][0][village]", homepassDataService.get);
//        paramMap.put("subscription[addresses][1][postal_code]", "16968");
//        paramMap.put("subscription[addresses][1][rw]", "-");
//        paramMap.put("subscription[addresses][1][rt]", "-");
//        paramMap.put("subscription[addresses][1][address_prefix]", null);
//        paramMap.put("subscription[addresses][1][building_ready]", true);
//        paramMap.put("subscription[addresses][1][building_name]", null);
//        paramMap.put("subscription[addresses][1][developer_complex]", "LEGENDA WISATA");
//        //paramMap.put("subscription[addresses][0][developer_sector]", homepassDataService.get);
//        paramMap.put("subscription[addresses][1][homepassdetailid]", "CBR.100.0501.LWI.ARCO.019.00082");
//        paramMap.put("subscription[addresses][1][country_code]", "IDN");
//
//
//        paramMap.put("order[type]", "New Order");
//        paramMap.put("order[order_date]", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        paramMap.put("order[channel_id]", "1");
//        paramMap.put("order[promo_code]", null);
//
//        paramMap.put("order[details][0][item_id]", "21243");
//        paramMap.put("order[details][0][quantity]", "1");
//        paramMap.put("order[details][1][item_id]", "20739");
//        paramMap.put("order[details][1][quantity]", "1");


//        UrlResponse response = URLConnector.doConnect(URL, paramMap);
        UrlResponse response = URLConnector.doConnect(URL,paramMap);
        MainModel<Order> model = StringUtil.convertStringToObject(response.getResultValue(),Order.class);
        Order order =model.getObject();

    }

}





