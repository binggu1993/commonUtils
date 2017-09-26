package com.avit.common.json.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class JsonDateFormatFull  extends JsonSerializer<Date> 
{
    
    
    
    @Override
    public void serialize(Date value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter =   new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }



  
    
    
}
