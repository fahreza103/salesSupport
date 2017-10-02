package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by myrepublicid on 26/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cluster {

    private String cityName;
    @JsonProperty("total_homepass")
    private Integer totalHomePass;
    @JsonProperty("clustername")
    private String clusterName;
    private String arpu;
    private String totalSubs;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getArpu() {
        return arpu;
    }

    public void setArpu(String arpu) {
        this.arpu = arpu;
    }

    public String getTotalSubs() {
        return totalSubs;
    }

    public void setTotalSubs(String totalSubs) {
        this.totalSubs = totalSubs;
    }

    public Integer getTotalHomePass() {
        return totalHomePass;
    }

    public void setTotalHomePass(Integer totalHomePass) {
        this.totalHomePass = totalHomePass;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
