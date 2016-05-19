package hand.framework.lookup.utils;

import hand.framework.json.JsonUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LookupManager {

	private static Logger logger = Logger.getLogger(LookupManager.class);

	private String lookupFiles;

	private DataSource dataSource;

	private Map<String, String> lookups = new HashMap<String, String>();

	public String getLookupFiles() {
		return lookupFiles;
	}

	public void setLookupFiles(String lookupFiles) {
		this.lookupFiles = lookupFiles;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		logger.info("Loading Lov Configs...");

		String[] filePaths = lookupFiles.split(",");
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		
		String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
		webRoot = webRoot.substring(0,webRoot.indexOf("WEB-INF"));
		
		for (String filePath : filePaths) {

			StringBuilder sb = new StringBuilder();
			sb.append(webRoot);

			if (filePath.contains("classpath:")) {
				filePath = filePath.replace("classpath:", "").trim();
				sb.append("WEB-INF/classes/");
			}
			sb.append(filePath);

			File file = new File(sb.toString());

			logger.info("filePath:" + sb.toString());
			if (file.isDirectory()) {
				File[] fs = file.listFiles();
				for (File xmlFile : fs) {
					if (!xmlFile.getName().endsWith(".xml")) {
						continue;
					}
					try {
						DocumentBuilder builder = f.newDocumentBuilder();
						Document doc = builder.parse(new FileInputStream(
								xmlFile));
						Node root = doc.getFirstChild();
						NodeList nodes = root.getChildNodes();
						for (int i = 0; i < nodes.getLength(); i++) {
							Node n = nodes.item(i);
							if ("WEB-INF/lookup".equals(n.getNodeName())) {
								String lookupId = n.getAttributes().item(0) .getNodeValue();

								NodeList children = n.getChildNodes();
								Map<String, String> m = new HashMap<String, String>();
								for (int j = 0; j < children.getLength(); j++) {
									Node c = children.item(j);
									if (!"#text".equals(c.getNodeName())) {
										m.put(c.getNodeName().toLowerCase(), c
												.getTextContent().trim());
									}
								}

								String sql = m.get("sql");
								lookups.put(lookupId, sql);
								logger.info("Lookup:" + lookupId + " loaded.");
								System.out.println("Lookup:" + lookupId + " loaded.");
							}
						}
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void destory() {
		lookups.clear();
	}
	
	public String queryLookup(String lookupId,List<String> conditions,List<String> params) {
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();

			String querySql = lookups.get(lookupId);
			if(querySql == null){
				throw new RuntimeException("Could not find Lookup:"+lookupId);
			}
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT * FROM (").append(querySql).append(") WHERE 1=1");
			
			if (conditions!= null) {
				for (int i = 0; i < conditions.size(); i++) {
					String condition = conditions.get(i);
					sb.append(" AND ").append(condition).append(" = ? ");
				}
			}
			
			querySql = sb.toString();

			logger.info(querySql);

			PreparedStatement sta = conn.prepareStatement(querySql);

			if (params!= null) {
				for (int i = 0; i < params.size(); i++) {
					String param = params.get(i);
					sta.setString(i + 1, param);
				}
			}
//			System.out.println(querySql);
			ResultSet rs = sta.executeQuery();

			String resultString = JsonUtils.resultSetToJson(rs);
			
			rs.close();
			
			return resultString;
			
		} catch (SQLException e) {
			System.out.println(lookupId);
			e.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lookupId;
	}
	
	
}
