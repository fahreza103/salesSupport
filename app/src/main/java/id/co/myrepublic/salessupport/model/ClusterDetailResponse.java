package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by myrepublicid on 29/9/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClusterDetailResponse<T>  {

    @JsonProperty("competitor_list")
    private List<String> competitorList;
    @JsonProperty("cluster")
    private List<T> cluster;

    public List<String> getCompetitorList() {
        return competitorList;
    }

    public void setCompetitorList(List<String> competitorList) {
        this.competitorList = competitorList;
    }

    public List<T> getCluster() {
        return cluster;
    }

    public void setCluster(List<T> cluster) {
        this.cluster = cluster;
    }
}
