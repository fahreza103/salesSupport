package id.co.myrepublic.salessupport.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Created by myrepublicid on 26/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cluster implements Serializable {

    private static final long serialVersionUID = 93655123454912675L;

    private String cityName;
    @JsonProperty("total_homepass")
    @PositionItem(type= RowItem.SUBTEXT1, prefix = "Total Homepass : ")
    private Integer totalHomePass;
    @JsonProperty("clustername")
    @PositionItem(type= RowItem.MAINTEXT1)
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
