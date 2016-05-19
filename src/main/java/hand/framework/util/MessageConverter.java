package hand.framework.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MessageConverter extends StringHttpMessageConverter {

	public MessageConverter(Charset defaultCharset) {  
        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(new MediaType("text", "plain", defaultCharset));
        mediaTypeList.add(MediaType.ALL);
        super.setSupportedMediaTypes(mediaTypeList);  
    }  
}
