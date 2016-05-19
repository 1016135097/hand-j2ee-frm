package hand.framework.listner;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {
	protected Logger log = Logger.getLogger(StartupListener.class);

	public void contextInitialized(ServletContextEvent event) {

		if (log.isDebugEnabled()) {
			log.debug("initializing context...");
		}

		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();
		setServletContext(context);

		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		setAppContext(ctx);

		setupContext(context);
		
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		webRoot = webRoot.substring(0,webRoot.indexOf("WEB-INF"));
		System.setProperty("app.root", webRoot);
	}

	public static void setupContext(ServletContext context) {

	}

	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}

	/** 
     */
	private static ApplicationContext appContext;

	public static ApplicationContext getAppContext() {
		return appContext;
	}

	public static void setAppContext(ApplicationContext ctx) {
		appContext = ctx;
	}

	/**
	 * ServletContext
	 */
	private static ServletContext servletContext;

	/**
	 * @return Returns the servletContext.
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext
	 *            The servletContext to set.
	 */
	public static void setServletContext(ServletContext servletContext) {
		StartupListener.servletContext = servletContext;
	}

	public static String getServletWebInfRealPath() {
		return servletContext.getRealPath("WEB-INF");
	}
	
	
}
