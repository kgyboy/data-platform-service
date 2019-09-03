package com.baiwang.dataplatform.dao.impl;

import com.baiwang.dataplatform.common.BeanProperty;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.StringUtils;
import com.baiwang.dataplatform.dao.CountDao;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
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
    public List<CountBean> query(CountBeanIn countBeanIn) {
        String sql = "SELECT ID,CONTRACT_CODE AS contractCode,DATE_FORMAT(CREATE_TIME,\"%Y-%m-%d\") AS createTime FROM contract_info";
        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
        if (countBeanIn != null)
            sql = dealSql(sql, list, countBeanIn);
        List<CountBean> result = HiveUtils.query(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    pstmt.setString(bean.getIndex(), getValue(bean, countBeanIn));
                }
            }
        }, new HiveUtils.ResultSetHandler<CountBean>() {
            @Override
            public CountBean processRs(ResultSet rs) throws SQLException {
                return new CountBean(rs.getInt("id"),
                        rs.getString("contractcode"),
                        rs.getString("createTime")
                );
            }
        });
        return result;
    }

    @Override
    public int count(CountBeanIn countBeanIn) {
        String sql = "SELECT COUNT(1) FROM contract_info";
        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
        if (countBeanIn != null){
            sql = dealSql(sql, list, null);
        }
        int totals = HiveUtils.singleQuery(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    pstmt.setString(bean.getIndex(), getValue(bean, countBeanIn));
                }
            }
        }, new HiveUtils.ResultSetHandler<Integer>() {
            @Override
            public Integer processRs(ResultSet rs) throws SQLException {
                return rs.getInt(1);
            }
        });
        return totals;
    }

    private List<BeanProperty> analysisCountBeanIn(CountBeanIn countBeanIn) {
        List<BeanProperty> list = new ArrayList<>();
        if (countBeanIn != null) {
            BeanProperty property = null;
            int i = 1;
            if (countBeanIn.getId() != 0) {
                property = new BeanProperty();
                property.setIndex(i++);
                property.setPropertyName("id");
                property.setPropertySql(" ID=?");
                list.add(property);
            }
            if (StringUtils.isNotBlank(countBeanIn.getContractCode())) {
                property = new BeanProperty();
                property.setIndex(i++);
                property.setPropertyName("contractCode");
                property.setPropertySql(" CONTRACT_CODE=?");
                list.add(property);
            }
            if (StringUtils.isNotBlank(countBeanIn.getCreateTime())) {
                property = new BeanProperty();
                property.setIndex(i++);
                property.setPropertyName("createtime");
                property.setPropertySql(" DATE_FORMAT(CREATE_TIME, \"%Y-%m-%d\")=?");
                list.add(property);
            }
        }
        return list;
    }

    private String dealSql(String sql, List<BeanProperty> list, CountBeanIn countBeanIn) {

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
        if (countBeanIn != null && countBeanIn.getPageSize() != 0) {
            sb.append(" LIMIT ");
            sb.append((countBeanIn.getPageNo() - 1) * countBeanIn.getPageSize());
            sb.append(",");
            sb.append(countBeanIn.getPageSize());
        }
        return sb.toString();
    }

    private String getValue(BeanProperty bean, CountBeanIn countBeanIn) {
        String value = "";
        if ("id".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getId() + "";
        else if ("contractode".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getContractCode();
        else if ("createtime".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getCreateTime();

        return value;
    }
}
