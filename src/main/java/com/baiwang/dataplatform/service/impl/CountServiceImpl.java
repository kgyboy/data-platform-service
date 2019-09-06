package com.baiwang.dataplatform.service.impl;


import com.baiwang.dataplatform.dao.SummaryDao;
import com.baiwang.dataplatform.dao.SummaryViewDao;
import com.baiwang.dataplatform.dao.SummaryDayDao;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
import com.baiwang.dataplatform.entity.CountDayByDayIn;
import com.baiwang.dataplatform.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountServiceImpl implements CountService {
	@Autowired
	private SummaryDao summaryDao;

	@Autowired
	private SummaryViewDao summaryViewDao;

	@Autowired
	private SummaryDayDao summaryDay;

	@Override
	public Map countSum(CountBeanIn countBeanIn) {
		return summaryDao.countSum(countBeanIn);
	}

	@Override
	public List<CountBean> querySum(CountBeanIn countBeanIn) {
		return summaryDao.querySum(countBeanIn);
	}

	@Override
	public int countView(CountDayByDayIn countDayByDayIn) {
		return summaryViewDao.countView(countDayByDayIn);
	}

	@Override
	public List<CountBean> queryView(CountDayByDayIn countDayByDayIn) {
		return summaryViewDao.queryView(countDayByDayIn);
	}

	@Override
	public int countDay(CountBeanIn countBeanIn) {
		return summaryDay.countDay(countBeanIn);
	}

	@Override
	public List<CountBean> queryDay(CountBeanIn countBeanIn) {
		return summaryDay.queryDay(countBeanIn);
	}
}
