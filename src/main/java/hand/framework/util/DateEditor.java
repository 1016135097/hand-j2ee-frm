package hand.framework.util;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateEditor extends PropertyEditorSupport{

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy/MM/dd");  
    private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
    
    private DateFormat dateFormat;
   
    public DateEditor() {  
    }

	@Override
	public String getAsText() {
		  Date value = (Date) getValue();  
	        DateFormat dateFormat = this.dateFormat;  
	        if(dateFormat == null)  
	            dateFormat = TIMEFORMAT;  
		return (value != null ? dateFormat.format(value) : "");  
	}
	
	@Override  
    public void setAsText(String text) throws IllegalArgumentException {  
        if ( !StringUtils.hasText(text) || "null".equals(text)) {
            setValue(null);  
        } else {  
            try {  
                if(this.dateFormat != null)  
                    setValue(new Date(this.dateFormat.parse(text).getTime()));  
                else {  
                    if(text.contains(":"))  
						setValue(TIMEFORMAT.parse(text));  
                    else  
                        setValue(new Date(DATEFORMAT.parse(text).getTime()));  
                }  
            } catch (ParseException ex) {  
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);  
            }  
        }  
    }  
  

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}  
  
    
}
