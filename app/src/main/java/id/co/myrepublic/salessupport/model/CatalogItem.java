package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Catalog Internet Items Response API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogItem implements Serializable {

    private static final long serialVersionUID = 5321563345449153333L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("name")
    @PositionItem(type = RowItem.MAINTEXT1)
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price_amount")
    private String priceAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }
}
