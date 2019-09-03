package com.baiwang.dataplatform.entity;

import com.baiwang.dataplatform.common.PageNavigator;

import java.util.List;

/**
 * @ClassName CountBeanOut
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/3
 **/
public class CountBeanOut {
    private List list;
    private PageNavigator page;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public PageNavigator getPage() {
        return page;
    }

    public void setPage(PageNavigator page) {
        this.page = page;
    }
}
