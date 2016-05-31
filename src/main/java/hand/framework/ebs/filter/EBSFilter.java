package hand.framework.ebs.filter;


import hand.framework.ebs.util.EBSContext;
import hand.framework.ebs.util.HEBizUtil;
import oracle.apps.fnd.ext.common.AppsRequestWrapper;
import oracle.apps.fnd.ext.common.AppsRequestWrapper.WrapperException;
import oracle.apps.fnd.ext.common.CookieStatus;
import oracle.apps.fnd.ext.common.Session;
import oracle.apps.fnd.security.HMAC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

/**
 * EBS Filter
 * @author Pany
 *
 */
public abstract class EBSFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(EBSFilter.class);
	
	protected String appServerId;

	protected FilterConfig config;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		DataSource dataSource = getDataSource();
		
		if (dataSource == null) {
			throw new ServletException(
					"DataSource is Null! Please Check The DataSource Config!");
		}

		AppsRequestWrapper wrapper = null;
		// 获取ApplId
		appServerId = getAppServerId();
		if (!(request instanceof AppsRequestWrapper)) {
			try {
				wrapper = new AppsRequestWrapper((HttpServletRequest) request,
												 (HttpServletResponse) response,
												 dataSource.getConnection(),
												 HEBizUtil.getEBizInstance( dataSource.getConnection(),appServerId));
			} catch (WrapperException e) {
				e.printStackTrace();
				throw new ServletException(e);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}
		logger.info("Request Wrapped.");
		// 调用具体业务处理逻辑
		boolean flag = doFilter(wrapper, response);
		logger.info("Filter Done.");
		if(flag){
			try{
				chain.doFilter(wrapper, response);
			} finally {
				if(wrapper != null){
					try {
						wrapper.getConnection().close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		this.appServerId = filterConfig.getServletContext().getInitParameter("APPL_SERVER_ID");
		logger.info("APP Server ID:"+this.appServerId);
	}

	public abstract boolean doFilter(AppsRequestWrapper wrappedRequest, ServletResponse response)  throws IOException, ServletException;

	public abstract DataSource getDataSource();

	protected String getAppServerId() {
		return this.appServerId;
	}
	
	/**
	 * 验证登陆是否有效
	 * @param wrappedRequest
	 * @return
	 * @throws ServletException
	 */
	protected boolean isAuthenticated(AppsRequestWrapper wrappedRequest) throws ServletException {
		Session session = wrappedRequest.getAppsSession(true);
		if (session == null) {
			throw new ServletException("Could not initailize ICX session object, Local="+ wrappedRequest.getLocale().toString());
		}
		CookieStatus icxCookieStatus = session.getCurrentState().getIcxCookieStatus();
		if (!icxCookieStatus.equals(CookieStatus.VALID)) {
			logger.info("Session Invalid!");
			return false;
		}
		
		// 设置EBS上下文信息
		setContextInfo(wrappedRequest);
		
		return true;
	}
	
	/**
	 * 获取EBS登陆地址
	 * @param wrappedRequest
	 * @return
	 */
	protected String getEbsLoginUrl(AppsRequestWrapper wrappedRequest){
		String agent = wrappedRequest.getEbizInstance().getAppsServletAgent();
		String loginUrl = agent+ "AppsLogin";
		return loginUrl;
	}
	
	/**
	 * 设置EBS上下文
	 * 
	 * @param wrappedRequest
	 */
	private void setContextInfo(AppsRequestWrapper wrappedRequest) {
		EBSContext context = EBSContext.getInstance();
		
		logger.info("Setting EBS Context");
		if (context == null) {
			context = new EBSContext();
		}

		Session session = wrappedRequest.getAppsSession();

		Map<String, Object> sessionInfo = session.getRawInfo();
		context.setSessionInfo(sessionInfo);
		String agent = wrappedRequest.getEbizInstance().getAppsServletAgent();
		context.setAgent(agent);
		context.setLangCode(wrappedRequest.getLangCode());
		context.setDateFormatMask((String) sessionInfo.get("DATE_FORMAT_MASK"));
		context.setFunctionId((BigDecimal) sessionInfo.get("FUNCTION_ID"));
		context.setModeCode((String) sessionInfo.get("MODE_CODE"));
		context.setNlsLanguage((String) sessionInfo.get("NLS_LANGUAGE"));
		context.setOrgId((BigDecimal) sessionInfo.get("ORG_ID"));
		context.setResponsibilityApplicationId((BigDecimal) sessionInfo.get("RESPONSIBILITY_APPLICATION_ID"));
		context.setResponsibilityId((BigDecimal) sessionInfo.get("RESPONSIBILITY_ID"));
		context.setSessionId((BigDecimal) sessionInfo.get("SESSION_ID"));
		context.setUserId((BigDecimal) sessionInfo.get("USER_ID"));
		context.setUserName(session.getUserName());
		context.setLoginId(new BigDecimal(-1));

		byte[] keys = (byte[])sessionInfo.get("MAC_KEY");
		context.setMacKey(keys);
		HMAC hmac = new HMAC(0);
		hmac.setKey(keys);
		context.setHmac(hmac);

		EBSContext.setContext(context);
	}

	@Override
	public void destroy() {
	}

}
