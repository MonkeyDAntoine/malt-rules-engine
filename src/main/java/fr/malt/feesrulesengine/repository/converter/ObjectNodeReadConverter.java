package fr.malt.feesrulesengine.repository.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@ReadingConverter
@Component
@RequiredArgsConstructor
public class ObjectNodeReadConverter implements Converter<DBObject, ObjectNode> {
  private final ObjectMapper objectMapper;

  @Override
  public ObjectNode convert(DBObject source) {
    try {
      return objectMapper.readValue(source.toString(), ObjectNode.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
