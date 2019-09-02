package com.baiwang.dataplatform.service.impl;


import com.baiwang.dataplatform.dao.CountDao;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountServiceImpl implements CountService {
	@Autowired
	private CountDao countDao;

	@Override
	public List<CountBean> query(CountBean account) {
		return countDao.query(account);
	}
}
