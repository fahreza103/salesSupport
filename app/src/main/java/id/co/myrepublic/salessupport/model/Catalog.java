package id.co.myrepublic.salessupport.model;

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

    @JsonProperty("internet_items")
    private List<CatalogItem> internetItems;
    @JsonProperty("tv_items")
    private List<CatalogItem> tvItems;
    @JsonProperty("vas_items")
    private List<CatalogItem> vasItems;
    @JsonProperty("stb_items")
    private List<CatalogItem> stbItems;
    @JsonProperty("ont_items")
    private List<CatalogItem> ontItems;
    @JsonProperty("router_items")
    private List<CatalogItem> routerItems;
    @JsonProperty("promotions")
    private List<CatalogItem> promotions;

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
}
