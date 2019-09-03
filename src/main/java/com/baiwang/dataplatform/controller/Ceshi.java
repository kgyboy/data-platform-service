package com.baiwang.dataplatform.controller;

import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.PageNavigator;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
import com.baiwang.dataplatform.entity.CountBeanOut;
import com.baiwang.dataplatform.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName Ceshi
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
@RestController
public class Ceshi {
    @Autowired
    private CountService countService;


    @GetMapping(value = "/index")
    public Object index(){
        return new ModelAndView("/html/index.html");
    }

    @RequestMapping(value = "/ceshi", method = RequestMethod.POST)
    public CountBeanOut ceshi(@RequestBody CountBeanIn countBeanIn) {

        int totals = countService.count(countBeanIn);
        List<CountBean> list = countService.query(countBeanIn);

        PageNavigator page = new PageNavigator();
        page.setCurPage(countBeanIn.getPageNo());
        page.setPageSize(countBeanIn.getPageSize());
        page.setTotalRows(totals);
        System.out.println("totals = " +totals);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + "===" + list.get(i).toString());
        }
        CountBeanOut cbo = new CountBeanOut();
        cbo.setList(list);
        cbo.setPage(page);
        return cbo;
    }

    @GetMapping(value = "/ceshi1")
    public void ceshi1() {
        String sql = "SHOW TABLES";
        List<Object> list = HiveUtils.query(sql, null, new HiveUtils.ResultSetHandler<Object>() {
            @Override
            public Object processRs(ResultSet rs) throws SQLException {
                return rs.getObject(1);
            }
        });

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + "===" + list.get(i));
        }
    }
}
