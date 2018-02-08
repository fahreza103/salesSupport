package id.co.myrepublic.salessupport.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Model from get_dealer_info response
 */

public class Dealer implements Serializable {

    private static final long serialVersionUID = 3456893432642791674L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("company_name")
    private String companyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
