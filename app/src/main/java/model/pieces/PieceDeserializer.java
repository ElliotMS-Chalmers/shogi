package model.pieces;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import model.PieceFactory;

import java.io.IOException;

public class PieceDeserializer extends JsonDeserializer<Piece> {

    @Override
    public Piece deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String sfenAbbreviation = node.asText();
        return PieceFactory.fromSfenAbbreviation(sfenAbbreviation);
    }
}
