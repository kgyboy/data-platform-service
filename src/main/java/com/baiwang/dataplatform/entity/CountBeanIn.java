package com.baiwang.dataplatform.entity;

/**
 * @ClassName CountBeanIn
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/3
 **/
public class CountBeanIn extends CountBean {
    private int pageNo;
    private int pageSize;

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
