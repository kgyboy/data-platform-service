package com.baiwang.dataplatform.dao;

import java.util.List;

//公共接口
public interface BaseDao<T> {
    public int save(T obj);
    public int delete(int id);
    public int delete(T obj);
    public int update(T obj);
    public List<T> queryAll();
    public T query(T obj);
    public T queryById(int id);
}
