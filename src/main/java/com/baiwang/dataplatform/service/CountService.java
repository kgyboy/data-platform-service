package com.baiwang.dataplatform.service;


import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
import com.baiwang.dataplatform.entity.CountDayByDayIn;

import java.util.List;
import java.util.Map;

public interface CountService {
	Map countSum(CountBeanIn countBeanIn);

	List<CountBean> querySum(CountBeanIn countBeanIn);

	int countView(CountDayByDayIn countDayByDayIn);

	List<CountBean> queryView(CountDayByDayIn countDayByDayIn);

	int countDay(CountBeanIn countBeanIn);

	List<CountBean> queryDay(CountBeanIn countBeanIn);
}
