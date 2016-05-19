package hand.framework.ebs.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class MOACUtil {
	
	private static ThreadLocal<String> MOAC = new ThreadLocal<String>();
	
	private final static String MOAC_INIT_SQL = "begin  mo_global.init(?); end;";
	
	public static void init(String applShortName){
		MOAC.set(applShortName);
	}
	
	public static boolean isMOACRequied(){
		return !(MOAC.get() == null);
	} 
	
	public static void initMO(Connection conn){
		if(!isMOACRequied()) return;
		initMO(conn, MOAC.get());
	}
	
	public static void initMO(Connection conn,String moac){
		CallableStatement csta = null;
		try {
			csta = conn.prepareCall(MOAC_INIT_SQL);
			csta.setString(1, moac);
			csta.execute();
			csta.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
}
