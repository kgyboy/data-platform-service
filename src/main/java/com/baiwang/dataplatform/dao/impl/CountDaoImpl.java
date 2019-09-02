package com.baiwang.dataplatform.dao.impl;

import com.baiwang.dataplatform.common.BeanProperty;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.StringUtils;
import com.baiwang.dataplatform.dao.CountDao;
import com.baiwang.dataplatform.entity.CountBean;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName CountDaoImpl
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
@Service
public class CountDaoImpl implements CountDao {
    @Override
    public List<CountBean> query(CountBean countBean) {
        String sql = "SELECT ID,CONTRACT_CODE,DATE_FORMAT(CREATE_TIME,\"%Y-%m-%d\") createTime FROM contract_info";
        List<BeanProperty> list = analysisCountBean(countBean);
        if (countBean != null)
            sql = dealSql(sql, list);
        List<CountBean> result = HiveUtils.query(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    String value = "";
                    if ("id".equalsIgnoreCase(bean.getPropertyName()))
                        value = countBean.getId() + "";
                    else if ("contract_code".equalsIgnoreCase(bean.getPropertyName()))
                        value = countBean.getContractCode();
                    else if ("createtime".equalsIgnoreCase(bean.getPropertyName()))
                        value = countBean.getCreateTime();
                    pstmt.setString(bean.getIndex(), value);
                }
            }
        }, new HiveUtils.ResultSetHandler<CountBean>() {
            @Override
            public CountBean processRs(ResultSet rs) throws SQLException {
                return new CountBean(rs.getInt("id"),
                        rs.getString("contract_code"),
                        rs.getString("createTime")
                );

            }
        });
        return result;
    }

    private List<BeanProperty> analysisCountBean(CountBean countBean) {
        List<BeanProperty> list = new ArrayList<>();
        BeanProperty property = null;
        int i = 1;
        if (countBean.getId() != 0) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("id");
            property.setPropertySql(" ID=?");
            list.add(property);
        }
        if (StringUtils.isNotBlank(countBean.getContractCode())) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("contract_code");
            property.setPropertySql(" CONTRACT_CODE=?");
            list.add(property);
        }
        if (StringUtils.isNotBlank(countBean.getCreateTime())) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("createtime");
            property.setPropertySql(" DATE_FORMAT(CREATE_TIME, \"%Y-%m-%d\")=?");
            list.add(property);
        }
        return list;
    }

    private String dealSql(String sql, List<BeanProperty> list) {

        StringBuffer sb = new StringBuffer();
        sb.append(sql);

        if (!list.isEmpty()) {
            sb.append(" WHERE");
            for (int i = 0; i < list.size(); i++) {
                if (i == 0)
                    sb.append(list.get(i).getPropertySql());
                else {
                    sb.append("AND").append(list.get(i).getPropertySql());
                }
            }
        }
        return sb.toString();
    }
}
