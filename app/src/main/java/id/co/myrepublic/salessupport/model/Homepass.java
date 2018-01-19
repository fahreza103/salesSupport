package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Homepass / address list model
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Homepass implements Serializable {

    private static final long serialVersionUID = 6433135639323452675L;

    @JsonProperty("homepassdetailid")
    private String homepassDetailId;
    @JsonProperty("homepassmasterid")
    private String homepassMasterId;
    @JsonProperty("street")
    private String street;
    @JsonProperty("block")
    private String block;
    @JsonProperty("homenumber")
    private String homeNumber;
    @JsonProperty("rt")
    private String rt;
    @JsonProperty("rw")
    private String rw;
    @JsonProperty("fatcode")
    private String fatCode;
    @JsonProperty("fdtcode")
    private String fdtCode;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("rfsdate")
    private String rfsDate;
    @JsonProperty("condition")
    private String condition;
    @JsonProperty("picture")
    private String picture;
    @JsonProperty("class")
    private String homepassClass;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("isdeleted")
    private String isDeleted;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("ward")
    private String ward;
    @JsonProperty("postalcode")
    private String postalcode;
    @JsonProperty("district")
    private String district;
    @JsonProperty("clusterid")
    private String clusterId;
    @JsonProperty("homepassrelation")
    private String homepassRelation;
    @JsonProperty("type")
    private String type;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("oltcode")
    private String oltCode;
    @JsonProperty("bc_code")
    private String bcCode;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("homepass_source")
    private String homepassSource;
    @JsonProperty("commercial_name")
    private String commercialName;
    @JsonProperty("address_prefix")
    private String addressPrefix;
    @JsonProperty("address_suffix")
    private String addressSuffix;
    @JsonProperty("sub_address_prefix")
    private String subAddressPrefix;
    @JsonProperty("sub_address")
    private String subAddress;
    @JsonProperty("sub_address_suffix")
    private String subAddressSuffix;
    @JsonProperty("dwelling_type")
    private String dwellingType;
    @JsonProperty("building_type")
    private String buildingType;
    @JsonProperty("building_property_name")
    private String buildingPropertyName;
    @JsonProperty("building_property_location")
    private String buildingPropertyLocation;
    @JsonProperty("floor")
    private String floor;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("residential_service_ready")
    private Boolean residentialServiceReady;
    @JsonProperty("sme_service_ready")
    private Boolean smeServiceReady;
    @JsonProperty("enterprise_service_ready")
    private Boolean enterpriseServiceReady;
    @JsonProperty("installation")
    private String installation;
    @JsonProperty("availability")
    private Boolean availability;
    @JsonProperty("network_presence")
    private String networkPresence;
    @JsonProperty("wallplate_installation")
    private Boolean wallplateInstallation;
    @JsonProperty("complex")
    private String complex;
    @JsonProperty("clustername")
    private String clusterName;
    @JsonProperty("id")
    private String id;
    @JsonProperty("building_name")
    private String buildingName;
    @JsonProperty("main_address")
    private String mainAddress;

    @PositionItem(type= RowItem.MAINTEXT2)
    private String homepassAddressView;
    @PositionItem(type= RowItem.MAINTEXT1)
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHomepassDetailId() {
        return homepassDetailId;
    }

    public void setHomepassDetailId(String homepassDetailId) {
        this.homepassDetailId = homepassDetailId;
    }

    public String getHomepassMasterId() {
        return homepassMasterId;
    }

    public void setHomepassMasterId(String homepassMasterId) {
        this.homepassMasterId = homepassMasterId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getFatCode() {
        return fatCode;
    }

    public void setFatCode(String fatCode) {
        this.fatCode = fatCode;
    }

    public String getFdtCode() {
        return fdtCode;
    }

    public void setFdtCode(String fdtCode) {
        this.fdtCode = fdtCode;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRfsDate() {
        return rfsDate;
    }

    public void setRfsDate(String rfsDate) {
        this.rfsDate = rfsDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getHomepassClass() {
        return homepassClass;
    }

    public void setHomepassClass(String homepassClass) {
        this.homepassClass = homepassClass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getHomepassRelation() {
        return homepassRelation;
    }

    public void setHomepassRelation(String homepassRelation) {
        this.homepassRelation = homepassRelation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOltCode() {
        return oltCode;
    }

    public void setOltCode(String oltCode) {
        this.oltCode = oltCode;
    }

    public String getBcCode() {
        return bcCode;
    }

    public void setBcCode(String bcCode) {
        this.bcCode = bcCode;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getHomepassSource() {
        return homepassSource;
    }

    public void setHomepassSource(String homepassSource) {
        this.homepassSource = homepassSource;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getAddressPrefix() {
        return addressPrefix;
    }

    public void setAddressPrefix(String addressPrefix) {
        this.addressPrefix = addressPrefix;
    }

    public String getAddressSuffix() {
        return addressSuffix;
    }

    public void setAddressSuffix(String addressSuffix) {
        this.addressSuffix = addressSuffix;
    }

    public String getSubAddressPrefix() {
        return subAddressPrefix;
    }

    public void setSubAddressPrefix(String subAddressPrefix) {
        this.subAddressPrefix = subAddressPrefix;
    }

    public String getSubAddress() {
        return subAddress;
    }

    public void setSubAddress(String subAddress) {
        this.subAddress = subAddress;
    }

    public String getSubAddressSuffix() {
        return subAddressSuffix;
    }

    public void setSubAddressSuffix(String subAddressSuffix) {
        this.subAddressSuffix = subAddressSuffix;
    }

    public String getDwellingType() {
        return dwellingType;
    }

    public void setDwellingType(String dwellingType) {
        this.dwellingType = dwellingType;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public String getBuildingPropertyName() {
        return buildingPropertyName;
    }

    public void setBuildingPropertyName(String buildingPropertyName) {
        this.buildingPropertyName = buildingPropertyName;
    }

    public String getBuildingPropertyLocation() {
        return buildingPropertyLocation;
    }

    public void setBuildingPropertyLocation(String buildingPropertyLocation) {
        this.buildingPropertyLocation = buildingPropertyLocation;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getResidentialServiceReady() {
        return residentialServiceReady;
    }

    public void setResidentialServiceReady(Boolean residentialServiceReady) {
        this.residentialServiceReady = residentialServiceReady;
    }

    public Boolean getSmeServiceReady() {
        return smeServiceReady;
    }

    public void setSmeServiceReady(Boolean smeServiceReady) {
        this.smeServiceReady = smeServiceReady;
    }

    public Boolean getEnterpriseServiceReady() {
        return enterpriseServiceReady;
    }

    public void setEnterpriseServiceReady(Boolean enterpriseServiceReady) {
        this.enterpriseServiceReady = enterpriseServiceReady;
    }

    public String getInstallation() {
        return installation;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getNetworkPresence() {
        return networkPresence;
    }

    public void setNetworkPresence(String networkPresence) {
        this.networkPresence = networkPresence;
    }

    public Boolean getWallplateInstallation() {
        return wallplateInstallation;
    }

    public void setWallplateInstallation(Boolean wallplateInstallation) {
        this.wallplateInstallation = wallplateInstallation;
    }

    public String getComplex() {
        return complex;
    }

    public void setComplex(String complex) {
        this.complex = complex;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getHomepassAddressView() {
        return homepassAddressView;
    }

    public void setHomepassAddressView(String homepassAddressView) {
        this.homepassAddressView = homepassAddressView;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
