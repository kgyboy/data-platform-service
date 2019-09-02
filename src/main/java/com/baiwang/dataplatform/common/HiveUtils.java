package com.baiwang.dataplatform.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HiveUtils
 * @Description //TODO
 * @Author yangsibiao
 * @Date 2019/9/2
 **/
public final class HiveUtils {
	// 此方法实现所有的增删改操作
	public static int update(String sql, PreparedStatementSetter setter) {
		int res = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			pstmt = conn.prepareStatement(sql);
			// 补全sql语句
			if (setter != null) {
				setter.setValues(pstmt);
			}
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(null, pstmt, conn);
		}
		return res;

	}

	// 泛型方法,返回的是一个集合
	public static <T> List<T> query(String sql, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<T> dataList = null;
		try {
			conn = ConnectionFactory.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (setter != null) {
				setter.setValues(pstmt);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				dataList = new ArrayList<T>();
				while (rs.next()) {
					// 添加读取到的记录对象
					dataList.add(handler.processRs(rs));
				}
			}
		} catch (Exception e) {
		} finally {
			closeAll(null, pstmt, conn);
		}

		return dataList;
	}

	// 查询返回单个值
	public static <T> T singleQuery(String sql, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		T data = null;
		try {
			conn = ConnectionFactory.getConnection();
			pstmt = conn.prepareStatement(sql);
			if (setter != null) {
				setter.setValues(pstmt);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					// 添加读取到的记录对象
					data = handler.processRs(rs);
				}
			}
		} catch (Exception e) {
		} finally {
			closeAll(null, pstmt, conn);
		}
		return data;
	}

	// 关闭
	public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
		}
	}

	public static interface PreparedStatementSetter {
		// 对带不同占位符的sql语句进行替换补全的操作
		public void setValues(PreparedStatement pstmt) throws SQLException;
	}

	// 对处理不同的结果集进行抽象
	public static interface ResultSetHandler<T> {
		// 处理结果集返回一个对象
		public T processRs(ResultSet rs) throws SQLException;
	}

}
