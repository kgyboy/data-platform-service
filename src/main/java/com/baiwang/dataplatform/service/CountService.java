package com.baiwang.dataplatform.service;


import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;

import java.util.List;

public interface CountService {
	List<CountBean> query(CountBeanIn countBeanIn);

	int count(CountBeanIn countBeanIn);
}
