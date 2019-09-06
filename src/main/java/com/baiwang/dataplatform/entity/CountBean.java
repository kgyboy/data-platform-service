package com.baiwang.dataplatform.entity;

public class CountBean {
    private String taxNo;
    private String diskNo;
    private String baseData;
    private String middData;
    private String dataData;
    private String createDate;


    public CountBean() {
    }

    public CountBean(String taxNo, String diskNo, String baseData, String middData, String dataData, String createDate) {
        this.taxNo = taxNo;
        this.diskNo = diskNo;
        this.baseData = baseData;
        this.middData = middData;
        this.dataData = dataData;
        this.createDate = createDate;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getDiskNo() {
        return diskNo;
    }

    public void setDiskNo(String diskNo) {
        this.diskNo = diskNo;
    }

    public String getBaseData() {
        return baseData;
    }

    public void setBaseData(String baseData) {
        this.baseData = baseData;
    }

    public String getMiddData() {
        return middData;
    }

    public void setMiddData(String middData) {
        this.middData = middData;
    }

    public String getDataData() {
        return dataData;
    }

    public void setDataData(String dataData) {
        this.dataData = dataData;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
