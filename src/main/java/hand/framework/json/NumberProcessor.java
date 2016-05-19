package hand.framework.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class NumberProcessor implements JsonValueProcessor {

    public NumberProcessor() {  
        super();  
    }  
    public Object processArrayValue(Object paramObject,  
            JsonConfig paramJsonConfig) {
        return process(paramObject);  
    }  
  
    public Object processObjectValue(String paramString, Object paramObject,  
            JsonConfig paramJsonConfig) {
        return process(paramObject);  
    }  
      
    private Object process(Object value){  
        return value == null ? "" : value.toString();    
    }
}
