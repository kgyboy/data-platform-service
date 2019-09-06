package com.baiwang.dataplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.PageNavigator;
import com.baiwang.dataplatform.entity.*;
import com.baiwang.dataplatform.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Ceshi
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
@RestController
public class CountController {
    @Autowired
    private CountService countService;


    @GetMapping(value = "/index")
    public Object index() {
        return new ModelAndView("/html/index.html");
    }

    @RequestMapping(value = "/ceshi1", method = RequestMethod.POST)
    public Object ceshi1(@RequestBody CountBeanIn countBeanIn) {
        int totals = 0;
        Map map = countService.countSum(countBeanIn);
        List<BMArea> bmAreas = (List<BMArea>) map.get("BM");
        List<MDArea> mdAreas = (List<MDArea>) map.get("MD");
        for (BMArea bm : bmAreas) {
            totals += bm.getValue();
        }
        List<CountBean> list = countService.querySum(countBeanIn);

        PageNavigator page = new PageNavigator();
        page.setCurPage(countBeanIn.getPageNo());
        page.setPageSize(countBeanIn.getPageSize());
        page.setTotalRows(totals);

        CountBeanOut cbo = new CountBeanOut();
        cbo.setBmAreas(bmAreas);
        cbo.setMdAreas(mdAreas);
        cbo.setCountBeans(list);
        cbo.setPage(page);
        return cbo;
    }

    @RequestMapping(value = "/ceshi2", method = RequestMethod.POST)
    public Object ceshi2(@RequestBody CountDayByDayIn countDayByDayIn) {
        int totals = countService.countView(countDayByDayIn);
        List<CountBean> list = countService.queryView(countDayByDayIn);

        PageNavigator page = new PageNavigator();
        page.setCurPage(countDayByDayIn.getPageNo());
        page.setPageSize(countDayByDayIn.getPageSize());
        page.setTotalRows(totals);

        CountBeanOut cbo = new CountBeanOut();
        cbo.setCountBeans(list);
        cbo.setPage(page);
        return cbo;
    }

    @RequestMapping(value = "/ceshi3", method = RequestMethod.POST)
    public Object ceshi3(@RequestBody CountBeanIn countBeanIn) {
        int totals = countService.countDay(countBeanIn);
        List<CountBean> list = countService.queryDay(countBeanIn);

        PageNavigator page = new PageNavigator();
        page.setCurPage(countBeanIn.getPageNo());
        page.setPageSize(countBeanIn.getPageSize());
        page.setTotalRows(totals);

        CountBeanOut cbo = new CountBeanOut();
        cbo.setCountBeans(list);
        cbo.setPage(page);
        return cbo;
    }
}
