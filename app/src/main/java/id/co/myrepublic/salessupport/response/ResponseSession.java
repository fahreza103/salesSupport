package id.co.myrepublic.salessupport.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Model in response from check_session API
 */
public class ResponseSession implements Serializable {

    private static final long serialVersionUID = 3322135639324356754L;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("expiry_date")
    private String expiryDate;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
