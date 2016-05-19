package hand.framework.lov.utils;

import hand.framework.ebs.util.MOACUtil;
import hand.framework.lov.entity.GeneralLov;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LovManager {

	private static Logger logger = Logger.getLogger(LovManager.class);

	private String lovFiles;

	private DataSource dataSource;

	private Map<String, GeneralLov> lovs = new HashMap<String, GeneralLov>();

	public LovManager() {
	}

	public String getLovFiles() {
		return lovFiles;
	}

	public void setLovFiles(String lovFiles) {
		this.lovFiles = lovFiles;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		logger.info("Loading Lov Configs...");

		String[] filePaths = lovFiles.split(",");
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
							if ("WEB-INF/lov".equals(n.getNodeName())) {
								String lovId = n.getAttributes().item(0)
										.getNodeValue();

								NodeList children = n.getChildNodes();
								Map<String, String> m = new HashMap<String, String>();
								for (int j = 0; j < children.getLength(); j++) {
									Node c = children.item(j);
									if (!"#text".equals(c.getNodeName())) {
										m.put(c.getNodeName().toLowerCase(), c
												.getTextContent().trim());
									}

								}
								GeneralLov tLov = new GeneralLov(lovId,
										m.get("sql"), m.get("moac"),m.get("query_condition")
												.split(","), m.get(
												"query_condition_desc").split(
												","), m.get("result_display")
												.split(","), m.get(
												"result_display_desc").split(
												","), m.get("return")
												.split(","));
								putLov(lovId, tLov);
								logger.info("lov:" + lovId + " loaded.");
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
		lovs.clear();
	}

	public void queryLov(GeneralLov lov) {
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
		Connection conn = null;
		try {
			// MOAC
			if("Y".equals(lov.getMoFlag())){
				MOACUtil.init("M");
			}
			
			conn = this.dataSource.getConnection();

			StringBuilder sb = new StringBuilder();
			sb.append(" select t.*");

			StringBuilder sb2 = new StringBuilder();
			sb2.append(" select count(1) from(");

			String eSql = lov.getSql();// .replaceFirst("select",
										// "select rownum rn,");

			sb2.append(eSql);

			String outLimitClause = "";

			eSql = "select rownum as rn,a.* from(" + eSql + ") a where  1= 1 "
					+ lov.getQueryWhereClause();

			if (lov.isPaging()) {
				eSql = eSql + " AND rownum <= " + lov.getEnd();

				outLimitClause = " AND rn >=" + lov.getStart();
			}

			sb.append(" from (").append(eSql).append(") t where 1=1 ")
					.append(outLimitClause);

			sb2.append(") where 1=1 ").append(lov.getQueryWhereClause());

			String querySql = sb.toString();
			String countSql = sb2.toString();

			logger.info(querySql);
//			System.out.println(querySql);
			logger.info(countSql);

			PreparedStatement sta = conn.prepareStatement(querySql);
			PreparedStatement sta2 = conn.prepareStatement(countSql);

			List<String> params = lov.getParams();
			if (lov.getParams() != null) {
				for (int i = 0; i < params.size(); i++) {
					String param = params.get(i);
					
					sta.setString(i + 1, param);
					sta2.setString(i + 1, param);
				}
			}

			ResultSet rs = sta.executeQuery();

			String[] column = lov.getConfig().getColumn();
			String[] alias = lov.getConfig().getAlias();

			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();

				for (int i = 0; i < column.length; i++) {
					map.put(alias[i], rs.getObject(alias[i]));
				}
				valueList.add(map);
			}

			ResultSet rs2 = sta2.executeQuery();
			int totalCount = 0;

			if (rs2.next()) {
				totalCount = rs2.getInt(1);
			}

			lov.setQueryWhereClause(null);
			lov.setParams(null);

			lov.getQueryResult().setValueList(valueList);
			lov.getQueryResult().setTotalCount(totalCount);
		} catch (SQLException e) {
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
	}

	public GeneralLov getLov(String key) {
		return lovs.get(key);
	}

	public void putLov(String key, GeneralLov lov) {
		lovs.put(key, lov);
	}
}
