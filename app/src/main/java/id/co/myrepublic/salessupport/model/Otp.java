package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Model for OTP response
 */

public class Otp implements Serializable {

    private static final long serialVersionUID = 938713564337791267L;

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("otp")
    private String otp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
