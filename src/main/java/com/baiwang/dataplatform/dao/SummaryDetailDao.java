package com.baiwang.dataplatform.dao;

import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
import com.baiwang.dataplatform.entity.CountDayByDayIn;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SummaryDao
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public interface SummaryDetailDao {

    List<CountBean> query(CountDayByDayIn countDayByDayIn);

    int count(CountDayByDayIn countDayByDayIn);

    List<CountBean> querySum(CountBeanIn countBeanIn);

    Map countSum(CountBeanIn countBeanIn);
}
