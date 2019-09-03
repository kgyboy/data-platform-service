package com.baiwang.dataplatform.common;

import java.io.Serializable;

/**
 * @ClassName PageNavigator
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/3
 **/
public class PageNavigator implements Serializable {
    private static final long serialVersionUID = 1L;
    private int curPage = 1;
    private int pageSize = 100;
    private int pageCount = 0;
    private int totalRows = 0;

    public PageNavigator() {
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
        this.pageCount = this.totalRows / this.pageSize;
        if (this.totalRows % this.pageSize > 0) {
            ++this.pageCount;
        }

    }

    public int getTotalRows() {
        return this.totalRows;
    }
}

