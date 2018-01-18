package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Model for all response from API
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainModel<T> implements Serializable {

    private static final long serialVersionUID = 4560135639324912675L;

    @JsonProperty("action")
    private String action;

    @JsonProperty("error")
    private String error;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("timestamp")
    private String timestamp;

    /**
     * using JsonNode here because response could be any object, array or single
     */
    @JsonProperty("response")
    private JsonNode response;

    /**
     * if response is a single object, the value will be set here
     */
    @JsonIgnoreProperties
    private T object;

    /**
     * if response is an array, the value will be set here
     */
    @JsonIgnoreProperties
    private List<T> listObject = new ArrayList<T>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public JsonNode getResponse() {
        return response;
    }

    public void setResponse(JsonNode response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<T> getListObject() {
        return listObject;
    }

    public void setListObject(List<T> listObject) {
        this.listObject = listObject;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
