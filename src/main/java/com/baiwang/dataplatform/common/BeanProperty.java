package com.baiwang.dataplatform.common;

/**
 * @ClassName BeanProperty
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public class BeanProperty {
    private int index;
    private String propertyName;
    private String propertySql;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertySql() {
        return propertySql;
    }

    public void setPropertySql(String propertySql) {
        this.propertySql = propertySql;
    }
}
