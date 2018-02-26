package id.co.myrepublic.salessupport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Catalog Response API
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 2334565678487532333L;

    private List<CatalogItem> internetItems;
    private List<CatalogItem> tvItems;
    private List<CatalogItem> vasItems;
    private List<CatalogItem> internetAddonItems;
    private List<CatalogItem> stbItems;
    private List<CatalogItem> ontItems;
    private List<CatalogItem> routerItems;
    private List<CatalogItem> promotions;
    @JsonProperty("total")
    private int total;
    @JsonProperty("data")
    private List<CatalogItem> data;

    public List<CatalogItem> getInternetItems() {
        return internetItems;
    }

    public void setInternetItems(List<CatalogItem> internetItems) {
        this.internetItems = internetItems;
    }

    public List<CatalogItem> getTvItems() {
        return tvItems;
    }

    public void setTvItems(List<CatalogItem> tvItems) {
        this.tvItems = tvItems;
    }

    public List<CatalogItem> getVasItems() {
        return vasItems;
    }

    public void setVasItems(List<CatalogItem> vasItems) {
        this.vasItems = vasItems;
    }

    public List<CatalogItem> getStbItems() {
        return stbItems;
    }

    public void setStbItems(List<CatalogItem> stbItems) {
        this.stbItems = stbItems;
    }

    public List<CatalogItem> getOntItems() {
        return ontItems;
    }

    public void setOntItems(List<CatalogItem> ontItems) {
        this.ontItems = ontItems;
    }

    public List<CatalogItem> getRouterItems() {
        return routerItems;
    }

    public void setRouterItems(List<CatalogItem> routerItems) {
        this.routerItems = routerItems;
    }

    public List<CatalogItem> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<CatalogItem> promotions) {
        this.promotions = promotions;
    }

    public List<CatalogItem> getInternetAddonItems() {
        return internetAddonItems;
    }

    public void setInternetAddonItems(List<CatalogItem> internetAddonItems) {
        this.internetAddonItems = internetAddonItems;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CatalogItem> getData() {
        return data;
    }

    public void setData(List<CatalogItem> data) {
        this.data = data;
    }
}
