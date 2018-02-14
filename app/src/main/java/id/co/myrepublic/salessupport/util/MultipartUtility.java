package id.co.myrepublic.salessupport.util;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import id.co.myrepublic.salessupport.listener.ProgressListener;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 *
 */
public class MultipartUtility {

    private ProgressListener progressListener;
    private MultipartEntityBuilder entityBuilder;
    private String url;
    private String cookieStr;
    private int timeout;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String cookieStr,int timeout)
            throws IOException {
        this.url = requestURL;
        this.cookieStr = cookieStr;
        this.timeout = timeout;
        entityBuilder = MultipartEntityBuilder.create();
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        entityBuilder.addPart(name, new StringBody(value, ContentType.MULTIPART_FORM_DATA));
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param requestFile a File to be processed
     * @throws IOException
     */
    public void addFilePart(String fieldName, File requestFile)
            throws IOException {
        entityBuilder.addPart(fieldName, new FileBody(requestFile));
    }



    public RequestConfig requestConfigWithTimeout(int timeoutInMilliseconds) {
        return RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(timeoutInMilliseconds)
                .setConnectTimeout(timeoutInMilliseconds)
                .setConnectionRequestTimeout(timeoutInMilliseconds)
                .build();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String doConnect() throws IOException {
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient = builder.build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfigWithTimeout(timeout));

        HttpEntity entity = null;
        if(progressListener == null) {
            entity = entityBuilder.build();
        } else {
            ProgressHttpEntityWrapper.ProgressCallback  progressCallback = new ProgressHttpEntityWrapper.ProgressCallback() {
                @Override
                public void progress(float progress) {
                    progressListener.onAsyncProgressUpdate((int) progress);
                }

            };
            entity = new ProgressHttpEntityWrapper(entityBuilder.build(), progressCallback);
        }
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);

        // get the result
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity, "UTF-8");

        return responseString;
    }

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }
}