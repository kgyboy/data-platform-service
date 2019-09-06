package com.baiwang.dataplatform.dao.impl;

import com.baiwang.dataplatform.common.BeanProperty;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.StringUtils;
import com.baiwang.dataplatform.dao.SummaryDayDao;
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
public class SummaryDayDaoImpl implements SummaryDayDao {
    private static String TABLENAME = "datacenter";
    private static String FILED = "TAXNO,DISKNO,BASEDATA,MIDDDATA,DATADATA,DATE_FORMAT(CREATEDATE,\"%Y-%m-%d\") AS CREATEDATE";

    @Override
    public List<CountBean> queryDay(CountBeanIn countBeanIn) {
        String sql = "SELECT " + FILED + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
        if (countBeanIn != null) {
            sql = dealSql(sql, list, countBeanIn);
        }
        return HiveUtils.query(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    pstmt.setString(bean.getIndex(), getValue(bean, countBeanIn));
                }
            }
        }, new HiveUtils.ResultSetHandler<CountBean>() {
            @Override
            public CountBean processRs(ResultSet rs) throws SQLException {
                return new CountBean(rs.getString("TAXNO"),
                        rs.getString("DISKNO"),
                        rs.getString("BASEDATA"),
                        rs.getString("MIDDDATA"),
                        rs.getString("DATADATA"),
                        rs.getString("CREATEDATE")
                );
            }
        });
    }

    @Override
    public int countDay(CountBeanIn countBeanIn) {
        String sql = "SELECT COUNT(1) FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
        if (countBeanIn != null) {
            sql = dealSql(sql, list, null);
        }
        return HiveUtils.singleQuery(sql, new HiveUtils.PreparedStatementSetter() {
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

        if (countBeanIn != null) {
            sb.append(" LIMIT ");
            sb.append((countBeanIn.getPageNo() - 1) * countBeanIn.getPageSize());
            sb.append(",");
            sb.append(countBeanIn.getPageSize());
        }
        return sb.toString();
    }

    private List<BeanProperty> analysisCountBeanIn(CountBeanIn countBeanIn) {
        List<BeanProperty> list = new ArrayList<>();
        BeanProperty property = null;
        int i = 1;
        if (StringUtils.isNotBlank(countBeanIn.getTaxno())) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("taxno");
            property.setPropertySql(" TAXNO=?");
            list.add(property);
        }
        if (StringUtils.isNotBlank(countBeanIn.getStartDate())) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("startDate");
            property.setPropertySql(" DATE_FORMAT(CREATEDATE, \"%Y-%m-%d\")>=?");
            list.add(property);
        }
        if (StringUtils.isNotBlank(countBeanIn.getEndDate())) {
            property = new BeanProperty();
            property.setIndex(i++);
            property.setPropertyName("endDate");
            property.setPropertySql(" DATE_FORMAT(CREATEDATE, \"%Y-%m-%d\")<=?");
            list.add(property);
        }
        return list;
    }

    private String getValue(BeanProperty bean, CountBeanIn countBeanIn) {
        String value = "";
        if ("taxno".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getTaxno();
        else if ("startDate".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getStartDate();
        else if ("endDate".equalsIgnoreCase(bean.getPropertyName()))
            value = countBeanIn.getEndDate();

        return value;
    }


}
