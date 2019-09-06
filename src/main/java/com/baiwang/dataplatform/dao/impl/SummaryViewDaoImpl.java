package com.baiwang.dataplatform.dao.impl;

import com.baiwang.dataplatform.common.BeanProperty;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.StringUtils;
import com.baiwang.dataplatform.dao.SummaryViewDao;
import com.baiwang.dataplatform.entity.CountBean;
import com.baiwang.dataplatform.entity.CountBeanIn;
import com.baiwang.dataplatform.entity.CountDayByDayIn;
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
public class SummaryViewDaoImpl implements SummaryViewDao {
    private static String TABLENAME = "datacenter";
    private static String FILED = "TAXNO,DISKNO,SUM(BASEDATA) AS BASEDATA,SUM(MIDDDATA) AS MIDDDATA,SUM(DATADATA) AS DATADATA," +
            "CASE WHEN SUM(BASEDATA) / SUM(MIDDDATA) > 1 THEN '>100%'" +
            "WHEN SUM(BASEDATA) / SUM(MIDDDATA) = 1 THEN '100%'" +
            "WHEN SUM(BASEDATA) / SUM(MIDDDATA) < 1 AND SUM(BASEDATA) / SUM(MIDDDATA) >= 0.8 THEN '80%-100%'" +
            "ELSE '<80%'" +
            "END AS BM," +
            "CASE WHEN SUM(MIDDDATA) / SUM(DATADATA) > 1 THEN '>100%'" +
            "WHEN SUM(MIDDDATA) / SUM(DATADATA) = 1 THEN '100%'" +
            "WHEN SUM(MIDDDATA) / SUM(DATADATA) < 1 AND SUM(MIDDDATA) / SUM(DATADATA) >= 0.8 THEN '80%-100%'" +
            "ELSE '<80%'" +
            "END AS MD";

    @Override
    public List<CountBean> queryView(CountDayByDayIn countDayByDayIn) {

        String sql = "SELECT " + FILED + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countDayByDayIn);
        sql = dealSumSql(sql, list, countDayByDayIn);

        return HiveUtils.query(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    pstmt.setString(bean.getIndex(), getValue(bean, countDayByDayIn));
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
                        null
                );
            }
        });
    }

    @Override
    public int countView(CountDayByDayIn countDayByDayIn) {
        String sql = "SELECT " + FILED + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countDayByDayIn);
        sql = dealSumSql(sql, list, null);
        sql = "SELECT COUNT(1) FROM (" + sql + " ) AS a WHERE " + countDayByDayIn.getName() + " = '" + countDayByDayIn.getValue() + "'";
        return HiveUtils.singleQuery(sql, new HiveUtils.PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                for (BeanProperty bean : list) {
                    pstmt.setString(bean.getIndex(), getValue(bean, countDayByDayIn));
                }
            }
        }, new HiveUtils.ResultSetHandler<Integer>() {
            @Override
            public Integer processRs(ResultSet rs) throws SQLException {
                return rs.getInt(1);
            }
        });
    }

    private String dealSumSql(String sql, List<BeanProperty> list, CountDayByDayIn countDayByDayIn) {
        StringBuffer sb = new StringBuffer();

        if (countDayByDayIn != null)
            sb.append("SELECT * FROM (");
        sb.append(sql);
        sb.append(" WHERE");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0)
                sb.append(list.get(i).getPropertySql());
            else {
                sb.append("AND").append(list.get(i).getPropertySql());
            }
        }
        sb.append(" GROUP BY");
        sb.append(" taxno,diskno");
        if (countDayByDayIn != null) {
            sb.append(") AS a WHERE " + countDayByDayIn.getName() + " = '" + countDayByDayIn.getValue() + "'");
            sb.append(" LIMIT ");
            sb.append((countDayByDayIn.getPageNo() - 1) * countDayByDayIn.getPageSize());
            sb.append(",");
            sb.append(countDayByDayIn.getPageSize());
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
