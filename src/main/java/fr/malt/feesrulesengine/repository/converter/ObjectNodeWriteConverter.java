package fr.malt.feesrulesengine.repository.converter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class ObjectNodeWriteConverter implements Converter<ObjectNode, DBObject> {
  @Override
  public DBObject convert(ObjectNode source) {
    return BasicDBObject.parse(source.toString());
  }
}
