package hand.framework.ebs.util.datasource;

import hand.framework.ebs.util.EBSContext;
import hand.framework.ebs.util.MOACUtil;
import oracle.apps.fnd.ext.jdbc.datasource.AppsDataSource;
import oracle.apps.fnd.util.PreCondition;
import oracle.jdbc.OracleCallableStatement;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

//import oracle.jdbc.OracleCallableStatement;

public class OracleAppsDataSource extends AppsDataSource {

	private static final long serialVersionUID = -3215841348716356331L;

	private static final String INIT_SQL = "begin  fnd_global.APPS_INITIALIZE(user_id => ?,resp_id => ?,resp_appl_id =>? ); end;";
	
	private static Logger logger = Logger.getLogger(OracleAppsDataSource.class);
	
	public OracleAppsDataSource() throws SQLException{
		super();
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection conn = super.getConnection();
		initConnection(conn);
		return conn;
	}
	
	@Override
	public synchronized void setDbcFile(String dbcFile) {
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		webRoot = webRoot.substring(0,webRoot.indexOf("WEB-INF"));
		
		if(dbcFile.startsWith("/WEB-INF")){
			dbcFile = webRoot + dbcFile;
		}
		logger.info("DBCFile Is:"+dbcFile);
		super.setDbcFile(dbcFile);
	}

	public void initConnection(Connection conn){
		EBSContext context = EBSContext.getInstance();
		if(context != null){
			try {
				setNLSContext(conn,context.getNlsLanguage());
				CallableStatement csta = conn.prepareCall(INIT_SQL);
				csta.setInt(1,context.getUserId().intValue());
				csta.setInt(2, context.getResponsibilityId().intValue());
				csta.setInt(3, context.getResponsibilityApplicationId().intValue());
				
				csta.execute();
//				System.out.println("Initialized Connection using user_id:"+context.getUserId()+" respId:"+context.getResponsibilityId()+" respAppId:"+context.getResponsibilityApplicationId());
				logger.info("Initialized Connection using user_id:"+context.getUserId()+" respId:"+context.getResponsibilityId()+" respAppId:"+context.getResponsibilityApplicationId());
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		// MOAC
		MOACUtil.initMO(conn);
	}

	public void setNLSContext(Connection connection, String nlsLanguage)
			throws SQLException {
		PreCondition.assertNotNull(connection);

		OracleCallableStatement cStmt = null;
		try {
			cStmt = (OracleCallableStatement) connection
					.prepareCall("BEGIN FND_AOLJ_UTIL.SET_NLS_CONTEXT( :1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13); END;");

			if (nlsLanguage != null)
				cStmt.setString(1, nlsLanguage);
			else {
				cStmt.setNull(1, 12);
			}
			cStmt.setNull(2, 12);

			cStmt.setNull(3, 12);
			cStmt.setNull(4, 12);
			cStmt.setNull(5, 12);
			cStmt.setNull(6, 12);
			cStmt.registerOutParameter(7, 12, 0, 64);
			cStmt.registerOutParameter(8, 12, 0, 64);
			cStmt.registerOutParameter(9, 12, 0, 64);
			cStmt.registerOutParameter(10, 12, 0, 64);
			cStmt.registerOutParameter(11, 12, 0, 64);
			cStmt.registerOutParameter(12, 12, 0, 64);
			cStmt.registerOutParameter(13, 12, 0, 64);

			cStmt.execute();

		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	//@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
//
//	@Override
//	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
//		return null;
//	}
}
