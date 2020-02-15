package mil.nga.giat.data.elasticsearch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.IOException;

public class TotalDeserializer extends StdDeserializer<Long> {

    public TotalDeserializer() {
        this(null);
    }

    public TotalDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            return jsonParser.readValueAs(Long.class);
        }  catch (MismatchedInputException e) {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            return node.get("value").longValue();
        }
    }

}
