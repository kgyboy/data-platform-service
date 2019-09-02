package com.baiwang.dataplatform.service;


import com.baiwang.dataplatform.entity.CountBean;

import java.util.List;

public interface CountService {
	List<CountBean> query(CountBean account);
}
