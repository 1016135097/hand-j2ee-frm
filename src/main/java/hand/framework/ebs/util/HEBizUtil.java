package hand.framework.ebs.util;

import oracle.apps.fnd.ext.common.EBiz;

import java.sql.Connection;
import java.sql.SQLException;

public class HEBizUtil {

	private static EBiz ebiz;

	public static EBiz getEBizInstance(Connection conn, String applServerID) {
		if (ebiz != null) {
			return ebiz;
		}

		try {
			ebiz = new EBiz(conn, applServerID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ebiz;
		
	}

}
