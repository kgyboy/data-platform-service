package com.baiwang.dataplatform.dao.impl;

import com.baiwang.dataplatform.common.BeanProperty;
import com.baiwang.dataplatform.common.HiveUtils;
import com.baiwang.dataplatform.common.StringUtils;
import com.baiwang.dataplatform.dao.SummaryDetailDao;
import com.baiwang.dataplatform.entity.*;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName CountDaoImpl
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
@Service
public class SummaryDetailDaoImpl implements SummaryDetailDao {
    private static String TABLENAME = "datacenter";
    private static String FILED = "TAXNO,DISKNO,BASEDATA,MIDDDATA,DATADATA,DATE_FORMAT(CREATEDATE,\"%Y-%m-%d\") AS CREATEDATE";
    private static String FILED2 = "TAXNO, DISKNO, BASEDATA, MIDDDATA, DATADATA, DATE_FORMAT(CREATEDATE, \"%Y-%m-%d\") CREATEDATE, CASE WHEN BASEDATA / MIDDDATA > 1 THEN '>100%' WHEN BASEDATA / MIDDDATA = 1 THEN '100%' WHEN BASEDATA / MIDDDATA < 1 AND BASEDATA / MIDDDATA >= 0.8 THEN '80%-100%' ELSE '<80%' END AS BM,  CASE WHEN MIDDDATA / DATADATA > 1 THEN '>100%' WHEN MIDDDATA / DATADATA = 1 THEN '100%' WHEN MIDDDATA / DATADATA < 1 AND MIDDDATA / DATADATA >= 0.8 THEN '80%-100%' ELSE '<80%' END AS MD";
    private static String FILED3 = "TAXNO,DISKNO,SUM(BASEDATA) AS BASEDATA,SUM(MIDDDATA) AS MIDDDATA,SUM(DATADATA) AS DATADATA," +
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
    public List<CountBean> query(CountDayByDayIn countDayByDayIn) {
        String sql = "SELECT " + FILED2 + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countDayByDayIn);
        if (countDayByDayIn != null) {
            sql = dealSql(sql, list, countDayByDayIn);
        }
        List<CountBean> result = HiveUtils.query(sql, new HiveUtils.PreparedStatementSetter() {
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
                        rs.getString("CREATEDATE")
                );
            }
        });
        return result;
    }

    @Override
    public int count(CountDayByDayIn countDayByDayIn) {
        String sql = "SELECT " + FILED2 + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countDayByDayIn);
        if (countDayByDayIn != null) {
            sql = dealSql(sql, list, null);
        }
        String sql1 = "SELECT COUNT(1) FROM (" + sql + " ) AS a WHERE " + countDayByDayIn.getName() + " = '" + countDayByDayIn.getValue() + "'";
        int totals = HiveUtils.singleQuery(sql1, new HiveUtils.PreparedStatementSetter() {
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
        return totals;
    }

    @Override
    public List<CountBean> querySum(CountBeanIn countBeanIn) {

        String sql = "SELECT " + FILED3 + " FROM " + TABLENAME;
        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
        sql = dealSumSql(sql, list, countBeanIn);

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
                return new CountBean(rs.getString("TAXNO"),
                        rs.getString("DISKNO"),
                        rs.getString("BASEDATA"),
                        rs.getString("MIDDDATA"),
                        rs.getString("DATADATA"),
                        null
                );
            }
        });
        return result;
    }

    @Override
    public Map countSum(CountBeanIn countBeanIn) {
        Map<String, List<? extends Object>> map = new HashMap<>();
//        String sql = "SELECT " + FILED3 + " FROM " + TABLENAME;
//        List<BeanProperty> list = analysisCountBeanIn(countBeanIn);
//        sql = dealSumSql(sql, list, null);
//        String BM = "BM";
//        String MD = "MD";
//        String sql1 = "SELECT " + BM + ",COUNT(1) num FROM (" + sql + " ) AS a GROUP BY " + BM;
//        String sql2 = "SELECT " + MD + ",COUNT(1) num FROM (" + sql + " ) AS a GROUP BY " + MD;
//        List<BMArea> bmAreas = HiveUtils.query(sql1, new HiveUtils.PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement pstmt) throws SQLException {
//                for (BeanProperty bean : list) {
//                    pstmt.setString(bean.getIndex(), getValue(bean, countBeanIn));
//                }
//            }
//        }, new HiveUtils.ResultSetHandler<BMArea>() {
//            @Override
//            public BMArea processRs(ResultSet rs) throws SQLException {
//                return new BMArea(rs.getInt("num"), rs.getString(BM));
//            }
//        });
//        List<MDArea> mdAreas = HiveUtils.query(sql2, new HiveUtils.PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement pstmt) throws SQLException {
//                for (BeanProperty bean : list) {
//                    pstmt.setString(bean.getIndex(), getValue(bean, countBeanIn));
//                }
//            }
//        }, new HiveUtils.ResultSetHandler<MDArea>() {
//            @Override
//            public MDArea processRs(ResultSet rs) throws SQLException {
//                return new MDArea(rs.getInt("num"), rs.getString(MD));
//            }
//        });
//        map.put(BM, bmAreas);
//        map.put(MD, mdAreas);
        return map;
    }

    private String dealSumSql(String sql, List<BeanProperty> list, CountBeanIn countBeanIn) {
        StringBuffer sb = new StringBuffer();
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
        if (countBeanIn != null && countBeanIn.getPageSize() != 0) {
            sb.append(" LIMIT ");
            sb.append((countBeanIn.getPageNo() - 1) * countBeanIn.getPageSize());
            sb.append(",");
            sb.append(countBeanIn.getPageSize());
        }
        return sb.toString();
    }

    private String dealSql(String sql, List<BeanProperty> list, CountDayByDayIn countDayByDayIn) {

        StringBuffer sb = new StringBuffer();
        if (countDayByDayIn != null)
            sb.append("SELECT * FROM (");
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

        if (countDayByDayIn != null && !list.isEmpty()) {
            sb.append(") AS a");
            sb.append(" WHERE " + countDayByDayIn.getName() + " = '" + countDayByDayIn.getValue() + "'");
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
