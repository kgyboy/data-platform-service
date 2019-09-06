package com.baiwang.dataplatform.entity;

/**
 * @ClassName Area
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/4
 **/
public class Area {
    private int value;
    private String name;

    public Area(){

    }

    public Area(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
