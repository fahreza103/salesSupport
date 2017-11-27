package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by myrepublicid on 26/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class City  implements Serializable {

    private static final long serialVersionUID = 413655339324912675L;

    @JsonProperty("cityid")
    private String cityId;
    @JsonProperty("stateid")
    private String stateId;
    @JsonProperty("systemgenerated")
    private String systemGenerated;
    @JsonProperty("name")
    private String cityName;
    @JsonIgnoreProperties
    private String arpu;
    @JsonIgnoreProperties
    private String activeSubs;

//    public City(String cityName, String arpu, String activeSubs) {
//        this.cityName = cityName;
//        this.arpu = arpu;
//        this.activeSubs = activeSubs;
//    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getSystemGenerated() {
        return systemGenerated;
    }

    public void setSystemGenerated(String systemGenerated) {
        this.systemGenerated = systemGenerated;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getArpu() {
        return arpu;
    }

    public void setArpu(String arpu) {
        this.arpu = arpu;
    }

    public String getActiveSubs() {
        return activeSubs;
    }

    public void setActiveSubs(String activeSubs) {
        this.activeSubs = activeSubs;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

//    public static List<City> getDataDummy() {
//        List<City> listCity = new ArrayList<City>();
//        listCity.add(new City("Jakarta","8343.333","4322"));
//        listCity.add(new City("Bekasi","8455.354","3543"));
//        listCity.add(new City("Tanggerang","8222.655","5465"));
//        listCity.add(new City("Depok","8993.893","7654"));
//        listCity.add(new City("Bogor","9033.544","2342"));
//        listCity.add(new City("Bandung","9222.433","2343"));
//        listCity.add(new City("Subang","8453.100","5454"));
//        listCity.add(new City("Cilegon","4324.200","7655"));
//        listCity.add(new City("Sukabumi","6565.300","1243"));
//        listCity.add(new City("Cirebon","9453.400","6565"));
//        listCity.add(new City("Purwakarta","1232.500","7665"));
//        listCity.add(new City("Probolinggo","5432.600","2343"));
//        listCity.add(new City("Semarang","2233.700","6456"));
//        listCity.add(new City("Yogyakarta","1257.800","4565"));
//        listCity.add(new City("Solo","5465.900","7657"));
//        listCity.add(new City("Purbalingga","7666.101","1234"));
//        listCity.add(new City("Surabaya","8756.201","7656"));
//        listCity.add(new City("Pontianak","6577.301","4544"));
//        listCity.add(new City("Samarinda","3454.401","3454"));
//
//        return listCity;
//    }
}
