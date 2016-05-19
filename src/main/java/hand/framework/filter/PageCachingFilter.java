package hand.framework.filter;

import hand.framework.ebs.util.EBSContext;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class PageCachingFilter extends SimplePageCachingFilter{
	
	private static Logger logger = Logger.getLogger(PageCachingFilter.class);
	
	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		StringBuffer stringBuffer = new StringBuffer();
		String queryStr = httpRequest.getQueryString();
		int dcIndex = queryStr.indexOf("&_dc=");
		if(dcIndex > 0){
			queryStr = queryStr.substring(0, dcIndex);
		}
		stringBuffer.append(httpRequest.getMethod()).append(httpRequest.getRequestURI()).append(queryStr);
		   
		Enumeration<String> names = httpRequest.getParameterNames();
	    while(names.hasMoreElements()){
	    	String name = names.nextElement();
	    	String value = httpRequest.getParameter(name);
	    	stringBuffer.append(name).append("=").append(value);
	    }
	    // 附加EBS信息
		EBSContext context = EBSContext.getInstance();
		stringBuffer.append(context.toString());
		
		String key = stringBuffer.toString();
		
		logger.info(key);
	    return key;
	}

}
