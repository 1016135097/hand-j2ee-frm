package hand.framework.util;

import java.util.Date;

public class CommonUtils {

	public static java.sql.Date getCurrentDate(){
		Date d = new Date();
		java.sql.Date  result = new java.sql.Date (d.getTime());
		
		return result;
	}
}
