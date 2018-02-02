package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Cluster info response API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterDetail<T> implements Serializable {

    private static final long serialVersionUID = 7657663563933224754L;

    @JsonProperty("competitor_list")
    private List<String> competitorList;
    @JsonProperty("cluster")
    private Map cluster;

    public List<String> getCompetitorList() {
        return competitorList;
    }

    public void setCompetitorList(List<String> competitorList) {
        this.competitorList = competitorList;
    }

    public Map getCluster() {
        return cluster;
    }

    public void setCluster(Map cluster) {
        this.cluster = cluster;
    }
}
