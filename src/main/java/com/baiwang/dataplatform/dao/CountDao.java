package com.baiwang.dataplatform.dao;

import com.baiwang.dataplatform.entity.CountBean;

import java.util.List;

/**
 * @ClassName CountDao
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public interface CountDao {

    List<CountBean> query(CountBean account);
}
