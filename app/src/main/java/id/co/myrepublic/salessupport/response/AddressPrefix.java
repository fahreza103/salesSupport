package id.co.myrepublic.salessupport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Model from address prefix API response
 *
 * @author Fahreza Tamara
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressPrefix implements Serializable {

    private static final long serialVersionUID = 764413456187354225L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("address_prefix")
    private String addressPrefix;
    @JsonProperty("address_prefix_name")
    @PositionItem(type=RowItem.MAINTEXT1)
    private String addressPrefixName;
    @JsonProperty("remarks")
    private String remarks;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("modified_date")
    private String modifiedDate;
    @JsonProperty("modified_by")
    private String modifiedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressPrefix() {
        return addressPrefix;
    }

    public void setAddressPrefix(String addressPrefix) {
        this.addressPrefix = addressPrefix;
    }

    public String getAddressPrefixName() {
        return addressPrefixName;
    }

    public void setAddressPrefixName(String addressPrefixName) {
        this.addressPrefixName = addressPrefixName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
