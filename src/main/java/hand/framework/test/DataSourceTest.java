package hand.framework.test;

import oracle.apps.fnd.security.AppsDataSource;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.pool.OracleDataSourceFactory;

import javax.naming.Referenceable;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by Edward on 2016-05-24.
 */
public class DataSourceTest {
    //apps user with umx| APPS_SCHEMA_CONNECT priviliges (default asadmin)
    public static String user = "ASADMIN";
    public static String pwd = "asadmin";
    public static String dbcFile = "";

    public static void testConnection(String dbcFile)
            throws Exception {
        System.out.println("Running testConnection DBC version...");
        AppsDataSource v2 = new AppsDataSource();
        v2.setUser(user);
        v2.setPassword(pwd);
        v2.setDbcFile(dbcFile);

        Object ods = new OracleDataSourceFactory().getObjectInstance(v2.getReference(), null, null, null);
        DataSource ds = (DataSource) ods;
        testSql(ds.getConnection());
    }

    public static void testSql(Connection conn)
            throws Exception {
        String sql = "BEGIN fnd_profile.get(:1,:2); END;";
        OracleCallableStatement cStmt = (OracleCallableStatement) conn.prepareCall(sql);
        cStmt.setString(1, "APPS_WEB_AGENT");
        cStmt.setString(2, null);
        cStmt.registerOutParameter(2, java.sql.Types.VARCHAR, 0, 100);
        cStmt.execute();
        String profVal = cStmt.getString(2);
        System.out.println(profVal);
    }

    public static void main(String j[])
            throws Exception {
        System.out.println(new AppsDataSource() instanceof Referenceable);
        //this.getClass().getClassLoader();
        dbcFile = "/D:/WorkSpace/GitHub/hand-j2ee-frm/src/main/webapp/WEB-INF/dbc/DBC.dbc";
//        if(dbcFile.startsWith("/WEB-INF")){
//            dbcFile = webRoot + dbcFile;
//        }
        testConnection(dbcFile);
    }
}

