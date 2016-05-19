package hand.framework.ebs.filter;

import hand.framework.listner.StartupListener;
import oracle.apps.fnd.ext.common.AppsRequestWrapper;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * EBS request 过滤器 将request转化成EBS SDK的AppsRequestWrapper
 * 
 * @author Pany
 * 
 */
public class EBSWrapperFilter extends EBSFilter {

	private DataSource dataSource;

	/**
	 * 获取DataSource
	 */
	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = (DataSource) StartupListener.getBean("dataSource");
		}
		return dataSource;
	}

	@Override
	public boolean doFilter(AppsRequestWrapper wrappedRequest, ServletResponse response) throws IOException,
			ServletException {
		String url = wrappedRequest.getServletPath();
		// 静态资源不拦截
		// timeout.jsp 不拦截
		if (url.endsWith(".js") 
				|| url.endsWith(".css") 
					|| url.endsWith(".gif")
						|| url.endsWith(".jpg") 
							|| url.endsWith(".png")
								|| url.equals("/jsp/timeout.jsp")
									|| url.endsWith("auth.jsp")) {
			return true;
		}

		// 验证登陆
		boolean valid = isAuthenticated(wrappedRequest);

		if (!valid) {
			// 获取EBS登陆地址
			String loginUrl = getEbsLoginUrl(wrappedRequest);

			// 进行跳转
			String contentType = wrappedRequest.getHeader("X-Requested-With");

			if (contentType != null) {
				((HttpServletResponse) response).addHeader("_timeout", "true");
				response.getWriter().println("{sucess:false,error:'Timeout',url:'"+ loginUrl + "'}");
				return false;
			} else {
				String path = wrappedRequest.getContextPath();
				String basePath = wrappedRequest.getScheme() + "://"
						+ wrappedRequest.getServerName() + ":"
						+ wrappedRequest.getServerPort() + path + "/";
				String timeOutUrl = basePath + "/jsp/timeout.jsp?url="
						+ loginUrl;
				((HttpServletResponse) response).sendRedirect(timeOutUrl);
				return false;
			}
		}

		return true;
	}

}
