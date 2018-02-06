package id.co.myrepublic.salessupport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import id.co.myrepublic.salessupport.annotation.PositionItem;
import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Model for Area
 */

public class Area implements Serializable {

    private static final long serialVersionUID = 6557135639443912673L;

    private String areaId;
    @PositionItem(type= RowItem.SUBTEXT1)
    private String areaCode;
    @PositionItem(type= RowItem.MAINTEXT1)
    private String areaName;
    private String remark;
    private String areainitial;
    private String sequence;
    private String ottareaid;
    private String areaCategory;
    private String enterpriseareacode;
    private String provinceId;
    private String provinceCode;

    public String getAreaId() {
        return areaId;
    }

    public Area setAreaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public Area setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getAreaName() {
        return areaName;
    }

    public Area setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Area setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getAreainitial() {
        return areainitial;
    }

    public Area setAreainitial(String areainitial) {
        this.areainitial = areainitial;
        return this;
    }

    public String getSequence() {
        return sequence;
    }

    public Area setSequence(String sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getOttareaid() {
        return ottareaid;
    }

    public Area setOttareaid(String ottareaid) {
        this.ottareaid = ottareaid;
        return this;
    }

    public String getAreaCategory() {
        return areaCategory;
    }

    public Area setAreaCategory(String areaCategory) {
        this.areaCategory = areaCategory;
        return this;
    }

    public String getEnterpriseareacode() {
        return enterpriseareacode;
    }

    public Area setEnterpriseareacode(String enterpriseareacode) {
        this.enterpriseareacode = enterpriseareacode;
        return this;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public Area setProvinceId(String provinceId) {
        this.provinceId = provinceId;
        return this;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public Area setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
        return this;
    }

    public static List<Area> createDataDummy() {
        List<Area> areaList = new ArrayList<Area>();
        Area area1 = new Area();area1.setAreaId("ARC000000022").setAreaCode("101").setAreaName("Cibubur").setProvinceId("STA0001").setProvinceCode("DKI Jakarta");
        Area area2 = new Area();area2.setAreaId("ARC000000023").setAreaCode("102").setAreaName("BSD").setProvinceId("STA0010").setProvinceCode("Banten");
        Area area3 = new Area();area3.setAreaId("ARC000000024").setAreaCode("103").setAreaName("Depok").setProvinceId("STA0002").setProvinceCode("Jawa Barat");
        Area area4 = new Area();area4.setAreaId("ARC000000025").setAreaCode("104").setAreaName("Bekasi").setProvinceId("STA0002").setProvinceCode("Jawa Barat");
        Area area5 = new Area();area5.setAreaId("ARC000000026").setAreaCode("105").setAreaName("Surabaya").setProvinceId("STA0005").setProvinceCode("Jawa Timur");
        Area area6 = new Area();area6.setAreaId("ARC000000028").setAreaCode("106").setAreaName("Tangerang").setProvinceId("STA0010").setProvinceCode("Banten");
        Area area7 = new Area();area7.setAreaId("ARC000000030").setAreaCode("107").setAreaName("Malang").setProvinceId("STA0005").setProvinceCode("Jawa Timur");
        Area area8 = new Area();area8.setAreaId("ARC000000031").setAreaCode("108").setAreaName("Semarang").setProvinceId("STA0004").setProvinceCode("Jawa Tengah");
        Area area9 = new Area();area9.setAreaId("ARC000000032").setAreaCode("109").setAreaName("Bogor").setProvinceId("STA0002").setProvinceCode("Jawa Barat");
        Area area10 = new Area();area10.setAreaId("ARC000000033").setAreaCode("110").setAreaName("Palembang").setProvinceId("STA0012").setProvinceCode("Sumatera Selatan");
        Area area11 = new Area();area11.setAreaId("ARC000000035").setAreaCode("111").setAreaName("Jakarta").setProvinceId("STA0001").setProvinceCode("DKI Jakarta");
        Area area12 = new Area();area12.setAreaId("ARC000000036").setAreaCode("112").setAreaName("Medan").setProvinceId("STA0009").setProvinceCode("Sumatera Utara");

        areaList.add(area1);areaList.add(area2);areaList.add(area3);areaList.add(area4);areaList.add(area5);
        areaList.add(area6);areaList.add(area7);areaList.add(area8);areaList.add(area9);areaList.add(area10);
        areaList.add(area11);areaList.add(area12);
        return areaList;
    }
}
