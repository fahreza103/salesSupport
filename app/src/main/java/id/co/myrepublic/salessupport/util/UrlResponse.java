package id.co.myrepublic.salessupport.util;

/**
 * Response object API, contain result string, status, and error message
 *
 * @author Fahreza Tamara
 */

public class UrlResponse {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_ERR_FATAL = -1;
    public static final int RESULT_ERR_TIMEOUT = 2;
    public static final int RESULT_ERR_SESSION_EXPIRED = 3;

    private String resultKey;
    private String resultValue;
    private int resultCode;
    private String errorMessage;

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }
}
