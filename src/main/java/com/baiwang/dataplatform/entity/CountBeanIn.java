package com.baiwang.dataplatform.entity;

/**
 * @ClassName CountBeanIn
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/3
 **/
public class CountBeanIn {
    private String taxno;
    private String startDate;
    private String endDate;
    private int pageNo;
    private int pageSize;

    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
