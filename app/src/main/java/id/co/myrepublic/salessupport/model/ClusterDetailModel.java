package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by myrepublicid on 29/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterDetailModel {

    @JsonProperty("action")
    private String action;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("response")
    private ClusterDetailResponse response;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ClusterDetailResponse getResponse() {
        return response;
    }

    public void setResponse(ClusterDetailResponse response) {
        this.response = response;
    }
}
