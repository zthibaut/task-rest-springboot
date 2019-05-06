package com.blackswandata.serializer;

import com.blackswandata.exception.DateTimeNotValidException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import static java.util.Objects.isNull;

import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMATTER;

public class JsonDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext ctx) throws IOException {
    String dateTimeString = jsonparser.getText();
    if(isNull(dateTimeString)) {
      throw new DateTimeNotValidException("");
    }
    try {
      return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new DateTimeNotValidException(dateTimeString);
    }
  }
}
