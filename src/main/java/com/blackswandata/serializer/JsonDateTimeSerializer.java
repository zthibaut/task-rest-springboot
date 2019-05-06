package com.blackswandata.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMATTER;

public class JsonDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider provider) throws IOException {
    String formattedDateTime = dateTime.format(DATE_TIME_FORMATTER);
    gen.writeString(formattedDateTime);
  }
}
