package com.baiwang.dataplatform.controller;

import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/ceshi", method = RequestMethod.POST)
    public void ceshi(@RequestBody CountBean countBean) {
        List<CountBean> list = countService.query(countBean);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + "===" + list.get(i).toString());
        }
    }

    @GetMapping(value = "/ceshi1")
    public void ceshi1() {
        String sql = "SHOW TABLES";
        sql = "SELECT count(1) FROM contract_info";
//        sql = "DESC kylin_sales";
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
