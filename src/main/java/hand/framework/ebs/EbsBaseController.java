package hand.framework.ebs;

import java.sql.Date;

import hand.framework.json.JsonUtils;
import hand.framework.util.DateEditor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

public class EbsBaseController {

	@ExceptionHandler
	@ResponseBody
	public String exp(HttpServletRequest request, Exception ex){
		ex.printStackTrace();
		String msg = null;
		if(ex instanceof UncategorizedSQLException){
			UncategorizedSQLException e = (UncategorizedSQLException)ex;
//			System.out.println(e.getSQLException().getErrorCode());
			if(e.getSQLException().getErrorCode() == 20003){
					msg = e.getSQLException().getMessage();
					msg = msg.split("ORA-20003: ")[1].split("ORA")[0];
			} else{
				msg = e.getSQLException().getMessage();
			}
			
		} else{
			msg = ex.getMessage();
		}
		return JsonUtils.error(msg, null);		
	}

	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
}
