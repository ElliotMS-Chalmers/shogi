
package model.pieces;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Test;
import util.Side;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

        class PieceDeserializerTest {

            /**
             * Custom implementation of JsonParser for testing.
             * It wraps an input string and returns a mock `JsonNode` to simulate JSON parsing behavior.
             */
            private static class TestJsonParser extends JsonParser {
                private final String text;
                private final ObjectCodec codec;

                public TestJsonParser(String text) {
                    this.text = text;
                    this.codec = new TestObjectCodec(new TextNode(text));
                }

                @Override
                public ObjectCodec getCodec() {
                    return codec;
                }

                @Override
                public void setCodec(ObjectCodec c) {
                    throw new UnsupportedOperationException("Not supported in the test parser.");
                }

                @Override
                public Version version() {
                    return null;
                }

                @Override
                public void close() {
                    // No operation needed for test
                }

                @Override
                public boolean isClosed() {
                    return false;
                }

                @Override
                public JsonStreamContext getParsingContext() {
                    return null;
                }

                @Override
                public JsonLocation getCurrentLocation() {
                    return null;
                }

                @Override
                public JsonLocation getTokenLocation() {
                    return null;
                }

                @Override
                public JsonToken nextToken() throws IOException {
                    return null;
                }

                @Override
                public JsonToken nextValue() throws IOException {
                    return null;
                }

                @Override
                public JsonParser skipChildren() throws IOException {
                    return null;
                }

                @Override
                public JsonToken getCurrentToken() {
                    return null;
                }

                @Override
                public int getCurrentTokenId() {
                    return 0;
                }

                @Override
                public boolean hasCurrentToken() {
                    return false;
                }

                @Override
                public boolean hasTokenId(int i) {
                    return false;
                }

                @Override
                public boolean hasToken(JsonToken jsonToken) {
                    return false;
                }

                @Override
                public void clearCurrentToken() {

                }

                @Override
                public JsonToken getLastClearedToken() {
                    return null;
                }

                @Override
                public void overrideCurrentName(String s) {

                }

                @Override
                public String getCurrentName() throws IOException {
                    return "";
                }

                @Override
                public String getText() throws IOException {
                    return "";
                }

                @Override
                public char[] getTextCharacters() throws IOException {
                    return new char[0];
                }

                @Override
                public int getTextLength() throws IOException {
                    return 0;
                }

                @Override
                public int getTextOffset() throws IOException {
                    return 0;
                }

                @Override
                public boolean hasTextCharacters() {
                    return false;
                }

                @Override
                public Number getNumberValue() throws IOException {
                    return null;
                }

                @Override
                public NumberType getNumberType() throws IOException {
                    return null;
                }

                @Override
                public int getIntValue() throws IOException {
                    return 0;
                }

                @Override
                public long getLongValue() throws IOException {
                    return 0;
                }

                @Override
                public BigInteger getBigIntegerValue() throws IOException {
                    return null;
                }

                @Override
                public float getFloatValue() throws IOException {
                    return 0;
                }

                @Override
                public double getDoubleValue() throws IOException {
                    return 0;
                }

                @Override
                public BigDecimal getDecimalValue() throws IOException {
                    return null;
                }

                @Override
                public byte[] getBinaryValue(Base64Variant base64Variant) throws IOException {
                    return new byte[0];
                }

                @Override
                public String getValueAsString(String s) throws IOException {
                    return "";
                }

                // Other methods overridden but left unimplemented since they are not needed for the test
            }

            /**
             * Custom implementation of ObjectCodec for testing purposes.
             */
            private static class TestObjectCodec extends ObjectCodec {
                private final JsonNode node;

                public TestObjectCodec(JsonNode node) {
                    this.node = node;
                }

                @Override
                public JsonNode readTree(JsonParser p) throws IOException {
                    return node;
                }

                @Override
                public Version version() {
                    return null;
                }

                @Override
                public <T> T readValue(JsonParser p, Class<T> clazz) {
                    return null;
                }

                @Override
                public <T> T readValue(JsonParser jsonParser, TypeReference<T> typeReference) throws IOException {
                    return null;
                }

                @Override
                public <T> T readValue(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
                    return null;
                }

                @Override
                public <T> Iterator<T> readValues(JsonParser jsonParser, Class<T> aClass) throws IOException {
                    return null;
                }

                @Override
                public <T> Iterator<T> readValues(JsonParser jsonParser, TypeReference<T> typeReference) throws IOException {
                    return null;
                }

                @Override
                public <T> Iterator<T> readValues(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
                    return null;
                }

                @Override
                public void writeValue(JsonGenerator jsonGenerator, Object o) throws IOException {

                }

                @Override
                public void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException {

                }

                @Override
                public TreeNode createObjectNode() {
                    return null;
                }

                @Override
                public TreeNode createArrayNode() {
                    return null;
                }

                @Override
                public JsonParser treeAsTokens(TreeNode treeNode) {
                    return null;
                }

                @Override
                public <T> T treeToValue(TreeNode treeNode, Class<T> aClass) throws JsonProcessingException {
                    return null;
                }

                // Other ObjectCodec methods are unimplemented as they are unnecessary for this test
            }

            @Test
            void testDeserialize_ValidSfenAbbreviation() throws IOException {
                // Arrange
                String input = "P"; // Sente Pawn SFEN abbreviation
                TestJsonParser parser = new TestJsonParser(input);
                DeserializationContext context = null; // Not used in this test

                PieceDeserializer deserializer = new PieceDeserializer();

                // Act
                Piece result = deserializer.deserialize(parser, context);

                // Assert
                assertNotNull(result, "The deserialized piece should not be null.");
                assertEquals(Pawn.class, result.getClass(), "The deserialized piece should be a Pawn.");
                assertEquals(Side.SENTE, result.getSide(), "The deserialized piece should belong to the SENTE side.");
            }

            @Test
            void testDeserialize_InvalidSfenAbbreviation() {
                // Arrange
                String input = "X"; // Invalid SFEN abbreviation
                TestJsonParser parser = new TestJsonParser(input);
                DeserializationContext context = null;

                PieceDeserializer deserializer = new PieceDeserializer();

                // Act & Assert
                Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                    deserializer.deserialize(parser, context);
                });

                assertTrue(exception.getMessage().contains("Invalid sfen abbreviation: X"),
                        "The exception message should match the expected result.");
            }

            @Test
            void testDeserialize_EmptyInput() throws IOException {
                // Arrange
                String input = ""; // Empty input
                TestJsonParser parser = new TestJsonParser(input);
                DeserializationContext context = null;

                PieceDeserializer deserializer = new PieceDeserializer();

                // Act & Assert
                Exception exception = assertThrows(StringIndexOutOfBoundsException.class, () -> {
                    deserializer.deserialize(parser, context);
                });

                assertEquals("Index -1 out of bounds for length 0", exception.getMessage(),
                        "The exception message should indicate an invalid SFEN value.");
            }

            @Test
            void testDeserialize_NullInput() throws IOException {
                // Arrange
                String input = null; // Simulate null JSON input
                TestJsonParser parser = new TestJsonParser(input);
                DeserializationContext context = null;

                PieceDeserializer deserializer = new PieceDeserializer();

                // Act & Assert
                Exception exception = assertThrows(NullPointerException.class, () -> {
                    deserializer.deserialize(parser, context);
                });

                assertNotNull(exception, "A NullPointerException should be thrown when input is null.");
            }
        }

