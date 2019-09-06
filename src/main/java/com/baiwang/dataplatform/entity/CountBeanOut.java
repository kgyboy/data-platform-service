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
    private List<BMArea> bmAreas;
    private List<MDArea> mdAreas;
    private List<CountBean> countBeans;
    private PageNavigator page;

    public List<BMArea> getBmAreas() {
        return bmAreas;
    }

    public void setBmAreas(List<BMArea> bmAreas) {
        this.bmAreas = bmAreas;
    }

    public List<MDArea> getMdAreas() {
        return mdAreas;
    }

    public void setMdAreas(List<MDArea> mdAreas) {
        this.mdAreas = mdAreas;
    }

    public List<CountBean> getCountBeans() {
        return countBeans;
    }

    public void setCountBeans(List<CountBean> countBeans) {
        this.countBeans = countBeans;
    }

    public PageNavigator getPage() {
        return page;
    }

    public void setPage(PageNavigator page) {
        this.page = page;
    }
}
