package hand.framework.ebs.util;


import hand.framework.listner.StartupListener;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBSUtils {
	private static DataSource dataSource = (DataSource) StartupListener.getBean("dataSource");

	public static String getPersonName() {
		BigDecimal userId = EBSContext.getInstance().getUserId();

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			   
			String sql = "SELECT ppf.full_name " 
					+ " FROM per_all_people_f ppf,"
					+ "		 fnd_user         fu"
					+ " WHERE ppf.person_id = fu.employee_id"
					+ "	 AND SYSDATE BETWEEN ppf.effective_start_date AND"
					+ "			 ppf.effective_end_date" + "	 AND fu.user_id = ?";
			PreparedStatement sta = conn.prepareStatement(sql);
			sta.setBigDecimal(1, userId);
			ResultSet rs = sta.executeQuery();
			if(rs.next()){
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static BigDecimal getPersonId() {
		BigDecimal userId = EBSContext.getInstance().getUserId();

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			   
			String sql = "SELECT ppf.person_id " 
					+ " FROM per_all_people_f ppf,"
					+ "		 fnd_user         fu"
					+ " WHERE ppf.person_id = fu.employee_id"
					+ "	 AND SYSDATE BETWEEN ppf.effective_start_date AND"
					+ "			 ppf.effective_end_date" + "	 AND fu.user_id = ?";
			PreparedStatement sta = conn.prepareStatement(sql);
			sta.setBigDecimal(1, userId);
			ResultSet rs = sta.executeQuery();
			if(rs.next()){
				return rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
