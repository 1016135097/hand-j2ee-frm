package hand.framework.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
	
	private static JsonConfig jsonConfig;
	static{
		jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new SqlDateProcessor());
        NumberProcessor numberProcessor = new NumberProcessor();
        jsonConfig.registerJsonValueProcessor(java.math.BigDecimal.class, numberProcessor);
        jsonConfig.registerJsonValueProcessor(Integer.class, numberProcessor);
        jsonConfig.registerJsonValueProcessor(Double.class, numberProcessor);
        jsonConfig.registerJsonValueProcessor(Float.class, numberProcessor);
	}
	
	 // 将数组转换成JSON   
    public static String array2json(Object object) {   
        JSONArray jsonArray = JSONArray.fromObject(object,jsonConfig);   
        return jsonArray.toString();   
    }   
   
    // 将JSON转换成数组,其中valueClz为数组中存放的对象的Class   
    public static Object json2Array(String json, Class valueClz) {   
        JSONArray jsonArray = JSONArray.fromObject(json,jsonConfig);   
        return JSONArray.toArray(jsonArray, valueClz);   
    }   
   
    // 将Collection转换成JSON   
    public static String collection2json(Object object) {   
        JSONArray jsonArray = JSONArray.fromObject(object,jsonConfig);   
        return jsonArray.toString();   
    }   
   
    // 将JSON转换成Collection,其中collectionClz为Collection具体子类的Class,   
    // valueClz为Collection中存放的对象的Class   
    public static Collection json2Collection(String json, Class collectionClz,   
            Class valueClz) {   
        JSONArray jsonArray = JSONArray.fromObject(json,jsonConfig);   
        return JSONArray.toCollection(jsonArray, valueClz);   
    }   
   
    // 将Map转换成JSON   
    public static String map2json(Object object) {   
        JSONObject jsonObject = JSONObject.fromObject(object,jsonConfig);   
        return jsonObject.toString();   
    }   
   
    // 将JSON转换成Map,其中valueClz为Map中value的Class,keyArray为Map的key   
    public static Map json2Map(Object[] keyArray, String json, Class valueClz) {   
        JSONObject jsonObject = JSONObject.fromObject(json,jsonConfig);   
        Map classMap = new HashMap();   
   
        for (int i = 0; i < keyArray.length; i++) {   
            classMap.put(keyArray[i], valueClz);   
        }   
   
        return (Map) JSONObject.toBean(jsonObject, Map.class, classMap);   
    }   
   
    // 将POJO转换成JSON   
    public static String bean2json(Object object) {   
        JSONObject jsonObject = JSONObject.fromObject(object,jsonConfig);   
        return jsonObject.toString();   
    }   
   
    // 将JSON转换成POJO,其中beanClz为POJO的Class   
    public static Object json2Object(String json, Class beanClz) {   
        return JSONObject.toBean(JSONObject.fromObject(json,jsonConfig), beanClz);   
    }   
   
    // 将String转换成JSON   
    public static String string2json(String key, String value) {   
        JSONObject object = new JSONObject();   
        object.put(key, value);   
        return object.toString();   
    }   
   
    // 将JSON转换成String   
    public static String json2String(String json, String key) {   
        JSONObject jsonObject = JSONObject.fromObject(json,jsonConfig);   
        return jsonObject.get(key).toString();   
    }  
	
    public static String commonSuccess(String msg,Map<String,Object> data){
    	JSONObject j1 = new JSONObject();
    	j1.put("success","true");
    	j1.put("msg", msg);
    	j1.put("status", "S");
    	
    	JSONObject j2 = JSONObject.fromObject(data);
    	j1.put("data", j2);
    	
    	return j1.toString();
    }
    public static String error(String msg,Map<String,Object> data){
		JSONObject j1 = new JSONObject();
		j1.put("success","false");
		j1.put("msg", msg);
    	j1.put("status", "E");
    	
    	JSONObject j2 = JSONObject.fromObject(data);
    	j1.put("data", j2);
    	return j1.toString();
	}
    
	public static String commonError(String msg,Map<String,Object> data){
		JSONObject j1 = new JSONObject();
		j1.put("success","true");
		j1.put("msg", msg);
    	j1.put("status", "E");
    	
    	JSONObject j2 = JSONObject.fromObject(data);
    	j1.put("data", j2);
    	return j1.toString();
	}
	
	public static void main(String[] args) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("headerId",2);
		params.put("type", 1);
		System.out.println(JsonUtils.commonError(null, params));
	}
	
	public static String resultSetToJson(ResultSet rs) throws SQLException
	{  
	   // json数组  
	   JSONArray array = new JSONArray();  
	    
	   // 获取列数  
	   ResultSetMetaData metaData = rs.getMetaData();  
	   int columnCount = metaData.getColumnCount();  
	    
	   // 遍历ResultSet中的每条数据  
	    while (rs.next()) {  
	        JSONObject jsonObj = new JSONObject();  
	         
	        // 遍历每一列  
	        for (int i = 1; i <= columnCount; i++) {  
	            String columnName =metaData.getColumnLabel(i);  
	            String value = rs.getString(columnName);  
	            jsonObj.put(columnName, value);  
	        }   
	        array.add(jsonObj);   
	    }  
	    
	   return array.toString();  
	}  
}
