package cn.xiaochi.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 日期处理
 * 使用时在属性上加注解 @JsonSerialize(using = Date2LongSerializer.class)
 */
public class Date2LongSerializer extends JsonSerializer<Date> {

    // 需要的是毫秒数，实际是微妙
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeNumber(date.getTime() / 1000);
    }
}
