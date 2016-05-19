package hand.framework.plsql.util;


import hand.framework.entity.CallableEntity;
import hand.framework.plsql.CallableConfig;
import hand.framework.plsql.excutor.CallableExcutor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CallableManager {

	private static Logger logger = Logger.getLogger(CallableManager.class);

	private Map<String, CallableConfig> callables = new HashMap<String, CallableConfig>();
	
	private String configFiles;
	
	private CallableExcutor excutor;
	
	public CallableManager(){
		
	}
	
	public void init(){
		String[] filePaths = configFiles.split(",");
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

			logger.info("filePath:"+sb.toString());
			if (file.isDirectory()) {
				File[] fs = file.listFiles();
				for (File xmlFile : fs) {
					if (!xmlFile.getName().endsWith(".xml")) {
						continue;
					}
					try {
						DocumentBuilder builder = f.newDocumentBuilder();
						Document doc = builder.parse(new FileInputStream(xmlFile));
						Node root = doc.getFirstChild();
						NodeList nodes = root.getChildNodes();
						for (int i = 0; i < nodes.getLength(); i++) {
							Node n = nodes.item(i);
							if ("call".equals(n.getNodeName())) {
								String id = n.getAttributes().item(0)
										.getNodeValue();
								
								String sql = n.getTextContent().trim();
								
								
								CallableConfig config = new CallableConfig(id,sql);
								putConfig(id, config);
								logger.info("call:" + id + " loaded.");
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
	
	public void excuteCall(String id,Map<String,Object> params){
		CallableConfig c = getConfig(id);
		if(c == null){
			throw new RuntimeException("Can not find Callable Sql with ID:"+id);
		}
		CallableEntity ce = c.getCallableEntity();
		ce.setParams(params);
		excutor.excuteCallable(ce);
	}

	public String getConfigFiles() {
		return configFiles;
	}

	public void setConfigFiles(String configFiles) {
		this.configFiles = configFiles;
	}
	
	public void putConfig(String id,CallableConfig c){
		this.callables.put(id, c);
	}
	
	public void destory() {
		this.callables.clear();
	}
	
	public CallableConfig getConfig(String id){
		return this.callables.get(id);
	}

	public CallableExcutor getExcutor() {
		return excutor;
	}

	public void setExcutor(CallableExcutor excutor) {
		this.excutor = excutor;
	}
	
}
