package model.pieces;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Custom deserializer for the `Piece` class used with Jackson.
 * This class is responsible for deserializing Shogi piece data from a JSON string.
 * It interprets the SFEN abbreviation (e.g., "P" for a sente pawn) and uses the
 * piece factory to create the correct type of `Piece` based on the abbreviation.
 */
public class PieceDeserializer extends JsonDeserializer<Piece> {

    /**
     * Deserializes a JSON string representing a Shogi piece (using its SFEN abbreviation) into a `Piece` object.
     *
     * @param p the JSON parser used to read the JSON content.
     * @param ctxt the deserialization context.
     * @return the corresponding `Piece` object based on the SFEN abbreviation.
     * @throws IOException if an I/O error occurs while deserializing.
     * @throws JsonProcessingException if a JSON processing error occurs.
     */
    @Override
    public Piece deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // Read the JSON node (SFEN abbreviation) as text
        JsonNode node = p.getCodec().readTree(p);
        String sfenAbbreviation = node.asText();

        // Use the PieceFactory to create the correct Piece based on the SFEN abbreviation
        return PieceFactory.fromSfenAbbreviation(sfenAbbreviation);
    }
}
